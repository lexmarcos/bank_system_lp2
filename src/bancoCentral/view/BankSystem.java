package bancoCentral.view;

import bancoCentral.model.*;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

import java.util.ArrayList;

public class BankSystem {
    public static void main(String[] args) {
        Database db = new Database();

        JSONObject test =  new JSONObject();
        test.put("id", "1");
        test.put("name", "NUBANK");
        test.put("costumers", new JSONArray());

        JSONObject test2 =  new JSONObject();
        test2.put("id", "2");
        test2.put("name", "BANCO DO BRASIL");
        test2.put("costumers", new JSONArray());

        db.addBank(test);
        db.addBank(test2);
        Bank bankTest = new Bank("NUBANK", "1");
        Bank bankTest2 = new Bank("BANCO DO BRASIL", "2");
        Response response = bankTest.addCostumerPersonal("Henry Medeiros", "henrymedeiros@hotmail.com", "hhsyehbd637", "09187457482","84996620235", "henrymedeiros@hotmail.com", "241546541", "rua epaminhondas macaxeira", "128", "Campina Grande", "Paríba");
        Response response2 = bankTest2.addCostumerPersonal("Marcosuel Vieira", "marocusl.vieuir@hotmail.com", "hbruh123", "06108851465", "99158291", "marcola@hotmail.com", "59050090", "rua rio norte", "173A", "Natal", "Rio Grande do Norte");

        Account marcola = bankTest2.getAccount("marcola@hotmail.com", "pixKey");
        Account henry = bankTest.getAccount("henrymedeiros@hotmail.com", "pixKey");

        Response deposit = henry.deposit(1000.45f);
        Response transfer = henry.transfer(500.40f, marcola.getPixKey(), "pixKey");

        System.out.println(db.getDb().toJSONString());
    }
}
