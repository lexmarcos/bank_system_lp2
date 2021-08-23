package bancoCentral.model;

public class CostumerLegal extends Costumer  {
    private String cnpj;

    public CostumerLegal(String name, String email, String password, String phoneNumber, Account account, Andress andress, String cnpj) {
        super(name, email, password, phoneNumber, account, andress);
        this.cnpj = cnpj;
    }

    public String getCnpj() {
        return cnpj;
    }
}
