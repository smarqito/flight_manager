package g12.Server.FlightManager.UserManager;

import java.util.Set;

public class Client extends User {

	/**
	 * IDs das reservas
	 */
	private Set<String> reservas;

	public Set<String> getReservas() {
		return this.reservas;
	}

	/**
	 * Adiciona uma reserva ao Set de reservas
	 * @param id
	 */
	public void addReserva(String id) {
		// TODO - implement Client.addReserva
		throw new UnsupportedOperationException();
	}

	/**
	 * Verifica se o ID recebido se encontra no Set de reservas
	 * @param id Identificador que se procura
	 * @param id
	 */
	public Boolean hasReserva(String id) {
		// TODO - implement Client.hasReserva
		throw new UnsupportedOperationException();
	}

}