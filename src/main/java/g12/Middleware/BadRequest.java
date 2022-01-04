package g12.Middleware;
/**
 * Vai dar a resposta com erro 400
 */
public class BadRequest extends Exception {

    public BadRequest() {
    }

    public BadRequest(String message) {
        super(message);
    }
    
}
