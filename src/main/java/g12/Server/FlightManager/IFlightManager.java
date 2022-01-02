package g12.Server.FlightManager;

import java.time.LocalDate;
import java.util.List;

import g12.Server.FlightManager.BookingManager.*;

public interface IFlightManager {

	/**
	 * 
	 * @param user
	 * @param pass
	 */
	Boolean login(String user, String pass);

	/**
	 * 
	 * @param user
	 * @param pass
	 */
	Boolean registerUser(String user, String pass);

	/**
	 * 
	 * @param user
	 * @param origem
	 * @param dest
	 * @param cap
	 */
	void registerFlight(String user, String origem, String dest, Integer cap);

	/**
	 * altera o dia de hoje, colocando-o como closed!
	 * @param user
	 */
	Boolean closeDay(String user);

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
	 * @param user
	 * @param id
	 */
	Boolean cancelBook(String user, Integer id);

	List<InfoVoo> availableFlights();

}