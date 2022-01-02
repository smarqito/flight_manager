package g12.Server.FlightManager.Exceptions;

public class UserNaoExistente extends Exception {

    public UserNaoExistente() {
        super();
    }

    public UserNaoExistente(String msg) {
        super(msg);
    }
}
