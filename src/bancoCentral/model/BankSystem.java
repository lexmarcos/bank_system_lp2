package bancoCentral.model;

import bancoCentral.model.*;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class BankSystem {
    Database db = new Database();
    ArrayList<Bank> banks = new ArrayList<Bank>();
    public void addBank(String name){
        banks.add(new Bank("String name"));
        System.out.println("Banco adicionado com sucesso");
    }


    public void removeBank(String bankID){
        JSONObject query = new JSONObject();
        query.put("id", bankID);
        JSONObject bankObject = db.findBank(query);

        for(Bank b: this.banks){
            if(b.getId() != bankID) continue;
            banks.remove(b);
            db.removeBank(bankID);
        }

        System.out.println("Banco adicionado com sucesso");
    }
}
