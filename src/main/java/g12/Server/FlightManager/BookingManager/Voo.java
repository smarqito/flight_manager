package g12.Server.FlightManager.BookingManager;

import g12.Server.FlightManager.Exceptions.CapacidadeMaximaAtingida;

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
	private String id;
	private String origem;
	private String destino;
	private Integer capacidadeAtual;
	private Integer capacidadeMax;

	//private Condition cond = this.lock.newCondition();

	/**
	 * Nao lhe associa um ID
	 * @param origem
	 * @param dest
	 * @param cap
	 */
	public Voo(String origem, String dest, Integer cap) {
		this.lockVoo = new ReentrantLock();
		this.id=null;
		this.origem=origem;
		this.destino=dest;
		this.capacidadeAtual=0;
		this.capacidadeMax=cap;
	}

	/**
	 * gera um novo ID!
	 * @param voo
	 */
	public Voo(Voo voo) {
		this.lockVoo=voo.getLockVoo();
		this.id=""+GetID();
		this.origem=voo.getOrigem();
		this.destino=voo.getDestino();
		this.capacidadeAtual=voo.getCapacidadeAtual();
		this.capacidadeMax=voo.getCapacidadeMax();
	}

	public Lock getLockVoo() {
		return lockVoo;
	}

	public void setLockVoo(Lock lockVoo) {
		this.lockVoo = lockVoo;
	}

	public String getId() {
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

	public Integer getCapacidadeAtual() {
		return this.capacidadeAtual;
	}

	public void setCapacidadeAtual(Integer capacidade) {
		this.capacidadeAtual = capacidade;
	}

	public Integer getCapacidadeMax() {
		return this.capacidadeMax;
	}

	public void addUser() throws CapacidadeMaximaAtingida {
		if(this.capacidadeAtual>=this.capacidadeMax) throw new CapacidadeMaximaAtingida("Voo "+id+" atingiu a sua capacidade máxima.");
		this.capacidadeAtual++;
	}

	public void removeUser() {
		if(this.capacidadeAtual>0) this.capacidadeAtual--;
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
				", capacidadeAtual=" + capacidadeAtual +
				", capacidadeMax=" + capacidadeMax +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Voo voo = (Voo) o;
		return Objects.equals(lockVoo, voo.lockVoo) && Objects.equals(id, voo.id) && Objects.equals(origem, voo.origem) && Objects.equals(destino, voo.destino) && Objects.equals(capacidadeAtual, voo.capacidadeAtual) && Objects.equals(capacidadeMax, voo.capacidadeMax);
	}

	@Override
	public int hashCode() {
		return Objects.hash(lockVoo, id, origem, destino, capacidadeAtual, capacidadeMax);
	}
}