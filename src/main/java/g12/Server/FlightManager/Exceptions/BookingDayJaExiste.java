package g12.Server.FlightManager.Exceptions;

public class BookingDayJaExiste extends Exception{
    public BookingDayJaExiste() {
        super();
    }

    public BookingDayJaExiste(String msg) {
        super(msg);
    }
}
