package bancoCentral.view;

import bancoCentral.model.Database;
import org.json.simple.JSONObject;

public class BankSystem {
    public static void main(String[] args) {
        Database db = new Database();

        JSONObject test =  new JSONObject();
        test.put("id", 3);
        test.put("name", "Banco do brasil");

        JSONObject costumer = new JSONObject();
        costumer.put("name", "Rayane");

        db.addCostumer("1", costumer);
        JSONObject json = db.findCostumerByBank("1", costumer);
        System.out.println(json.toJSONString());
    }
}
