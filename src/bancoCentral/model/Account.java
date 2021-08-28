package bancoCentral.model;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class Account {
    private String id;
    private String bankID;
    private String number;
    private float balance;
    private String pixKey;
    private JSONArray historic;
    Database db = new Database();

    public Account(String bankID, String number, String pixKey) {
        this.bankID = bankID;
        this.id =  UUID.randomUUID().toString();
        this.balance = 0;
        this.pixKey = pixKey;
    }


    private void addToHistoric(float amount, String pixKey, String number, String BankID, String type){
        JSONObject transaction = new JSONObject();

        transaction.put("amount", amount);
//        transaction.put("to", to);
        transaction.put("BankID", BankID);
//        transaction.put("bankName", bankName);
        transaction.put("type", type);
        transaction.put("number", number);

        if(pixKey.isEmpty()){
            transaction.put("pixKey", pixKey);
        }

        int timestamp = LocalDateTime.now().getNano();
        transaction.put("timestamp", timestamp);
        this.historic.add(transaction);
    }


    private void addToHistoricSelfTransaction(float amount, String type){
        JSONObject transaction = new JSONObject();

        transaction.put("amount", amount);
        transaction.put("type", type);

        int timestamp = LocalDateTime.now().getNano();
        transaction.put("timestamp", timestamp);

        this.historic.add(transaction);
    }


    public Response deposit(float amount){
        if(amount <= 0){
            return new Response("O depósito precisa ser de um valor maior do que R$ 0.00", false);
        }
        this.balance += amount;
        addToHistoricSelfTransaction(amount, "Depósito");
        return new Response("Depósito efetuado com sucesso", true);
    }


    public void receiveTransfer(float amount, String pixKey, String bankID, String number){
        this.balance += amount;
        if(pixKey.isEmpty()){
            addToHistoric(amount, pixKey, number, bankID, "Transferência recebida");
        }else{
            addToHistoric(amount, pixKey, number, bankID, "Transferência PIX recebida");
        }
        System.out.println(String.format("Transferencia recebida de %s\nValor: %f", pixKey, amount));
    }


    public Response transfer(float amount, String keyToSearch, String type){
        if(amount <= 0){
            return new Response("O depósito precisa ser de um valor maior do que R$ 0.00", false);
        }

        JSONObject query = new JSONObject();
        query.put(type, keyToSearch);
        JSONObject accountInfo = db.findPublicAccountInfos(query);

        if(accountInfo.isEmpty()){ return new Response(String.format("%s inválido!", type), false); }

        JSONObject bankQuery = new JSONObject();
        bankQuery.put("id", accountInfo.get("bankID"));
        JSONObject bankObject = db.findBank(bankQuery);

        if(bankObject.isEmpty()){ return new Response("Banco não encontrado", false); }

        Bank bank = db.generateBankObject(bankObject);

        Account accountToReceiveTransfer = bank.getAccount(keyToSearch, type);
        accountToReceiveTransfer.receiveTransfer(amount, keyToSearch, this.bankID, this.number);
        if(Objects.equals(type, "number")){
            addToHistoric(amount, "", accountToReceiveTransfer.number, accountToReceiveTransfer.bankID, "Transferência enviada");
        }else{
            addToHistoric(amount, accountToReceiveTransfer.pixKey, accountToReceiveTransfer.number, accountToReceiveTransfer.bankID, "Transferência PIX enviada");
        }

        return new Response("Depósito efetuado com sucesso", true);
    }


    public Response withdraw(float amount){
        if(amount <= 0){
            return new Response("O saque precisa ser de um valor maior do que R$ 0.00", false);
        }
        this.balance -= amount;
        addToHistoricSelfTransaction(amount, "Saque");
        return new Response("Depósito efetuado com sucesso", true);
    }


    public Response collect(float amount){
        if(amount <= 0){
            return new Response("A cobrança precisa ser de um valor maior do que R$ 0.00", false);
        }
        String collectID = UUID.randomUUID().toString();
        JSONObject query = new JSONObject();
        JSONObject acountInfoQuery = new JSONObject();
        acountInfoQuery.put("number", this.number);
        JSONObject accountInfo = db.findPublicAccountInfos(acountInfoQuery);
        query.put("id", collectID);
        query.put("amount", amount);
        query.put("pix", this.pixKey);
        query.put("number", this.number);
        query.put("bankID", this.bankID);
        query.put("collector", accountInfo.get("costumerName").toString());

        db.addCollect(query);
        return new Response(String.format("Cobrança gerada com sucesso, chave da cobrança: %s", collectID), true);
    }


    public Response payCollect(String collectID){
        JSONObject query = new JSONObject();
        JSONObject collectFinded = db.findCollect(query);
        if(collectFinded.isEmpty()){
            return new Response("Nenhuma cobranća com essa chave foi encontrada", false);
        }

        float collectAmount = (float) collectFinded.get("amount");
        if(this.balance < collectAmount){
            return new Response("Você não tem saldo o suficiente para pagar essa cobranća", false);
        }

        this.balance -= collectAmount;

        return new Response("Pagamento realizado com sucesso!", true);
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
