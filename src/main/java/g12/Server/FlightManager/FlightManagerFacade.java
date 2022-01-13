package g12.Server.FlightManager;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import g12.Middleware.TokenInvalido;
import g12.Server.FlightManager.BookingManager.*;
import g12.Server.FlightManager.Exceptions.*;
import g12.Server.FlightManager.UserManager.*;
import g12.Server.Persistence.FlightData;

public class FlightManagerFacade implements IFlightManager {

	private IBookingManager booking;
	private IUserManager users;

	public FlightManagerFacade() {
		booking = new BookingManager();
		users = new UserManager();
	}

	public static IFlightManager getState() {
		try {
			return FlightData.getState();
		} catch (ClassNotFoundException | IOException e) {
			return new FlightManagerFacade();
		}
	}

	public boolean saveState() {
		try {
			FlightData.saveState(this);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

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
	public void registerFlight(String origem, String dest, Integer cap) {
		booking.addFlight(origem, dest, cap);
	}

	/**
	 * 
	 * @param user
	 * @throws UserNaoExistente
	 * @throws NotAllowed
	 * @throws DiaFechado
	 */
	public Boolean closeDay(String user) throws UserNaoExistente, NotAllowed, DiaFechado, BookingDayNaoExistente {
		if (users.isAdmin(user)) {
			return booking.closeDay();
		}
		throw new NotAllowed("O utilizador " + user + "nao tem permissoes");
	}

	/**
	 * 
	 * @param user
	 * @param percurso
	 * @param de
	 * @param ate
	 * @throws UserNaoExistente
	 * @throws UserIsNotClient
	 */
	public String bookFlight(String user, List<String> percurso, LocalDate de, LocalDate ate)
			throws UserIsNotClient, UserNaoExistente, VooNaoExistente, ReservaIndisponivel, PercusoNaoDisponivel,
			BookingDayNaoExistente, BookingDayJaExiste, DiaFechado, VooJaExiste {
		if (users.hasUser(user)) {
			User u = users.getUser(user);
			u.lock.lock();
			String bookId;
			try {
				bookId = booking.bookFlight(user, percurso, de, ate);
				users.addReserva(user, bookId);
			} finally {
				u.lock.unlock();
			}

			return bookId;
		}
		throw new UserNaoExistente(user);
	}

	/**
	 * 
	 * @param user
	 * @param id
	 * @throws UserIsNotClient
	 * @throws UserNaoExistente
	 * @throws ReservaNaoExiste
	 */
	public Boolean cancelBook(String user, String id)
			throws UserNaoExistente, UserIsNotClient, ReservaNaoExiste, VooNaoExistente, BookingDayNaoExistente {
		if (this.users.isClient(user)) {
			User u = users.getUser(user);
			try {
				u.lock.lock();
				Client c = (Client) u;
				if (c.hasReserva(id)) {
					this.booking.removeBooking(id);
					c.removeReserva(id);
					return true;
				}
			} finally {
				u.lock.unlock();
			}
		}
		return false;
	}

	public List<Voo> availableFlights() {
		return this.booking.getAvailableFlights();
	}

	public List<List<String>> getFlightList(String origem, String destino){
		return this.booking.getFlightList(origem, destino);
	}

	@Override
	public String verifyToken(String token) throws TokenInvalido {
		return this.users.checkToken(token);
	}

}