package g12.Server.FlightManager.BookingManager;

import java.util.Objects;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Voo {

	private static int ID = 1;

	private static int GetID() {
		return ID++;
	}

	private Lock lockVoo = new ReentrantLock();
	private Integer id;
	private String origem;
	private String destino;
	private Integer lugaresOcupados;
	private Integer capacidade;

	// private Condition cond = this.lock.newCondition();

	/**
	 * Nao lhe associa um ID
	 * 
	 * @param origem
	 * @param dest
	 * @param cap
	 */
	public Voo(String origem, String dest, Integer cap) {
		this.lockVoo = new ReentrantLock();
		this.id = 0;
		this.origem = origem;
		this.destino = dest;
		this.lugaresOcupados = 0;
		this.capacidade = cap;
	}

	/**
	 * gera um novo ID!
	 * 
	 * @param voo
	 */
	public Voo(Voo voo) {
		this.id = GetID();
		this.origem = voo.getOrigem();
		this.destino = voo.getDestino();
		this.capacidade = voo.getCapacidade();
		this.lugaresOcupados = voo.getLugaresOcupados();
	}

	public Integer getId() {
		return this.id;
	}

	public String getOrigem() {
		return this.origem;
	}

	public String getDestino() {
		return this.destino;
	}

	public Integer getCapacidade() {
		return this.capacidade;
	}

	public Integer getLugaresOcupados() {
		return this.lugaresOcupados;
	}

	public void addUser() {
		if (this.lugaresOcupados < this.capacidade)
			this.capacidade++;
	}

	public void removeUser() {
		if (this.lugaresOcupados > 0)
			this.lugaresOcupados--;
	}

	public Voo clone() {
		return new Voo(this);
	}

	@Override
	public String toString() {
		return "Voo{" +
				"lockVoo=" + lockVoo +
				", id=" + id +
				", origem='" + origem + '\'' +
				", destino='" + destino + '\'' +
				", lugaresOcupados=" + lugaresOcupados +
				", capacidade=" + capacidade +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Voo voo = (Voo) o;
		return id.equals(voo.id) && origem.equals(voo.origem) && destino.equals(voo.destino);
	}

	@Override
	public int hashCode() {
		return Objects.hash(lockVoo, id, origem, destino);
	}
}