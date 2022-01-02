package g12.Server.FlightManager.UserManager;

import g12.Server.FlightManager.Exceptions.UserIsNotClient;

public interface IUserManager {

	/**
	 * 
	 * @param user
	 * @param pass
	 */
	Boolean checkLogin(String user, String pass);

	/**
	 * 
	 * @param user
	 */
	Boolean hasUser(String user);

	/**
	 * 
	 * @param user
	 * @param pass
	 */
	void addUser(String user, String pass);

	/**
	 * 
	 * @param user
	 * @param idR
	 */
	void addReserva(String user, String idR) throws UserIsNotClient;

	/**
	 * 
	 * @param user
	 * @param idR
	 */
	void removeReserva(String user, String idR) throws UserIsNotClient;

}