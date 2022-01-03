package g12.Middleware;

public class BadRequest extends Exception {

    public BadRequest() {
    }

    public BadRequest(String message) {
        super(message);
    }
    
}
