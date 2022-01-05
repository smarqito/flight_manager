package g12.Server.FlightManager.BookingManager;

import g12.Server.FlightManager.Exceptions.CapacidadeMaximaAtingida;
import g12.Server.FlightManager.Exceptions.DiaFechado;
import g12.Server.FlightManager.Exceptions.PercusoNaoDisponivel;
import g12.Server.FlightManager.Exceptions.VooNaoExistente;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class BookingDay {

	private Map<String, Voo> voos;
	private LocalDate date;
	private Boolean isOpen;
	//private Lock lock = new ReentrantLock();
	//private Condition cond = this.lock.newCondition();

	public BookingDay(LocalDate date) {
		this.voos=new HashMap<>();
		this.date=date;
		this.isOpen=true;
	}

	public BookingDay(BookingDay bd){
		this.voos=bd.getVoos();
		this.date=bd.getDate();
		this.isOpen=bd.getIsOpen();
	}

	public Map<String, Voo> getVoos() {
		return this.voos.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e->e.getValue().clone()));
	}

	public LocalDate getDate() {
		return this.date;
	}

	public Boolean getIsOpen() {
		return this.isOpen;
	}

	public void setIsOpen(Boolean isOpen) {
		this.isOpen = isOpen;
	}

	public Voo getVoo(String idVoo) throws VooNaoExistente {
		if(!this.voos.containsKey(idVoo)) throw new VooNaoExistente("Voo "+idVoo+" não existe.");
		return this.voos.get(idVoo);
	}

	public boolean existeVoo(String origem, String destino) {
		return this.voos.values().stream().anyMatch(v -> v.getOrigem().equals(origem) && v.getDestino().equals(destino));
	}

	public Voo getVooOD(String origem, String destino) throws VooNaoExistente {
		if(!existeVoo(origem, destino)) throw new VooNaoExistente();
		return this.voos.values().stream().filter(v -> v.getOrigem().equals(origem) && v.getDestino().equals(destino)).collect(Collectors.toList()).get(0);
	}

	public void closeDay() throws DiaFechado {
		if(!isOpen) throw new DiaFechado("Dia está fechado.");
	}

	public List<InfoVoo> addPassager(List<String> percurso) throws PercusoNaoDisponivel, VooNaoExistente, CapacidadeMaximaAtingida {
		List<InfoVoo> ret = new ArrayList<>();
		ListIterator<String> it = percurso.listIterator();

		while(it.hasNext()){
			it.next();
			if(it.hasPrevious()){
				if(!existeVoo(it.previous(), it.next())) throw new PercusoNaoDisponivel("O percurso não está disponível.");
				Voo curr = getVooOD(it.previous(), it.next());
				curr.addUser();
				ret.add(new InfoVoo(curr.getId(), curr.getOrigem(), curr.getDestino(), this.date));
			}
		}
		return ret;
	}

	public void addVoo(Voo voo) {
		this.voos.put(voo.getId(), voo);
	}

	//public int compareTo(BookingDay bookD);

	public BookingDay clone() {
		return new BookingDay(this);
	}

	@Override
	public String toString() {
		return "BookingDay{" +
				"voos=" + voos +
				", date=" + date +
				", isOpen=" + isOpen +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		BookingDay that = (BookingDay) o;
		return this.voos.equals(that.voos) && this.date.equals(that.date) && this.isOpen.equals(that.isOpen);
	}

	@Override
	public int hashCode() {
		return Objects.hash(voos, date, isOpen);
	}
}