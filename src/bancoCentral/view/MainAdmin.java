package bancoCentral.view;

import bancoCentral.model.*;
import org.json.simple.JSONObject;

public class MainAdmin {
    public static void main(String[] args) {
        Database db = new Database();

        Bank bankTest = new Bank("NUBANK");
        Bank bankTest2 = new Bank("BANCO DO BRASIL");

        Response response = bankTest.addCostumerPersonal("Henry Medeiros", "henrymedeiros@hotmail.com", "hhsyehbd637", "09187457482","84996620235", "henrymedeiros@hotmail.com", "241546541", "rua epaminhondas macaxeira", "128", "Campina Grande", "Paraíba");
        Response response2 = bankTest2.addCostumerPersonal("Marcosuel Vieira", "marocusl.vieuir@hotmail.com", "hbruh123", "06108851465", "99158291", "marcola@hotmail.com", "59050090", "rua rio norte", "173A", "Natal", "Rio Grande do Norte");

        System.out.println(response.getMessage());
        System.out.println(response2.getMessage());

        bankTest2.getAccount("marcola@hotmail.com", "pixKey");
        bankTest.getAccount("henrymedeiros@hotmail.com", "pixKey");

        JSONObject bankQuery = new JSONObject();
        bankQuery.put("id", bankTest.getId());
        JSONObject bank = db.findOne("banks", bankQuery);
        System.out.println(bank.toJSONString());

    }
}
