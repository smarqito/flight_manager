package g12.Server.FlightManager.UserManager;

import g12.Server.FlightManager.Exceptions.UserIsNotClient;
import g12.Server.FlightManager.Exceptions.UserNaoExistente;

import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class UserManager implements IUserManager {

	private Map<String, User> users;
	private Lock lockUM = new ReentrantLock();

	public Boolean checkLogin(String user, String pass) throws UserNaoExistente {
		lockUM.lock();
		try {
			return getUser(user).isPassValid(pass);
		}
		finally {
			lockUM.unlock();
		}
	}

	public Boolean hasUser(String user) {
		return this.users.containsKey(user);
	}

	public void addUser(String user, String pass) throws UserNaoExistente {
		lockUM.lock();
		try {
			this.users.put(user, getUser(user));
			getUser(user).setPass(pass);
		}
		finally {
			lockUM.unlock();
		}
	}

	public void addReserva(String user, String idR) throws UserIsNotClient, UserNaoExistente {
		lockUM.lock();
		try {
			if (!(getUser(user) instanceof Client)) throw new UserIsNotClient();
			((Client) getUser(user)).addReserva(idR);
		}
		finally {
			lockUM.unlock();
		}
	}

	public void removeReserva(String user, String idR) throws UserIsNotClient, UserNaoExistente {
		lockUM.lock();
		try {
			if (!(getUser(user) instanceof Client)) throw new UserIsNotClient();
			((Client) getUser(user)).removeReserva(idR);
		}
		finally {
			lockUM.unlock();
		}
	}

	/**
	 * Devolve o utilizador
	 * @param user Identificador do utilizador
	 */
	public User getUser(String user) throws UserNaoExistente {
		lockUM.lock();
		try{
			if(!this.users.containsKey(user)) throw new UserNaoExistente("Utilizador "+user+" não existe.");
			return this.users.get(user).clone();
		}
		finally {
			lockUM.unlock();
		}
	}

}