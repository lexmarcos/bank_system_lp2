package bancoCentral.model;

public class Andress {
    private String cep;
    private String street;
    private String number;
    private String state;

    public Andress(String cep, String street, String number, String state) {
        this.cep = cep;
        this.street = street;
        this.number = number;
        this.state = state;
    }

    public Response verifyCEP(String _cep){
        _cep = _cep.trim();
        if(cep.isEmpty() || cep.isBlank() || cep.length()  != 8){
            return new Response("CEP inválido", false);
        }
        return new Response("CEP válido", true);
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
