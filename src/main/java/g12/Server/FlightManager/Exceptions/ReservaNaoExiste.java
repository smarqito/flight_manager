package g12.Server.FlightManager.Exceptions;

public class ReservaNaoExiste extends Exception {

    public ReservaNaoExiste() {
        super();
    }

    public ReservaNaoExiste(String msg) {
        super(msg);
    }
}
