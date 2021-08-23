package bancoCentral.model;

public class Response {
    String message;
    boolean success;

    public Response(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public boolean getSucess() {
        return success;
    }
}
