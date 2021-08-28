package bancoCentral.view;

import bancoCentral.model.Bank;
import bancoCentral.model.Database;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

public class BankSystem {
    public static void main(String[] args) {
        Database db = new Database();

        JSONObject test =  new JSONObject();
        test.put("id", "1");
        test.put("name", "NUBANK");
        test.put("costumers", new JSONArray());

        db.addBank(test);

        Bank bankTest = new Bank("NUBANK", "1");
//        bankTest.addCostumer("Henry Medeiros", "henrymedeiros@hotmail.com", "hhsyehbd637", "09187457482","84996620235", "henrymedeiros@hotmail.com", "241546541", "rua epaminhondas macaxeira", "128", "Campina Grande", "Paríba");
    }
}
