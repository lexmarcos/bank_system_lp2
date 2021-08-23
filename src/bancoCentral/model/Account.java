package bancoCentral.model;

public class Account {
    private String number;
    private float balance;
    private String pixKey;

    public Account(String number, float balance, String pixKey) {
        this.number = number;
        this.balance = balance;
        this.pixKey = pixKey;
    }

    public Response deposit(float amount){
        if(amount <= 0){
            return new Response("O depósito precisa ser de um valor maior do que R$ 0.00", false);
        }
        return new Response("Depósito efetuado com sucesso", true);
    }

    public Response transfer(float amount, String pixKey){
        if(amount <= 0){
            return new Response("O depósito precisa ser de um valor maior do que R$ 0.00", false);
        }
//        TODO adicionar a funćão de busca
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
