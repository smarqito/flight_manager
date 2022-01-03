package g12.Server.FlightManager.UserManager;

import g12.Server.FlightManager.Exceptions.UserIsNotClient;
import g12.Server.FlightManager.Exceptions.UserNaoExistente;

public interface IUserManager {

	/**
	 * Verifica é possível um utilizador fazer o login
	 * @param user Identificador do utilizador
	 * @param pass Palavra-pass do utilizador
	 */
	Boolean checkLogin(String user, String pass) throws UserNaoExistente;

	/**
	 * Verifica se o utilizador existe
	 * @param user Identificador do utilizador
	 */
	Boolean hasUser(String user);

	/**
	 * Adiciona um utilizador
	 * @param user Identificador do utilizador
	 * @param pass Palavra-pass do utilizador
	 */
	void addUser(String user, String pass) throws UserNaoExistente;

	/**
	 * Adiciona uma reserva
	 * @param user Identificador do utilizador
	 * @param idR Identificador da reserva
	 */
	void addReserva(String user, String idR) throws UserIsNotClient, UserNaoExistente;

	/**
	 * Remove uma reserva
	 * @param user Identificador do utilizador
	 * @param idR Identificador da reserva
	 */
	void removeReserva(String user, String idR) throws UserIsNotClient, UserNaoExistente;

}