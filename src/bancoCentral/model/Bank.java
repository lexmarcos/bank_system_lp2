package bancoCentral.model;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.UUID;

public class Bank {
    private String name;
    private String id = UUID.randomUUID().toString();
    private ArrayList<Costumer> costumers;
    private int numberOfCostumers;
    private final Database db = new Database();


    public Bank(String name, String id) {
        this.name = name;
        this.id = id;
        this.numberOfCostumers = 0;
    }


    public Bank(String name, String id, Object[] costumers) {
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
        if(personal.verifyCPF(cpf)){
            return new Response("CPF inválido", false);
        };

        costumers.add(personal);
        JSONObject costumerToAdd = new JSONObject();
        costumerToAdd.put("name", name);
        costumerToAdd.put("email", email);
        costumerToAdd.put("password", password);
        costumerToAdd.put("cpf", cpf);
        costumerToAdd.put("phoneNumber", phoneNumber);
        costumerToAdd.put("pixKey", pixKey);
        costumerToAdd.put("cep", cep);
        costumerToAdd.put("street", street);
        costumerToAdd.put("number", number);
        costumerToAdd.put("city", city);
        costumerToAdd.put("state", state);

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
        Account account = new Account(this.id, accountNumber, pixKey);
        Andress andress = new Andress(cep, street, number, state);

        CostumerLegal costumer = new CostumerLegal(name, email, password, cnpj, account, phoneNumber, andress);
        if(costumer.verifyCNPJ(cnpj)){
            return new Response("CPF inválido", false);
        };

        costumers.add(costumer);
        JSONObject costumerToAdd = new JSONObject();
        costumerToAdd.put("name", name);
        costumerToAdd.put("email", email);
        costumerToAdd.put("password", password);
        costumerToAdd.put("cpf", cnpj);
        costumerToAdd.put("phoneNumber", phoneNumber);
        costumerToAdd.put("pixKey", pixKey);
        costumerToAdd.put("cep", cep);
        costumerToAdd.put("street", street);
        costumerToAdd.put("number", number);
        costumerToAdd.put("city", city);
        costumerToAdd.put("state", state);

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
        return db.generateCostumerObject(db.findCostumerByBank(this.id, query));
    }


    public Account getAccount(String pixOrNumber, String key){
        JSONObject pixQuery = new JSONObject();
        pixQuery.put(key, pixOrNumber);
        JSONObject pixKeyObject = db.findPublicAccountInfos(pixQuery);
        if(pixKeyObject.isEmpty()){ return new Account("NULL", "NULL", "NULL");}

        Costumer costumerToReceiveTransfer = findCostumer(new JSONObject(pixQuery));
        return costumerToReceiveTransfer.getAccount();
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
