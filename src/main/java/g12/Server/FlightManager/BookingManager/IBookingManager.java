package g12.Server.FlightManager.BookingManager;

import java.time.LocalDate;
import java.util.List;

import g12.Server.FlightManager.Exceptions.*;

public interface IBookingManager {

	/**
	 * 
	 * @param orig
	 * @param dest
	 * @param cap
	 */
	void addFlight(String orig, String dest, Integer cap);

	Boolean closeDay() throws DiaFechado;

	/**
	 * 
	 * @param user
	 * @param percurso
	 * @param de
	 * @param ate
	 */
	String bookFlight(String user, List<String> percurso, LocalDate de, LocalDate ate) throws VooNaoExistente, ReservaIndisponivel, PercusoNaoDisponivel;

	/**
	 * 
	 * @param bookId
	 * @throws ReservaNaoExiste
	 */
	void removeBooking(String bookId) throws ReservaNaoExiste, VooNaoExistente;

	List<Voo> getAvailableFlights();

}