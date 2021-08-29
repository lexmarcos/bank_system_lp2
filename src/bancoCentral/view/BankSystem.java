package bancoCentral.view;

import bancoCentral.model.*;

public class BankSystem {
    public static void main(String[] args) {
        Database db = new Database();

        Bank bankTest = new Bank("NUBANK");
        Bank bankTest2 = new Bank("BANCO DO BRASIL");

        Response response = bankTest.addCostumerPersonal("Henry Medeiros", "henrymedeiros@hotmail.com", "hhsyehbd637", "09187457482","84996620235", "henrymedeiros@hotmail.com", "241546541", "rua epaminhondas macaxeira", "128", "Campina Grande", "Paríba");
        Response response2 = bankTest2.addCostumerPersonal("Marcosuel Vieira", "marocusl.vieuir@hotmail.com", "hbruh123", "06108851465", "99158291", "marcola@hotmail.com", "59050090", "rua rio norte", "173A", "Natal", "Rio Grande do Norte");

        System.out.println(response.getMessage());
        System.out.println(response2.getMessage());

        Account marcola = bankTest2.getAccount("marcola@hotmail.com", "pixKey");
        Account henry = bankTest.getAccount("henrymedeiros@hotmail.com", "pixKey");

        Response deposit = henry.deposit(1000.45f);
        Response transfer = henry.transfer(500.40f, marcola.getPixKey(), "pixKey");

        System.out.println(deposit.getMessage());
        System.out.println(transfer.getMessage());
    }
}
