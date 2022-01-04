package g12.Server.FlightManager.UserManager;

import g12.Middleware.TokenInvalido;
import g12.Server.FlightManager.Exceptions.LoginInvalido;
import g12.Server.FlightManager.Exceptions.UserIsNotClient;
import g12.Server.FlightManager.Exceptions.UserJaExisteException;
import g12.Server.FlightManager.Exceptions.UserNaoExistente;

public interface IUserManager {

	/**
	 * Verifica é possível um utilizador fazer o login
	 * 
	 * @param user Identificador do utilizador
	 * @param pass Palavra-pass do utilizador
	 * @throws LoginInvalido
	 */
	String checkLogin(String user, String pass) throws LoginInvalido;

	/**
	 * Devolve o utilizador
	 * 
	 * @param user Identificador do utilizador
	 */
	User getUser(String user) throws UserNaoExistente;
	/**
	 * Verifica se o utilizador existe
	 * 
	 * @param user Identificador do utilizador
	 */
	Boolean hasUser(String user);

	/**
	 * Adiciona um utilizador
	 * 
	 * @param user Identificador do utilizador
	 * @param pass Palavra-pass do utilizador
	 * @throws UserJaExisteException
	 */
	void addUser(String user, String pass) throws UserJaExisteException;

	/**
	 * Adiciona uma reserva
	 * 
	 * @param user Identificador do utilizador
	 * @param idR  Identificador da reserva
	 */
	void addReserva(String user, String idR) throws UserIsNotClient, UserNaoExistente;

	/**
	 * Remove uma reserva
	 * 
	 * @param user Identificador do utilizador
	 * @param idR  Identificador da reserva
	 */
	void removeReserva(String user, String idR) throws UserIsNotClient, UserNaoExistente;

	/**
	 * 
	 * @param user
	 * @param id
	 * @return
	 * @throws UserNaoExistente
	 * @throws UserIsNotClient
	 */
	Boolean hasReserva(String user, String id) throws UserNaoExistente, UserIsNotClient;

	/**
	 * 
	 * @param token
	 * @return
	 * @throws TokenInvalido
	 */
	String checkToken(String token) throws TokenInvalido;

	/**
	 * 
	 * @param user
	 * @return
	 * @throws UserNaoExistente
	 */
	Boolean isAdmin(String user) throws UserNaoExistente;
	
	/**
	 * 
	 * @param user
	 * @return
	 * @throws UserNaoExistente
	 */
	Boolean isClient(String user) throws UserNaoExistente;
}