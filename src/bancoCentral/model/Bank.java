package bancoCentral.model;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class Bank {
    private String name;
    private String id;
    private ArrayList<Costumer> costumers = new ArrayList<Costumer>();
    private int numberOfCostumers;
    private final Database db = new Database();


    public Bank(String name) {
        this.name = name;
        this.id = UUID.randomUUID().toString();
        this.numberOfCostumers = 0;
    }

    public Bank(String name, String id) {
        this.name = name;
        this.id = id;
        this.numberOfCostumers = 0;
    }


    public Bank(String name, String id, JSONArray costumers) {
        this.numberOfCostumers = 0;
        this.name = name;
        this.id = id;
        for (Object costumer : costumers) {
            JSONObject currentCostumer = (JSONObject) costumer;
            boolean hasCPF = currentCostumer.containsKey("cpf");
            if (hasCPF) {
                addCostumerPersonal(db.generateCostumerObject(currentCostumer));
                continue;
            }
            addCostumerLegal(db.generateCostumerObject(currentCostumer));
        }
    }

    public void addCostumerPersonal(Costumer costumer){
        costumers.add(costumer);
    }


    public void addCostumerLegal(Costumer costumer){
        costumers.add(costumer);
    }


    private JSONObject generateCostumerObject(
            String name, String email,
            String password,
            String phoneNumber, String pixKey,
            String cep, String street,
            String number, String city,
            String state,
            Account account
    ){
        JSONObject costumerToAdd = new JSONObject();
        JSONObject accountObject = new JSONObject();
        costumerToAdd.put("name", name);
        costumerToAdd.put("email", email);
        costumerToAdd.put("password", password);
        costumerToAdd.put("phoneNumber", phoneNumber);
        costumerToAdd.put("pixKey", pixKey);
        costumerToAdd.put("cep", cep);
        costumerToAdd.put("street", street);
        costumerToAdd.put("number", number);
        costumerToAdd.put("city", city);
        costumerToAdd.put("state", state);
        accountObject.put("balance", account.getBalance());
        accountObject.put("number", account.getNumber());
        accountObject.put("bankID", account.getBankID());
        accountObject.put("pixKey", account.getPixKey());
        accountObject.put("historic", new JSONArray());
        costumerToAdd.put("account", accountObject);
        return costumerToAdd;
    }


    private JSONObject generatePublicInfo(String name, String pixKey, String number){
        JSONObject publicInfo = new JSONObject();
        publicInfo.put("name", name);
        publicInfo.put("pixKey", pixKey);
        publicInfo.put("number", number);
        publicInfo.put("bankID", this.id);

        return publicInfo;
    }


    public Response addCostumerPersonal(
            String name, String email,
            String password, String cpf,
            String phoneNumber, String pixKey,
            String cep, String street,
            String number, String city,
            String state
    ){
        this.numberOfCostumers += 1;
        String accountNumber = String.format("%d", 10000 + numberOfCostumers);

        Account account = new Account(this.id, accountNumber, pixKey);
        Andress andress = new Andress(cep, street, number, state);

        CostumerPersonal personal = new CostumerPersonal(name, email, password, cpf, account, phoneNumber, andress);
        account.setCostumerID(personal.getId());
        personal.setAccount(account);

        if(!personal.verifyCPF(cpf)){
            return new Response("CPF inválido", false);
        }

        costumers.add(personal);
        JSONObject costumerToAdd = generateCostumerObject(name, email, password, phoneNumber, pixKey, cep, street, number, city, state, account);
        costumerToAdd.put("cpf", cpf);
        costumerToAdd.put("id", personal.getId());
        db.addPublicAccountInfo(generatePublicInfo(name, pixKey, number));
        db.addCostumer(this.id, costumerToAdd);


        return new Response("Usuário adicionado com sucesso", true);
    }


    public Response addCostumerLegal(
            String name, String email,
            String password, String cnpj,
            String phoneNumber, String pixKey,
            String cep, String street,
            String number, String city,
            String state
    ){
        this.numberOfCostumers += 1;
        String accountNumber = String.format("%d", 10000 + numberOfCostumers);
        Andress andress = new Andress(cep, street, number, state);
        Account account = new Account(this.id, accountNumber, pixKey);
        CostumerLegal costumer = new CostumerLegal(name, email, password, cnpj, account, phoneNumber, andress);
        account.setCostumerID(costumer.getId());
        costumer.setAccount(account);

        if(costumer.verifyCNPJ(cnpj)){
            return new Response("CPF inválido", false);
        };

        costumers.add(costumer);
        JSONObject costumerToAdd = generateCostumerObject(name, email, password, phoneNumber, pixKey, cep, street, number, city, state, account);
        costumerToAdd.put("cnpj", cnpj);
        costumerToAdd.put("id", costumer.getId());

        db.addPublicAccountInfo(generatePublicInfo(name, pixKey, number));
        db.addCostumer(this.id, costumerToAdd);

        return new Response("Usuário adicionado com sucesso", true);
    }


    public Response removeCostumerByCPF(String cpf){
        JSONObject query = new JSONObject();
        query.put("cpf", cpf);
        return removeCostumer(query);
    }


    public Response removeCostumerByQuery(JSONObject query){
        return removeCostumer(query);
    }


    private Response removeCostumer(JSONObject query) {
        Response response = db.removeCostumer(this.id, query);
        if(!response.success){
            return response;
        }

        Costumer costumerToRemove = db.generateCostumerObject(db.findCostumerByBank(this.id, query));
        costumers.remove(costumerToRemove);
        return response;
    }


    public Costumer findCostumer(JSONObject query){
        JSONObject costumer =  db.findCostumerByBank(this.id, query);
        System.out.println(costumer.toJSONString());
        return db.generateCostumerObject(costumer);
    }


    public Account getAccount(String pixOrNumber, String key){
        for(Costumer c: costumers){
            if(Objects.equals(c.getAccount().getPixKey(), pixOrNumber)){
                System.out.println(c.getName());
                return c.getAccount();
            }
        }
        return new Account("NULL","NULL","NULL");
    };


    public String getName() { return name; }


    public void setName(String name) {
        this.name = name;
    }


    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }


    public ArrayList<Costumer> getCostumers() {
        return costumers;
    }


    public void setCostumers(ArrayList<Costumer> costumers) {
        this.costumers = costumers;
    }
}
