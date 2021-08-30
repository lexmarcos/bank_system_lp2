package bancoCentral.model;

import org.json.simple.JSONObject;

public class Andress implements Displayer {
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
        if(_cep.isEmpty() || _cep.isBlank() || _cep.length()  != 8){
            return new Response("CEP inválido", false);
        }
        return new Response("CEP válido", true);
    }


    @Override
    public JSONObject displayData() {
        JSONObject allDataOfClass = new JSONObject();
        allDataOfClass.put("cep", this.cep);
        allDataOfClass.put("street", this.street);
        allDataOfClass.put("number", this.number);
        allDataOfClass.put("state", this.state);

        System.out.println(allDataOfClass.toJSONString());

        return allDataOfClass;
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
