package g12.Server.FlightManager.UserManager;

import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class User {

	private String nome;
	private String pass;
	public Lock lock;
	private String token;

	public User(String nome, String pass) {
		this.nome = nome;
		this.pass = pass;
		this.lock = new ReentrantLock();
		this.token = "";
	}

	public User(User u) {
		this(u.nome, u.pass);
	}

	public String getNome() {
		return this.nome;
	}

	public void setPass(String novaPass) {
		this.pass = novaPass;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String newToken) {
		this.token = newToken;
	}

	public boolean checkToken(String toToken) {
		return this.token.equals(toToken);
	}

	/**
	 * Verifica se a palavra pass corresponde à que se encontra armazenada
	 * 
	 * @param pass Palavra pass a verificar
	 */
	public Boolean isPassValid(String pass) {
		return this.pass.equals(pass);
	}

	public abstract User clone();

	public abstract String toString();

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		User user = (User) o;
		return this.nome.equals(user.nome) && pass.equals(user.pass);
	}

	@Override
	public int hashCode() {
		return Objects.hash(nome, pass);
	}
}