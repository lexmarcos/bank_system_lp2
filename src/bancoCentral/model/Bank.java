package bancoCentral.model;

import java.util.ArrayList;
import java.util.UUID;

public class Bank {
    private String name;
    private String id = UUID.randomUUID().toString();
    private ArrayList<Costumer> costumers = new ArrayList<Costumer>();

    public Bank(String name, String id, ArrayList<Costumer> costumers) {
        this.name = name;
        this.id = id;
    }

    void addCostumer(
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
