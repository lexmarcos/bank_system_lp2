package bancoCentral.view;

import bancoCentral.model.Database;
import org.json.simple.JSONObject;

public class BankSystem {
    public static void main(String[] args) {
        Database db = new Database();

        JSONObject test =  new JSONObject();
        test.put("id", 1);
        test.put("name", "ITAU");

        JSONObject costumer = new JSONObject();
        costumer.put("name", "Rayane");

        db.removeCostumer("1", costumer);
        JSONObject json = db.findBank(test);
        System.out.println(json.toJSONString());
    }
}
