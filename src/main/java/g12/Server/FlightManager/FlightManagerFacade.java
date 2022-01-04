package g12.Server.FlightManager;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import g12.Server.FlightManager.BookingManager.*;
import g12.Server.FlightManager.Exceptions.LoginInvalido;
import g12.Server.FlightManager.Exceptions.UserJaExisteException;
import g12.Server.FlightManager.UserManager.*;

public class FlightManagerFacade implements IFlightManager {

	private IBookingManager booking;
	private IUserManager users;
	private Lock lock = new ReentrantLock();

	/**
	 * 
	 * @param user
	 * @param pass
	 * @throws LoginInvalido
	 */
	public String login(String user, String pass) throws LoginInvalido {
		return users.checkLogin(user, pass);
	}

	/**
	 * 
	 * @param user
	 * @param pass
	 * @throws UserJaExisteException
	 */
	public void registerUser(String user, String pass) throws UserJaExisteException {
		users.addUser(user, pass);
	}

	/**
	 * 
	 * @param user
	 * @param origem
	 * @param dest
	 * @param cap
	 */
	public void registerFlight(String user, String origem, String dest, Integer cap) {
		// TODO - implement FlightManagerFacade.registerFlight
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param user
	 */
	public Boolean closeDay(String user) {
		// TODO - implement FlightManagerFacade.closeDay
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param user
	 * @param percurso
	 * @param de
	 * @param ate
	 */
	public String bookFlight(String user, List<String> percurso, LocalDate de, LocalDate ate) {
		// TODO - implement FlightManagerFacade.bookFlight
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param user
	 * @param id
	 */
	public Boolean cancelBook(String user, Integer id) {
		// TODO - implement FlightManagerFacade.cancelBook
		throw new UnsupportedOperationException();
	}

	public List<InfoVoo> availableFlights() {
		// TODO - implement FlightManagerFacade.availableFlights
		throw new UnsupportedOperationException();
	}

	@Override
	public Boolean verifyToken(String token) {
		// TODO Auto-generated method stub
		return null;
	}

}