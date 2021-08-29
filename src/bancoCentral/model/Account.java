package bancoCentral.model;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Account {
    private String bankID;
    private String number;
    private float balance;
    private String pixKey;
    private String costumerID;
    private JSONArray historic = new JSONArray();
    Database db = new Database();

    public Account(String bankID, String number, String pixKey) {
        this.bankID = bankID;
        this.balance = 0;
        this.number = number;
        this.pixKey = pixKey;
    }

    public Account(String bankID, String number, String pixKey, JSONArray historic) {
        this.bankID = bankID;
        this.balance = 0;
        this.number = number;
        this.pixKey = pixKey;
        this.historic = historic;
    }

    public Account(String bankID, String number, float balance, String pixKey, JSONArray historic) {
        this.bankID = bankID;
        this.balance = balance;
        this.number = number;
        this.pixKey = pixKey;
        this.historic = historic;
    }


    private void addToHistoric(float amount, String pixKey, String number, String BankID, String type){
        JSONObject transaction = new JSONObject();

        transaction.put("amount", amount);
//        transaction.put("to", to);
        transaction.put("BankID", BankID);
//        transaction.put("bankName", bankName);
        transaction.put("type", type);
        transaction.put("number", number);

        transaction.put("pixKey", pixKey);

        int timestamp = LocalDateTime.now().getNano();
        transaction.put("timestamp", timestamp);
        this.historic.add(transaction);
    }


    private JSONObject generateAccountObject(){
        JSONObject accountObject = new JSONObject();

        accountObject.put("bankID", this.bankID);
        accountObject.put("balance", this.balance);
        accountObject.put("number", this.number);
        accountObject.put("pixKey", this.pixKey);
        accountObject.put("historic", this.historic);

        return accountObject;
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
        System.out.println("Realizando Deposito\n");
        if(amount <= 0){
            return new Response("O depósito precisa ser de um valor maior do que R$ 0.00", false);
        }
        this.balance += amount;
        addToHistoricSelfTransaction(amount, "Depósito");
        this.db.updateAccount(this.bankID, this.costumerID, generateAccountObject());
        return new Response("Depósito efetuado com sucesso", true);
    }


    public void receiveTransfer(float amount, String pixKey, String bankID, String number, String type){
        this.balance += amount;
        if(Objects.equals(type, "pixKey")){
            addToHistoric(amount, pixKey, number, bankID, "Transferência recebida");
        }else{
            addToHistoric(amount, pixKey, number, bankID, "Transferência PIX recebida");
        }
        this.db.updateAccount(this.bankID, this.costumerID, generateAccountObject());
        System.out.println(String.format("Transferencia recebida de %s\nValor: %f", pixKey, amount));
    }


    public Response transfer(float amount, String keyToSearch, String type){
        System.out.println("Realizando transferencia\n");
        if(amount <= 0){
            return new Response("O depósito precisa ser de um valor maior do que R$ 0.00", false);
        }

        JSONObject query = new JSONObject();
        query.put(type, keyToSearch);
        JSONObject accountInfo = this.db.findPublicAccountInfos(query);

        if(accountInfo.isEmpty()){ return new Response(String.format("%s inválido!", type), false); }

        JSONObject bankQuery = new JSONObject();
        bankQuery.put("id", accountInfo.get("bankID"));
        JSONObject bankObject = this.db.findBank(bankQuery);

        if(bankObject.isEmpty()){ return new Response("Banco não encontrado", false); }

        Bank bank = this.db.generateBankObject(bankObject);
        Account accountToReceiveTransfer = bank.getAccount(keyToSearch, type);
        accountToReceiveTransfer.receiveTransfer(amount, this.pixKey, this.bankID, this.number, type);

        if(Objects.equals(type, "number")){
            addToHistoric(amount, "", accountToReceiveTransfer.getNumber(), accountToReceiveTransfer.getBankID(), "Transferência enviada");
        }else{
            addToHistoric(amount, accountToReceiveTransfer.getPixKey(), accountToReceiveTransfer.getNumber(), accountToReceiveTransfer.getBankID(), "Transferência PIX enviada");
        }

        this.balance -= amount;
        this.db.updateAccount(this.bankID, this.costumerID, generateAccountObject());
        return new Response("Depósito efetuado com sucesso", true);
    }


    public Response withdraw(float amount){
        if(amount <= 0){
            return new Response("O saque precisa ser de um valor maior do que R$ 0.00", false);
        }
        this.balance -= amount;
        addToHistoricSelfTransaction(amount, "Saque");

        this.db.updateAccount(this.bankID, this.costumerID, generateAccountObject());
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
        acountInfoQuery.put("bankID", this.bankID);
        JSONObject accountInfo = this.db.findPublicAccountInfos(acountInfoQuery);

        query.put("id", collectID);
        query.put("amount", amount);
        query.put("pixKey", this.pixKey);
        query.put("number", this.number);
        query.put("bankID", this.bankID);
        query.put("isPaid", false);
        query.put("collector", accountInfo.get("name").toString());

        this.db.addCollect(query);
        return new Response(String.format("Cobrança gerada com sucesso, chave da cobrança: %s", collectID), true);
    }


    private void receiveCollect(String collectID){
        JSONObject query = new JSONObject();
        query.put("id", collectID);
        JSONObject collectFinded = this.db.findCollect(query);
        JSONObject payerInfo = (JSONObject) collectFinded.get("payerInfo");

        float collectAmount = Float.parseFloat(collectFinded.get("amount").toString());
        this.balance += collectAmount;

        this.addToHistoric(collectAmount,  payerInfo.get("pixKey").toString(), payerInfo.get("number").toString(),  payerInfo.get("bankID").toString(), "Recebimento de cobraća");
        this.db.updateAccount(this.bankID, this.costumerID, generateAccountObject());
    }


    public Response payCollect(String collectID){
        JSONObject query = new JSONObject();
        query.put("id", collectID);
        JSONObject collectFinded = this.db.findCollect(query);

        if(collectFinded.isEmpty()){
            return new Response("Nenhuma cobranća com essa chave foi encontrada", false);
        }else if(Boolean.parseBoolean(collectFinded.get("isPaid").toString())){
            return new Response("Essa cobranća já está paga", false);
        }

        float collectAmount = Float.parseFloat(collectFinded.get("amount").toString());
        if(this.balance < collectAmount){
            return new Response("Você não tem saldo o suficiente para pagar essa cobranća", false);
        }

        this.balance -= collectAmount;

        JSONObject bankQuery = new JSONObject();
        bankQuery.put("id", collectFinded.get("bankID").toString());
        JSONObject bankObject = this.db.findBank(bankQuery);
        Bank bank = this.db.generateBankObject(bankObject);

        JSONObject acountInfoQuery = new JSONObject();
        acountInfoQuery.put("number", this.number);
        acountInfoQuery.put("bankID", this.bankID);
        JSONObject accountInfo = this.db.findPublicAccountInfos(acountInfoQuery);

        JSONObject payerInfo = new JSONObject();
        payerInfo.put("name", accountInfo.get("name").toString());
        payerInfo.put("pixKey", this.pixKey);
        payerInfo.put("number", this.number);
        payerInfo.put("bankID", this.bankID);

        collectFinded.put("isPaid", true);
        collectFinded.put("payerInfo", payerInfo);

        addToHistoric(collectAmount, collectFinded.get("pixKey").toString(), collectFinded.get("number").toString(), collectFinded.get("bankID").toString(), "Pagamento de cobraća");
        this.db.updateCollect(collectFinded);
        this.db.updateAccount(this.bankID, this.costumerID, generateAccountObject());

        Account accountToReceiveTransfer = bank.getAccount(collectFinded.get("number").toString(), "number");
        System.out.println(accountToReceiveTransfer.getBalance());
        accountToReceiveTransfer.receiveCollect(collectID);
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


    public String getBankID() {
        return bankID;
    }


    public void setBankID(String bankID) {
        this.bankID = bankID;
    }


    public String getCostumerID() {
        return costumerID;
    }


    public void setCostumerID(String costumerID) {
        this.costumerID = costumerID;
    }


    public JSONArray getHistoric() {
        return historic;
    }


    public void setHistoric(JSONArray historic) {
        this.historic = historic;
    }
}
