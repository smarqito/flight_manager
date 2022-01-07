package g12.Server.FlightManager.UserManager;

import g12.Middleware.TokenInvalido;
import g12.Server.FlightManager.Exceptions.LoginInvalido;
import g12.Server.FlightManager.Exceptions.UserIsNotClient;
import g12.Server.FlightManager.Exceptions.UserJaExisteException;
import g12.Server.FlightManager.Exceptions.UserNaoExistente;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

public class UserManager implements IUserManager {

	private Map<String, User> users;
	private Lock lock = new ReentrantLock(); // para proteger o map users
	private Algorithm alg = Algorithm.HMAC256("g12sd");
	private JWTVerifier verifier = JWT.require(alg).build();

	public UserManager() {
		users = new HashMap<>();
		this.users.put("marco", new Admin("marco", "sousa"));
	}

	@Override
	public String checkLogin(String user, String pass) throws LoginInvalido {
		lock.lock();
		User u;
		try {
			u = getUser(user);
			u.lock.lock();
		} catch (UserNaoExistente une) {
			throw new LoginInvalido("Os dados de autenticacao nao correspondem!");
		} finally {
			lock.unlock();
		}
		try {
			if (u.isPassValid(pass)) {
				String token = generateToken(user);
				u.setToken(token);
				return token;
			} else {
				throw new LoginInvalido("Os dados de autenticacao nao correspondem!");
			}
		} finally {
			u.lock.unlock();
		}
	}

	@Override
	public Boolean hasUser(String user) {
		this.lock.lock();
		try {
			return this.users.containsKey(user);
		} finally {
			this.lock.unlock();
		}
	}

	@Override
	public void addUser(String user, String pass) throws UserJaExisteException {
		this.lock.lock();
		try {
			if (this.hasUser(user)) {
				throw new UserJaExisteException("O nome " + user + " ja existe!");
			}
			User u = new Client(user, pass);
			this.users.put(user, u);
		} finally {
			this.lock.unlock();
		}
	}

	@Override
	public void addReserva(String user, String idR) throws UserIsNotClient, UserNaoExistente {
		this.lock.lock();
		User u;
		try {
			u = getUser(user);
			u.lock.lock();
		} finally {
			this.lock.unlock();
		}
		try {
			if (!u.getClass().getSimpleName().equals(Client.class.getSimpleName())) {
				throw new UserIsNotClient();
			}
			Client c = (Client) u;
			c.addReserva(idR);
		} finally {
			u.lock.unlock();
		}
	}

	@Override
	public void removeReserva(String user, String idR) throws UserIsNotClient, UserNaoExistente {
		this.lock.lock();
		User u;
		try {
			u = getUser(user);
			u.lock.lock();
		} finally {
			this.lock.unlock();
		}
		try {
			if (!u.getClass().getSimpleName().equals(Client.class.getSimpleName())) {
				throw new UserIsNotClient();
			}
			Client c = (Client) u;
			c.removeReserva(idR);
		} finally {
			u.lock.unlock();
		}
	}

	@Override
	public Boolean hasReserva(String user, String id) throws UserNaoExistente, UserIsNotClient {
		this.lock.lock();
		User u;
		try {
			u = getUser(user);
			u.lock.lock();
		} finally {
			this.lock.unlock();
		}
		try {
			if (isClient(u)) {
				Client c = (Client) u;
				return c.hasReserva(id);
			}
		} finally {
			u.lock.unlock();
		}
		throw new UserIsNotClient();
	}

	@Override
	public String checkToken(String token) throws TokenInvalido {
		try {
			DecodedJWT dec = this.verifier.verify(token);
			Claim c = dec.getClaim("User");
			String user = c.asString();
			User u;
			this.lock.lock();
			try {
				u = this.getUser(user);
				u.lock.lock();
			} finally {
				this.lock.unlock();
			}
			try {
				if (u.checkToken(token)) {
					return c.asString();
				}
			} finally {
				u.lock.unlock();
			}
			throw new TokenInvalido("O token e invalido!");
		} catch (AlgorithmMismatchException | SignatureVerificationException | InvalidClaimException
				| UserNaoExistente e) {
			throw new TokenInvalido("O token e invalido!");
		}
	}

	@Override
	public User getUser(String user) throws UserNaoExistente {
		this.lock.lock();
		try {
			if (this.users.containsKey(user)) {
				return this.users.get(user);
			}
		} finally {
			this.lock.unlock();
		}
		throw new UserNaoExistente("Utilizador " + user + " não existe.");
	}

	public void setToken(String user, String token) throws UserNaoExistente {
		this.lock.lock();
		User u;
		try {
			u = getUser(user);
			u.lock.lock();
		} finally {
			this.lock.unlock();
		}
		try {
			u.setToken(token);
		} finally {
			u.lock.unlock();
		}
	}

	public String generateToken(String user) {
		boolean isAdmin = false;
		try {
			isAdmin = this.isAdmin(user);
		} catch (UserNaoExistente e) {
		}
		String token = JWT.create().withClaim("User", user).withClaim("isAdmin", isAdmin).sign(this.alg);
		return token;
	}

	@Override
	public Boolean isAdmin(String user) throws UserNaoExistente {
		User u;
		this.lock.lock();
		try {
			u = getUser(user);
			u.lock.lock();
		} finally {
			this.lock.unlock();
		}
		try {
			return isAdmin(u);
		} finally {
			u.lock.unlock();
		}
	}

	private Boolean isAdmin(User u) {
		return u.getClass().getSimpleName().equals(Admin.class.getSimpleName());
	}

	@Override
	public Boolean isClient(String user) throws UserNaoExistente {
		this.lock.lock();
		User u;
		try {
			u = this.getUser(user);
			u.lock.lock();
		} finally {
			this.lock.unlock();
		}

		try {
			return this.isClient(u);
		} finally {
			u.lock.unlock();
		}
	}

	private Boolean isClient(User u) {
		return u.getClass().getSimpleName().equals(Client.class.getSimpleName());
	}

}