package bancoCentral.model;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.Objects;
import java.util.Set;

public class Database {
    private final static String file = "db.json";
    private String path;
    private File fileObject;
    private JSONObject db = new JSONObject();


    public JSONObject getDb() {
        return db;
    }

    public Database(){
        File f = new File(file);
        if(f.exists() && !f.isDirectory()) {
            this.fileObject = f;
            if(f.length() == 0){
                initDB(f);
            }else{
                this.db = readFile();
            }
        }else{
            initDB(f);
        }
    }

    private void initDB(File f){
        try{
            f.createNewFile();
            this.path = f.getAbsolutePath();
            FileWriter myDB = new FileWriter(file);
            JSONObject dbJson = new JSONObject();
            dbJson.put("banks", new JSONArray());
            dbJson.put("publicAccountInfos", new JSONArray());
            dbJson.put("collects", new JSONArray());
            dbJson.put("costumerLogged", new JSONObject());
            myDB.write(dbJson.toString());
            myDB.close();
            this.db = dbJson;
        }catch(IOException e){
            e.printStackTrace();
        }

    }


    private JSONObject readFile(){
        JSONParser parser = new JSONParser();
        try {
            JSONObject data = (JSONObject) parser.parse(new FileReader(this.file));
            this.db = data;
            return data;
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }


    private void writeFile(JSONObject data){
        try{
            FileWriter writeFile = new FileWriter(this.file);
            writeFile.write(data.toJSONString());
            writeFile.close();
            this.db = data;
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }


    public JSONObject findOne(String key, JSONObject query){
        this.db = readFile();
        JSONArray data = (JSONArray) db.get(key);
        return getJsonObject(query, data);
    }


    private JSONObject findOne(JSONObject query, JSONArray ObjectToSearch){
        this.db = readFile();
        JSONArray data = ObjectToSearch;
        return getJsonObject(query, data);
    }


    private JSONObject getJsonObject(JSONObject query, JSONArray data) {
        Object[] dataConvert = data.toArray();
        Set<String> allKeys = query.keySet();
        Object[] allKeysArray = allKeys.toArray();
        int keyMatch = 0;

        for(int i = 0; i <  data.size(); i++){
            JSONObject currentObject = (JSONObject) dataConvert[i];
            for (Object o : allKeysArray) {
                if (!currentObject.containsKey(o)) return new JSONObject();
                if (currentObject.get(o).hashCode() != query.get(o).hashCode()) {
                    break;
                }
                if(keyMatch == allKeysArray.length){
                    break;
                }
                keyMatch++;
            }
            if(keyMatch != allKeysArray.length){
                continue;
            }
            return currentObject;
        }
        return new JSONObject();
    }


    public JSONObject findBank(JSONObject query){
        this.db = readFile();
        return findOne("banks", query);
    }


    public JSONObject findPublicAccountInfos(JSONObject query){
        this.db = readFile();
        return findOne("publicAccountInfos", query);
    }


    public JSONObject findCostumerByBank(String bankID, JSONObject query){
        this.db = readFile();
        JSONObject bankSearchQuery = new JSONObject();
        bankSearchQuery.put("id", bankID);
        JSONObject bank = findBank(bankSearchQuery);
        JSONArray costumers = (JSONArray) bank.get("costumers");
        return findOne(query, costumers);
    }


    public void addBank(JSONObject bankToAdd){
        this.db = readFile();
        JSONArray banks = (JSONArray) this.db.get("banks");
        banks.add(bankToAdd);
        this.db.replace("banks", banks);
        writeFile(this.db);
    }


    public int numberOfCostumers(String bankID){
        this.db = readFile();
        JSONObject query = new JSONObject();
        query.put("id", bankID);
        JSONObject bank = findBank(query);

        return (int) bank.get("numberOfCostumers");
    }


    public void removeBank(JSONObject bankToRemove){
        this.db = readFile();
        JSONArray banks = (JSONArray) this.db.get("banks");
        banks.remove(bankToRemove);
        this.db.replace("banks", banks);
        writeFile(this.db);
    }


    public JSONObject getCostumerInfoByString(String email){
        JSONObject query = new JSONObject();
        query.put("email", email);
        return findPublicAccountInfos(query);
    }


    public void dbUpdate(){
        this.db = readFile();
    }


    public Response removeBank(String bankID){
        this.db = readFile();
        JSONObject bankSearchQuery = new JSONObject();
        bankSearchQuery.put("id", bankID);
        JSONObject bank = findBank(bankSearchQuery);
        if(bank.isEmpty()){
            return new Response("Não existe banco com esse ID", false);
        }

        JSONArray banks = (JSONArray) this.db.get("banks");
        banks.remove(bank);

        this.db.replace("banks", banks);
        writeFile(this.db);
        return new Response("Banco exclúido com sucesso", true);
    }


    public void addCostumer(String bankID, JSONObject costumerToAdd){
        this.db = readFile();
        JSONObject bankSearchQuery = new JSONObject();
        bankSearchQuery.put("id", bankID);

        JSONObject bank = findBank(bankSearchQuery);
        JSONArray banks = (JSONArray) this.db.get("banks");
        banks.remove(bank);

        JSONArray costumers = (JSONArray) bank.get("costumers");
        costumers.add(costumerToAdd);
        bank.replace("costumers", costumers);

        banks.add(bank);
        this.db.replace("banks", banks);
        writeFile(this.db);
    }


    public Response removeCostumer(String bankID, JSONObject costumerToRemove){
        this.db = readFile();
        JSONObject bankSearchQuery = new JSONObject();
        bankSearchQuery.put("id", bankID);

        JSONObject bank = findBank(bankSearchQuery);
        if(bank.isEmpty()){
            return new Response("Não existe banco com esse ID", false);
        }

        JSONArray banks = (JSONArray) this.db.get("banks");
        banks.remove(bank);

        JSONArray costumers = (JSONArray) bank.get("costumers");
        JSONObject costumerFinded = findOne(costumerToRemove, costumers);
        if(costumerFinded.isEmpty()){
            return new Response("Nenhum cliente encontrado", false);
        }
        costumers.remove(costumerFinded);

        bank.replace("costumers", costumers);
        banks.add(bank);
        this.db.replace("banks", banks);
        writeFile(this.db);

        return new Response("Cliente exclúido com sucesso", true);
    }


    public void addPublicAccountInfo(JSONObject pixKeyObject){
        this.db = readFile();
        JSONArray publicAccountInfos = (JSONArray) this.db.get("publicAccountInfos");
        publicAccountInfos.add(pixKeyObject);
        this.db.replace("publicAccountInfos", publicAccountInfos);
        writeFile(this.db);
    }


    public void removePublicAccountInfo(JSONObject pixKeyToRemove){
        this.db = readFile();
        JSONArray publicAccountInfos = (JSONArray) this.db.get("publicAccountInfos");
        publicAccountInfos.remove(pixKeyToRemove);
        this.db.replace("publicAccountInfos", publicAccountInfos);
        writeFile(this.db);
    }


    public void addCollect(JSONObject query){
        this.db = readFile();
        JSONArray collects = (JSONArray) this.db.get("collects");
        collects.add(query);
        this.db.replace("collects", collects);
        writeFile(this.db);
    }


    public JSONObject findCollect(JSONObject query){
        this.db = readFile();
        JSONArray collects = (JSONArray) this.db.get("collects");
        return findOne("collects", query);
    }

    public JSONObject findLastCollectOfCostumer(String pixKey){
        this.db = readFile();
        JSONArray collects = (JSONArray) this.db.get("collects");
        JSONObject lastCollect = (JSONObject) collects.get(0);

        for(int i = 1; i < collects.size(); i++){
            JSONObject currentCollect = (JSONObject) collects.get(i);
            int lastTimestamp = Integer.parseInt(lastCollect.get("timestamp").toString());
            int CurrentTimestamp = Integer.parseInt(currentCollect.get("timestamp").toString());
            if(Objects.equals(currentCollect.get("pixKey").toString(), pixKey)){
                if(lastTimestamp > CurrentTimestamp){
                    lastCollect = currentCollect;
                }
            }
        }
        return lastCollect;
    }


    public void removeCollect(JSONObject query){
        this.db = readFile();
        JSONArray collects = (JSONArray) this.db.get("collects");
        collects.remove(query);
        this.db.replace("collects", collects);
        writeFile(this.db);
    }


    public void updateCollect(JSONObject query){
        this.db = readFile();
        JSONArray collects = (JSONArray) this.db.get("collects");
        JSONObject collectToUpdateQuery = new JSONObject();
        collectToUpdateQuery.put("id", query.get("id").toString());
        JSONObject collectToUpdate = findCollect(collectToUpdateQuery);
        collects.remove(collectToUpdate);
        collects.add(query);
        this.db.replace("collects", collects);
        writeFile(this.db);
    }


    public JSONObject getCollectInfo(String collectID){
        this.db = readFile();
        JSONObject query = new JSONObject();
        query.put("id", collectID);
        return findOne("collects", query);
    }


    public void updateAccount(String bankID, String costumerID, JSONObject query){
        this.db = readFile();
        JSONObject bankQuery = new JSONObject();
        bankQuery.put("id", bankID);
        JSONObject bank = findBank(bankQuery);
        JSONArray banks = (JSONArray) db.get("banks");
        banks.remove(bank);
        JSONArray costumers = (JSONArray) bank.get("costumers");

        JSONObject costumerSearchQuery = new JSONObject();
        costumerSearchQuery.put("id", costumerID);
        JSONObject costumer = findCostumerByBank(bankID, costumerSearchQuery);

        costumers.remove(costumer);

        costumer.replace("account", query);
        costumers.add(costumer);

        bank.replace("costumers", costumers);
        banks.add(bank);

        this.db.replace("banks", banks);
        writeFile(db);
    }


    public Bank generateBankObject(JSONObject query){
        this.db = readFile();
        String bankName = (String) query.get("name");
        String bankID = (String) query.get("id");
        JSONArray costumers = (JSONArray) query.get("costumers");
        return new Bank(bankName, bankID, costumers);
    }


    public Costumer generateCostumerObject(JSONObject query){
        this.db = readFile();
        boolean hasCPF = query.containsKey("cpf");
        if(hasCPF){
            JSONObject accountObject = (JSONObject) query.get("account");
            String accountNumber = accountObject.get("number").toString();
            JSONArray historic = (JSONArray) accountObject.get("historic");
            String balanceStr = accountObject.get("balance").toString();
            float balance = Float.parseFloat(balanceStr);

            Account account = new Account(accountObject.get("bankID").toString(), accountNumber, balance, accountObject.get("pixKey").toString(), historic);
            System.out.println("Balance: " + account.getBalance());
            Andress andress = new Andress(query.get("cep").toString(), query.get("street").toString(), query.get("number").toString(), query.get("state").toString());
            account.setCostumerID(query.get("id").toString());

            return new CostumerPersonal(
                    query.get("id").toString(),
                    query.get("name").toString(),
                    query.get("email").toString(),
                    query.get("password").toString(),
                    query.get("cpf").toString(),account, query.get("phoneNumber").toString(), andress
            );
        }


        JSONObject accountObject = (JSONObject) query.get("account");
        String accountNumber = accountObject.get("number").toString();
        JSONArray historic = (JSONArray) accountObject.get("historic");
        float balance = Float.parseFloat(accountObject.get("balance").toString());
        Account account = new Account(accountObject.get("bankID").toString(), accountNumber, balance, accountObject.get("pixKey").toString(), historic);
        Andress andress = new Andress(query.get("cep").toString(), query.get("street").toString(), query.get("number").toString(), query.get("state").toString());
        account.setCostumerID(query.get("id").toString());

        return  new CostumerLegal(
                query.get("name").toString(),
                query.get("email").toString(),
                query.get("password").toString(),
                query.get("cnpj").toString(),account, query.get("phoneNumber").toString(), andress
        );
    }


    public Costumer getCostumerLogged(){
        this.db = readFile();
        JSONObject costumerLogged = (JSONObject) this.db.get("costumerLogged");
        JSONObject bankQuery = new JSONObject();
        bankQuery.put("bankLoginId", costumerLogged.get("bankLoginId"));
        JSONObject bankObject = findBank(bankQuery);



        JSONObject query = new JSONObject();
        query.put("email", costumerLogged.get("email").toString());
        query.put("id", costumerLogged.get("id").toString());
        JSONArray costumers = (JSONArray) bankObject.get("costumers");
        JSONObject costumerObject = findOne(query, costumers);

        return generateCostumerObject(costumerObject);
    }


    public Costumer loginCostumer(String bankLoginId, String email, String password){
        this.db = readFile();
        JSONObject bankQuery = new JSONObject();
        bankQuery.put("bankLoginId",bankLoginId);
        JSONObject bankObject = findBank(bankQuery);


        JSONObject query = new JSONObject();
        query.put("email", email);
        query.put("password", password);
        JSONArray costumers = (JSONArray) bankObject.get("costumers");

        JSONObject costumerObject = findOne(query, costumers);

        JSONObject costumerLogged = new JSONObject();
        costumerLogged.put("id", costumerObject.get("id").toString());
        costumerLogged.put("email", costumerObject.get("email").toString());
        costumerLogged.put("name", costumerObject.get("name").toString());
        costumerLogged.put("bankLoginId", bankLoginId);

        this.db.replace("costumerLogged", costumerLogged);
        writeFile(this.db);
        return generateCostumerObject(costumerObject);
    }

    public Costumer logout(){
        return new Costumer();
    }
    
}
