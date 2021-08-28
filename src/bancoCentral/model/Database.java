package bancoCentral.model;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.Set;

public class Database {
    private final static String file = "db.json";
    private String path;
    private File fileObject;
    private JSONObject db;

    public Database(){
        File f = new File(file);
        if(f.exists() && !f.isDirectory()) {
            this.fileObject = f;
            this.db = readFile();
        }else{
            try{
                System.out.println("TESTE");
                f.createNewFile();
                this.path = f.getAbsolutePath();
                FileWriter myDB = new FileWriter(file);
                JSONObject dbJson = new JSONObject();
                dbJson.put("banks", new JSONArray());
                myDB.write(dbJson.toString());
                myDB.close();
                this.db = dbJson;
            }catch(IOException e){
                e.printStackTrace();
            }
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

    private JSONObject findOne(String key, JSONObject query){
        JSONArray data = (JSONArray) db.get(key);
        return getJsonObject(query, data);
    }

    private JSONObject findOne(JSONObject query, JSONArray ObjectToSearch){
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
        return findOne("banks", query);
    }

    public JSONObject findPixKeyPublic(JSONObject query){
        return findOne("pixKeys", query);
    }

    public JSONObject findAccountByNumber(String number){
        JSONObject query = new JSONObject();
        query.put("number", number);
        return findOne("publicAccountInfos", query);
    }

    public JSONObject findCostumerByBank(String bankID, JSONObject query){
        JSONObject bankSearchQuery = new JSONObject();
        bankSearchQuery.put("id", bankID);
        JSONObject bank = findBank(bankSearchQuery);
        JSONArray costumers = (JSONArray) bank.get("costumers");
        return findOne(query, costumers);
    }

    public void addBank(JSONObject bankToAdd){
        JSONArray banks = (JSONArray) this.db.get("banks");
        banks.add(bankToAdd);
        this.db.replace("banks", banks);
        writeFile(this.db);
    }

    public int numberOfCostumers(String bankID){
        JSONObject query = new JSONObject();
        query.put("id", bankID);
        JSONObject bank = findBank(query);

        return (int) bank.get("numberOfCostumers");
    }

    public void removeBank(JSONObject bankToRemove){
        JSONArray banks = (JSONArray) this.db.get("banks");
        banks.remove(bankToRemove);
        this.db.replace("banks", banks);
        writeFile(this.db);
    }

    public Response removeBank(String bankID){
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

    public void addPixKeyPublic(JSONObject pixKeyObject){
        JSONArray publicAccountInfos = (JSONArray) this.db.get("publicAccountInfos");
        publicAccountInfos.add(pixKeyObject);
        this.db.replace("publicAccountInfos", publicAccountInfos);
        writeFile(this.db);
    }

    public void removePixKeyPublic(JSONObject pixKeyToRemove){
        JSONArray publicAccountInfos = (JSONArray) this.db.get("publicAccountInfos");
        publicAccountInfos.remove(pixKeyToRemove);
        this.db.replace("publicAccountInfos", publicAccountInfos);
        writeFile(this.db);
    }

    Bank generateBankObject(JSONObject query){
        String bankName = (String) query.get("name");
        String bankID = (String) query.get("id");
        Object[] costumers = (Object[]) query.get("name");

        return new Bank(bankName, bankID, costumers);
    }

    Costumer generateCostumerObject(JSONObject query){
        boolean hasCPF = query.containsKey("cpf");
        if(hasCPF){
            JSONObject accountObject = (JSONObject) query.get("account");
            String accountNumber = accountObject.get("number").toString();
            Account account = new Account(accountObject.get("id").toString(), accountNumber, accountObject.get("pixKey").toString());
            Andress andress = new Andress(query.get("cep").toString(), query.get("street").toString(), query.get("number").toString(), query.get("state").toString());

            return  new CostumerPersonal(
                    query.get("name").toString(),
                    query.get("email").toString(),
                    query.get("password").toString(),
                    query.get("cpf").toString(),account, query.get("phoneNumber").toString(), andress
            );
        }
        JSONObject accountObject = (JSONObject) query.get("account");
        String accountNumber = accountObject.get("number").toString();
        Account account = new Account(accountObject.get("id").toString(), accountNumber, accountObject.get("pixKey").toString());
        Andress andress = new Andress(query.get("cep").toString(), query.get("street").toString(), query.get("number").toString(), query.get("state").toString());

        return  new CostumerLegal(
                query.get("name").toString(),
                query.get("email").toString(),
                query.get("password").toString(),
                query.get("cnpj").toString(),account, query.get("phoneNumber").toString(), andress
        );
    }
    
}
