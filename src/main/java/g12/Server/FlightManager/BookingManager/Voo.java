package g12.Server.FlightManager.BookingManager;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Voo {

	private Lock lock = new ReentrantLock();
	private Condition cond = this.lock.newCondition();
	private Integer id;
	private String origem;
	private String destino;
	private Integer capacidade;

	public Integer getId() {
		return this.id;
	}

	public String getOrigem() {
		return this.origem;
	}

	public void setOrigem(String origem) {
		this.origem = origem;
	}

	public String getDestino() {
		return this.destino;
	}

	public void setDestino(String destino) {
		this.destino = destino;
	}

	public Integer getCapacidade() {
		return this.capacidade;
	}

	public void setCapacidade(Integer capacidade) {
		this.capacidade = capacidade;
	}

	/**
	 * Nao lhe associa um ID
	 * @param origem
	 * @param dest
	 * @param cap
	 */
	public Voo(String origem, String dest, Integer cap) {
		// TODO - implement Voo.Voo
		throw new UnsupportedOperationException();
	}

	/**
	 * gera um novo ID!
	 * @param voo
	 */
	public Voo(Voo voo) {
		// TODO - implement Voo.Voo
		throw new UnsupportedOperationException();
	}

	public void addUser() {
		// TODO - implement Voo.addUser
		throw new UnsupportedOperationException();
	}

	public Voo clone() {
		// TODO - implement Voo.clone
		throw new UnsupportedOperationException();
	}

}