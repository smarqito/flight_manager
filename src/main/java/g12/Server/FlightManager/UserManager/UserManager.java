package g12.Server.FlightManager.UserManager;

import g12.Server.FlightManager.Exceptions.UserIsNotClient;
import g12.Server.FlightManager.Exceptions.UserNaoExistente;

import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class UserManager implements IUserManager {

	private Map<String, User> users;
	private Lock lock = new ReentrantLock();

	public Boolean checkLogin(String user, String pass) throws UserNaoExistente {
		lock.lock();
		try {
			return getUser(user).isPassValid(pass);
		}
		finally {
			lock.unlock();
		}
	}

	public Boolean hasUser(String user) {
		return this.users.containsKey(user);
	}

	public void addUser(String user, String pass) throws UserNaoExistente {
		lock.lock();
		try {
			this.users.put(user, getUser(user));
			getUser(user).setPass(pass);
		}
		finally {
			lock.unlock();
		}
	}

	public void addReserva(String user, String idR) throws UserIsNotClient, UserNaoExistente {
		lock.lock();
		try {
			if (!(getUser(user) instanceof Client)) throw new UserIsNotClient();
			((Client) getUser(user)).addReserva(idR);
		}
		finally {
			lock.unlock();
		}
	}

	public void removeReserva(String user, String idR) throws UserIsNotClient, UserNaoExistente {
		lock.lock();
		try {
			if (!(getUser(user) instanceof Client)) throw new UserIsNotClient();
			((Client) getUser(user)).removeReserva(idR);
		}
		finally {
			lock.unlock();
		}
	}

	/**
	 * Devolve o utilizador
	 * @param user Identificador do utilizador
	 */
	public User getUser(String user) throws UserNaoExistente {
		lock.lock();
		try{
			if(!this.users.containsKey(user)) throw new UserNaoExistente("Utilizador "+user+" não existe.");
			return this.users.get(user).clone();
		}
		finally {
			lock.unlock();
		}
	}

	@Override
	public Boolean checkToken(String token) {
		// TODO Auto-generated method stub
		return null;
	}

}