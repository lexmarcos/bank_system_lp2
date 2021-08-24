package bancoCentral.model;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.Set;

public class Database {
    private final static String file = "db.json";
    private String path;
    File fileObject;
    JSONObject db;

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

    public void writeFile(JSONObject data){
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
        Object[] dataConvert = data.toArray();
        Set<String> allKeys = query.keySet();
        Object[] allKeysArray = allKeys.toArray();
        int keyMatch = 0;

        for(int i = 0; i <  data.size(); i++){
            JSONObject currentObject = (JSONObject) dataConvert[i];
            for(int x = 0; x < allKeysArray.length; x++){
                if(!currentObject.containsKey(allKeysArray[x])) return new JSONObject();
                if(currentObject.get(allKeysArray[x]).hashCode() != query.get(allKeysArray[x]).hashCode()){
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

    private JSONObject findOne(JSONObject query, JSONArray ObjectToSearch){
        JSONArray data = ObjectToSearch;
        Object[] dataConvert = data.toArray();
        Set<String> allKeys = query.keySet();
        Object[] allKeysArray = allKeys.toArray();
        int keyMatch = 0;

        for(int i = 0; i <  data.size(); i++){
            JSONObject currentObject = (JSONObject) dataConvert[i];
            for(int x = 0; x < allKeysArray.length; x++){
                if(!currentObject.containsKey(allKeysArray[x])) return new JSONObject();
                if(currentObject.get(allKeysArray[x]).hashCode() != query.get(allKeysArray[x]).hashCode()){
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

    public void removeBank(JSONObject bankToRemove){
        JSONArray banks = (JSONArray) this.db.get("banks");
        banks.remove(bankToRemove);
        this.db.replace("banks", banks);
        writeFile(this.db);
    }

    public void removeBank(String bankID){
        JSONObject bankSearchQuery = new JSONObject();
        bankSearchQuery.put("id", bankID);
        JSONObject bank = findBank(bankSearchQuery);
        JSONArray banks = (JSONArray) this.db.get("banks");
        banks.remove(bank);
        this.db.replace("banks", banks);
        writeFile(this.db);
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

    public void removeCostumer(String bankID, JSONObject costumerToRemove){
        JSONObject bankSearchQuery = new JSONObject();
        bankSearchQuery.put("id", bankID);

        JSONObject bank = findBank(bankSearchQuery);
        JSONArray banks = (JSONArray) this.db.get("banks");
        banks.remove(bank);
        JSONArray costumers = (JSONArray) bank.get("costumers");

        JSONObject costumerFinded = findOne(costumerToRemove, costumers);

        costumers.remove(costumerFinded);
        bank.replace("costumers", costumers);

        banks.add(bank);
        this.db.replace("banks", banks);
        writeFile(this.db);
    }

    public void addPixKeyPublic(JSONObject pixKeyObject){
        JSONArray pixKeysPublic = (JSONArray) this.db.get("pixKeysPublic");
        pixKeysPublic.add(pixKeyObject);
        this.db.replace("pixKeysPublic", pixKeysPublic);
        writeFile(this.db);
    }

    
}
