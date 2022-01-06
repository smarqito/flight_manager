package g12.Server.FlightManager.BookingManager;

import g12.Server.FlightManager.Exceptions.CapacidadeMaximaAtingida;
import g12.Server.FlightManager.Exceptions.DiaFechado;
import g12.Server.FlightManager.Exceptions.PercusoNaoDisponivel;
import g12.Server.FlightManager.Exceptions.VooJaExiste;
import g12.Server.FlightManager.Exceptions.VooNaoExistente;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class BookingDay implements Comparable<BookingDay> {

	private Map<String, Voo> voos;
	private LocalDate date;
	private Boolean isOpen;
	public Lock l = new ReentrantLock();
	// private Condition cond = this.lock.newCondition();

	public BookingDay(LocalDate date) {
		this.voos = new HashMap<>();
		this.date = date;
		this.isOpen = true;
	}

	public BookingDay(BookingDay bd) {
		this.voos = bd.getVoos();
		this.date = bd.getDate();
		this.isOpen = bd.isOpen();
	}

	public Map<String, Voo> getVoos() {
		return this.voos.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().clone()));
	}

	public LocalDate getDate() {
		return this.date;
	}

	public Boolean isOpen() {
		return this.isOpen;
	}

	public Voo getVoo(String idVoo) throws VooNaoExistente {
		if (this.voos.containsKey(idVoo))
			return this.voos.get(idVoo);

		throw new VooNaoExistente("Voo " + idVoo + " não existe.");
	}

	public boolean existeVoo(String origem, String destino) {

		return this.voos.values().stream()
				.anyMatch(v -> v.getOrigem().equals(origem) && v.getDestino().equals(destino));
	}

	/**
	 * Calcula se existe algum voo entre uma origem e destino e retorna
	 * 
	 * @param origem
	 * @param destino
	 * @return
	 * @throws VooNaoExistente
	 */
	public Voo getVooOD(String origem, String destino) throws VooNaoExistente {
		try {
			return this.voos.values().stream()
					.filter(v -> v.getOrigem().equals(origem) && v.getDestino().equals(destino)).findFirst().get();
		} catch (NoSuchElementException e) {
			throw new VooNaoExistente();
		}
	}

	/**
	 * Coloca o dia como encerrado, nao permitindo novas alterações
	 * 
	 * @throws DiaFechado Caso o dia ja esteja fechado
	 */
	public void closeDay() throws DiaFechado {
		if (!isOpen)
			throw new DiaFechado("Dia está fechado.");
		isOpen = false;
	}

	/**
	 * Necessario fazer refactor. este metodo esta mal
	 * 
	 * TODO
	 * 
	 * @param percurso
	 * @return
	 * @throws VooNaoExistente
	 * @throws CapacidadeMaximaAtingida
	 * @throws DiaFechado
	 */
	public List<InfoVoo> addPassager(List<String> percurso) throws VooNaoExistente, DiaFechado {
		if (!isOpen)
			throw new DiaFechado("O dia " + this.date.toString() + " esta fechado");

		List<InfoVoo> ret = new ArrayList<>();

		for (int i = 0; i < percurso.size() - 1; i++) {
				Voo curr = getVooOD(percurso.get(i), percurso.get(i+1));
				curr.l.lock();
				try {
					curr.addUser();
				}catch (CapacidadeMaximaAtingida e) {
					return ret;
				} finally {
					curr.l.unlock();
				}
				ret.add(new InfoVoo(curr.getId(), curr.getOrigem(), curr.getDestino(), this.date));
			}
		return ret;
	}

	/**
	 * Adiciona um novo voo, caso este nao exista no dia
	 * 
	 * @param voo Voo a adicionar
	 * @throws VooJaExiste Caso o voo ja exista
	 * @throws DiaFechado
	 */
	public void addVoo(Voo voo) throws VooJaExiste, DiaFechado {
		if (isOpen) {
			if (this.voos.containsKey(voo.getId())) {
				throw new VooJaExiste();
			}

			this.voos.put(voo.getId(), voo);
		} else {
			throw new DiaFechado();
		}
	}

	/**
	 * @return Booking day clonado
	 */
	public BookingDay clone() {
		return new BookingDay(this);
	}

	@Override
	public String toString() {
		return "BookingDay{" +
				"voos=" + voos +
				", date=" + date +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		BookingDay that = (BookingDay) o;
		return this.voos.equals(that.voos) && this.date.equals(that.date) && this.isOpen.equals(that.isOpen);
	}

	@Override
	public int hashCode() {
		return Objects.hash(voos, date);
	}

	@Override
	public int compareTo(BookingDay arg0) {
		return this.date.compareTo(arg0.date);
	}
}