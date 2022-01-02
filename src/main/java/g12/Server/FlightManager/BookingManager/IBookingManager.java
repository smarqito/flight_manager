package g12.Server.FlightManager.BookingManager;

import java.time.LocalDate;
import java.util.List;

public interface IBookingManager {

	/**
	 * 
	 * @param orig
	 * @param dest
	 * @param cap
	 */
	void addFlight(String orig, String dest, Integer cap);

	Boolean closeDay();

	/**
	 * 
	 * @param user
	 * @param percurso
	 * @param de
	 * @param ate
	 */
	String bookFlight(String user, List<String> percurso, LocalDate de, LocalDate ate);

	/**
	 * 
	 * @param bookId
	 */
	void removeBooking(String bookId);

	List<InfoVoo> getAvailableFlights();

}