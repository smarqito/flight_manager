package g12.Server.FlightManager.UserManager;

import java.util.Objects;

public abstract class User {

	private String nome;
	private String pass;

	public User(String nome, String pass) {
		this.nome = nome;
		this.pass = pass;
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