package bancoCentral.model;

public class CostumerPersonal extends Costumer{
    private String cpf;

    public CostumerPersonal(String name, String email, String password, String phoneNumber, Account account, Andress andress, String cpf) {
        super(name, email, password, phoneNumber, account, andress);
        this.cpf = cpf;
    }

    public String getCpf() {
        return cpf;
    }
}
