package g12.Server.FlightManager.UserManager;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class UserManager implements IUserManager {

	private User users;
	private Lock lock = new ReentrantLock();

	/**
	 * 
	 * @param user
	 * @param pass
	 */
	public Boolean checkLogin(String user, String pass) {
		// TODO - implement UserManager.checkLogin
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param user
	 */
	public Boolean hasUser(String user) {
		// TODO - implement UserManager.hasUser
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param user
	 * @param pass
	 */
	public void addUser(String user, String pass) {
		// TODO - implement UserManager.addUser
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param user
	 * @param idR
	 */
	public void addReserva(String user, String idR) {
		// TODO - implement UserManager.addReserva
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param user
	 * @param idR
	 */
	public void removeReserva(String user, String idR) {
		// TODO - implement UserManager.removeReserva
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param user
	 */
	public User getUser(String user) {
		return null;
	}

}