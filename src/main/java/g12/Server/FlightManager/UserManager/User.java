package g12.Server.FlightManager.UserManager;

public abstract class User {

	private String nome;
	private String pass;

	public String getNome() {
		return this.nome;
	}

	public String getPass() {
		return this.pass;
	}

	/**
	 * Verifica se a palavra pass corresponde à que se encontra armazenada
	 * @param pass Palavra pass a verificar
	 * @param pass
	 */
	public Boolean isPassValid(String pass) {
		// TODO - implement User.isPassValid
		throw new UnsupportedOperationException();
	}

}