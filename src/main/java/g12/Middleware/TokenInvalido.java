package g12.Middleware;

public class TokenInvalido extends Exception {

    public TokenInvalido() {
    }

    public TokenInvalido(String message) {
        super(message);
    }
    
}
