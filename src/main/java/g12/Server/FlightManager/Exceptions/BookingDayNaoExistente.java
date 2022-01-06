package g12.Server.FlightManager.Exceptions;

public class BookingDayNaoExistente extends Exception {

    public BookingDayNaoExistente() {
        super();
    }

    public BookingDayNaoExistente(String msg) {
        super(msg);
    }
}
