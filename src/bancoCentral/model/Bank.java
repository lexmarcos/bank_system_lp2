package bancoCentral.model;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.UUID;

public class Bank {
    private String name;
    private String id = UUID.randomUUID().toString();
    private ArrayList<Costumer> costumers = new ArrayList<Costumer>();
    private Database db = new Database();
    public Bank(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public void addCostumer(
            String name,
            String email,
            String password,
            String phoneNumber,
            String pixKey,
            String cep,
            String street,
            String number,
            String city,
            String state
    ){
        JSONObject costumerToAdd = new JSONObject();
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

        db.addCostumer("1", costumerToAdd);
    }
    boolean removeCostumer(String cpf){

        return false;
    }

    public String getName() {
        return name;
    }

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
