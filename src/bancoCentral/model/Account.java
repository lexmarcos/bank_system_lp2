package bancoCentral.model;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.UUID;

public class Account {
    private String id;
    private String bankID;
    private String number;
    private float balance;
    private String pixKey;
    Database db = new Database();

    public Account(String bankID, String number, String pixKey) {
        this.bankID = bankID;
        this.id =  UUID.randomUUID().toString();
        this.balance = 0;
        this.pixKey = pixKey;
    }

    public Response deposit(float amount){
        if(amount <= 0){
            return new Response("O depósito precisa ser de um valor maior do que R$ 0.00", false);
        }
        return new Response("Depósito efetuado com sucesso", true);
    }

    public void receiveTransfer(float amount, String pixKey){
        System.out.println(String.format("Transferencia recebida de %s\nValor: %f", pixKey, amount));
    }

    public Response transfer(float amount, String pixKey){
        if(amount <= 0){
            return new Response("O depósito precisa ser de um valor maior do que R$ 0.00", false);
        }
        JSONObject pixQuery = new JSONObject();
        pixQuery.put("pixKey", pixKey);
        JSONObject pixKeyObject = db.findPixKeyPublic(pixQuery);

        if(pixKeyObject.isEmpty()){ return new Response("Essa chave pix não existe ou está inválida", false); }

        JSONObject bankQuery = new JSONObject();
        bankQuery.put("id", pixKeyObject.get("bankID"));
        JSONObject bankObject = db.findBank(bankQuery);

        if(bankObject.isEmpty()){ return new Response("Banco não encontrado", false); }

        Bank bank = db.generateBankObject(bankObject);

        Account accountToReceiveTransfer = bank.getAccount(pixKey);
        accountToReceiveTransfer.receiveTransfer(amount, pixKey);
        return new Response("Depósito efetuado com sucesso", true);
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public String getPixKey() {
        return pixKey;
    }

    public void setPixKey(String pixKey) {
        this.pixKey = pixKey;
    }
}
