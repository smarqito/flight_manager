package g12.Server.FlightManager.UserManager;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Client extends User {

	/**
	 * IDs das reservas
	 */
	private Set<String> reservas;

	public Client(String nome, String pass) {
		super(nome, pass);
		this.reservas = new HashSet<>();
	}

	public Client(Client c) {
		super(c);
		this.reservas = c.getReservas();
	}

	public Set<String> getReservas() {
		return this.reservas.stream().collect(Collectors.toSet());
	}

	public void setReservas(Set<String> reservas) {
		this.reservas = new HashSet<>(reservas);
	}

	/**
	 * Adiciona uma reserva ao Set de reservas
	 * 
	 * @param id Identificador a adicionar
	 */
	public void addReserva(String id) {
		this.reservas.add(id);
	}

	/**
	 * Remove uma reserva ao Set de reservas
	 * 
	 * @param id Identificador a remover
	 */
	public void removeReserva(String id) {
		this.reservas.remove(id);
	}

	/**
	 * Verifica se o ID recebido se encontra no Set de reservas
	 * 
	 * @param id Identificador que se procura
	 */
	public Boolean hasReserva(String id) {
		return this.reservas.contains(id);
	}

	@Override
	public User clone() {
		return new Client(this);
	}

	@Override
	public String toString() {
		return "Client{" +
				"reservas=" + reservas +
				'}';
	}
}