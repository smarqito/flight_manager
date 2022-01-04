package g12.Server.FlightManager;

import java.time.LocalDate;
import java.util.List;

import g12.Middleware.TokenInvalido;
import g12.Server.FlightManager.BookingManager.*;
import g12.Server.FlightManager.Exceptions.LoginInvalido;
import g12.Server.FlightManager.Exceptions.NotAllowed;
import g12.Server.FlightManager.Exceptions.UserIsNotClient;
import g12.Server.FlightManager.Exceptions.UserJaExisteException;
import g12.Server.FlightManager.Exceptions.UserNaoExistente;

public interface IFlightManager {

	/**
	 * 
	 * @param user
	 * @param pass
	 * @throws LoginInvalido
	 */
	String login(String user, String pass) throws LoginInvalido;

	/**
	 * 
	 * @param user
	 * @param pass
	 * @throws UserJaExisteException
	 */
	void registerUser(String user, String pass) throws UserJaExisteException;

	/**
	 * 
	 * @param origem
	 * @param dest
	 * @param cap
	 */
	void registerFlight(String origem, String dest, Integer cap);

	/**
	 * altera o dia de hoje, colocando-o como closed!
	 * 
	 * @param user
	 * @throws UserNaoExistente
	 * @throws NotAllowed
	 */
	Boolean closeDay(String user) throws UserNaoExistente, NotAllowed;

	/**
	 * 
	 * @param user
	 * @param percurso
	 * @param de
	 * @param ate
	 * @throws UserNaoExistente
	 * @throws UserIsNotClient
	 */
	String bookFlight(String user, List<String> percurso, LocalDate de, LocalDate ate) throws UserIsNotClient, UserNaoExistente;

	/**
	 * 
	 * @param user
	 * @param id
	 * @throws UserIsNotClient
	 * @throws UserNaoExistente
	 */
	Boolean cancelBook(String user, String id) throws UserNaoExistente, UserIsNotClient;

	/**
	 * 
	 * @return
	 */
	List<InfoVoo> availableFlights();

	/**
	 * 
	 * @param token
	 * @return
	 * @throws TokenInvalido
	 */
	String verifyToken(String token) throws TokenInvalido;
}