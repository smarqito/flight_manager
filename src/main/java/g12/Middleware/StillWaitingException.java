package g12.Middleware;

public class StillWaitingException extends Exception {

    public StillWaitingException() {
    }

    public StillWaitingException(String message) {
        super(message);
    }

}
