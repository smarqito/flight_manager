package g12.Server.FlightManager.UserManager;

import sun.tools.jstat.Token;

import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

public abstract class User {

	private String nome;
	private String pass;
	private ReentrantLock lockU;
	private Token tokenU;

	public User(String nome, String pass) {
		this.nome = nome;
		this.pass = pass;
		this.lockU = new ReentrantLock();
		this.tokenU = null;
	}

	public User(String nome, String pass, ReentrantLock lockU, Token tokenU) {
		this.nome = nome;
		this.pass = pass;
		this.lockU = lockU;
		this.tokenU = tokenU;
	}

	public User(User u){
		this(u.nome, u.pass);
	}

	public String getNome() {
		return this.nome;
	}
	
	public void setPass(String novaPass) {
		this.pass = novaPass;
	}

	public ReentrantLock getLockU() {
		return lockU;
	}

	public void setLockU(ReentrantLock lockU) {
		this.lockU = lockU;
	}

	public Token getTokenU() {
		return tokenU;
	}

	public void setTokenU(Token tokenU) {
		this.tokenU = tokenU;
	}

	/**
	 * Verifica se a palavra pass corresponde à que se encontra armazenada
	 * @param pass Palavra pass a verificar
	 */
	public Boolean isPassValid(String pass) {
		return this.pass.equals(pass);
	}

	public abstract User clone();

	public abstract String toString();

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		User user = (User) o;
		return Objects.equals(nome, user.nome) && Objects.equals(pass, user.pass);
	}

	@Override
	public int hashCode() {
		return Objects.hash(nome, pass);
	}
}