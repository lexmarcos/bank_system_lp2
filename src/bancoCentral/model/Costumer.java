package bancoCentral.model;

import java.util.UUID;

public class Costumer {
    private String id;
    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private Account account;
    private Andress andress;

    public Costumer(String name, String email, String password, String phoneNumber, Account account, Andress andress) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.account = account;
        this.andress = andress;
    }

    public Costumer(String id, String name, String email, String password, String phoneNumber, Account account, Andress andress) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.account = account;
        this.andress = andress;
    }

    public Costumer() {
        this.id = null;
        this.name = null;
        this.email = null;
        this.password = null;
        this.phoneNumber = null;
        this.account = null;
        this.andress = null;
    }

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }


    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }


    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public Account getAccount() {
        return account;
    }


    public void setAccount(Account account) {
        this.account = account;
    }


    public Andress getAndress() {
        return andress;
    }


    public void setAndress(Andress andress) {
        this.andress = andress;
    }
}
