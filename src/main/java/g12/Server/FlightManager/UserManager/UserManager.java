package g12.Server.FlightManager.UserManager;

import g12.Server.FlightManager.Exceptions.UserIsNotClient;
import g12.Server.FlightManager.Exceptions.UserNaoExistente;

import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class UserManager implements IUserManager {

	private Map<String, User> users;
	private Lock lock = new ReentrantLock();

	/**
	 * Verifica é possível um utilizador fazer o login
	 * @param user Identificador do utilizador
	 * @param pass Palavra-pass do utilizador
	 */
	public Boolean checkLogin(String user, String pass) {
		return getUser(user).isPassValid(pass);
	}

	/**
	 * Verifica se o utilizador existe
	 * @param user Identificador do utilizador
	 */
	public Boolean hasUser(String user) {
		return this.users.containsKey(user);
	}

	/**
	 * Adiciona um utilizador
	 * @param user Identificador do utilizador
	 * @param pass Palavra-pass do utilizador
	 */
	public void addUser(String user, String pass) {
		this.users.put(user, getUser(user));
		getUser(user).setPass(pass);
	}

	/**
	 * Adiciona uma reserva
	 * @param user Identificador do utilizador
	 * @param idR Identificador da reserva
	 */
	public void addReserva(String user, String idR) throws UserIsNotClient {
		if(!(getUser(user) instanceof Client)) throw new UserIsNotClient();
		((Client) getUser(user)).addReserva(idR);
	}

	/**
	 * Remove uma reserva
	 * @param user Identificador do utilizador
	 * @param idR Identificador da reserva
	 */
	public void removeReserva(String user, String idR) throws UserIsNotClient{
		if(!(getUser(user) instanceof Client)) throw new UserIsNotClient();
		((Client) getUser(user)).removeReserva(idR);
	}

	/**
	 * Devolve o utilizador
	 * @param user Identificador do utilizador
	 */
	public User getUser(String user) {
		return this.users.get(user).clone();
	}

}