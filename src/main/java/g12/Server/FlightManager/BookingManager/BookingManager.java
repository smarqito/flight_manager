package g12.Server.FlightManager.BookingManager;

import g12.Server.FlightManager.Exceptions.DiaFechado;
import g12.Server.FlightManager.Exceptions.ReservaNaoExiste;
import g12.Server.FlightManager.Exceptions.VooJaExiste;
import g12.Server.FlightManager.Exceptions.VooNaoExistente;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class BookingManager implements IBookingManager {

	private Map<String, Reserva> reservas;
	private Set<BookingDay> voos;
	private List<Voo> voosDiarios;
	private Lock l = new ReentrantLock();

	public BookingManager() {
		reservas = new HashMap<>();
		voos = new TreeSet<>();
		voosDiarios = new ArrayList<>();
	}

	@Override
	public void addFlight(String orig, String dest, Integer cap) {
		this.l.lock();
		try {
			Voo v = new Voo(orig, dest, cap);
			this.voosDiarios.add(v);
			Set<BookingDay> days = this.voos.stream()
					.filter(x -> x.getDate().compareTo(LocalDate.now()) >= 0 && x.isOpen())
					.collect(Collectors.toSet());
			// obtem locks por ordem de data
			days.stream().forEach(d -> d.l.lock());
			try {
				days.stream().forEach(d -> {
					try {
						d.addVoo(new Voo(v));
					} catch (VooJaExiste | DiaFechado e) {
						// ja controlado nos streams anteriores. Voo e novo ID
					}
				});
			} finally {
				// unlock pela mesma ordem
				days.stream().forEach(d -> d.l.unlock());
			}
		} finally {
			this.l.unlock();
		}
	}

	@Override
	public Boolean closeDay() throws DiaFechado {
		this.l.lock();
		BookingDay bd;
		try {
			bd = this.getBookingDay(LocalDate.now());
			bd.l.lock();
		} finally {
			this.l.unlock();
		}
		try {
			bd.closeDay();
			return true;
		} finally {
			bd.l.unlock();
		}
	}

	public String bookFlight(String user, List<String> percurso, LocalDate de, LocalDate ate) {
		// TODO - implement BookingManager.bookFlight
		throw new UnsupportedOperationException();
	}

	class VooIdData implements Comparable<VooIdData> {
		private List<String> idList = new ArrayList<>();
		private LocalDate data;

		public VooIdData(String id, LocalDate data){
			this.idList.add(id);
			this.data = data;
		}

		public List<String> getIdList() {
			return new ArrayList<>(idList);
		}

		public LocalDate getData() {
			return data;
		}

		public void addId(String id){
			this.idList.add(id);
		}

		@Override
		public int compareTo(VooIdData o) {
			return this.data.compareTo(o.getData());
		}
	}

	@Override
	public void removeBooking(String bookId) throws ReservaNaoExiste, VooNaoExistente {
		this.l.lock();
		Reserva r;
		try {
			r = this.getReserva(bookId);
			//falta verificar se existe algum lock à espera
			this.reservas.remove(bookId);
			r.l.lock();
		} finally {
			this.l.unlock();			
		}
		Set<VooIdData> voosReservados = new HashSet<>();
		try {
			for(InfoVoo i : r.getInfoVoos()){
				VooIdData v = voosReservados.stream().filter(x -> x.getData().equals(i.getData())).findFirst().orElse(null);
				if(v == null){
					voosReservados.add(new VooIdData(i.getId(), i.getData()));
				}else{
					v.addId(i.getId());
				}
			}
		} finally {
			r.l.unlock();
		}
		for(VooIdData v : voosReservados) {
			this.l.lock();
			BookingDay bd;
			try {
				bd = this.voos.stream().filter(x -> x.getDate().equals(v.getData())).findFirst().get();
				bd.l.lock();
			} finally {
				l.unlock();
			}
			Voo voo;
			for(String id : v.getIdList()) {
				try {
					voo = bd.getVoo(id);
					voo.l.lock();
					try {
						voo.removeUser();
					} finally {
						voo.l.unlock();
					}
				} finally {
					bd.l.unlock();
				}
			}
		}
	}

	public Voos getAvailableFlights() {
		Voos voos = new Voos();
		this.l.lock();
		try {
			try {
				for(BookingDay bd : this.voos){
					bd.l.lock();
					for(Voo v : bd.getVoos().values()){
						if(v.temLugarLivres())
							voos.add(new InfoVoo(v.getId(), v.getOrigem(), v.getDestino(), bd.getDate()));
					}
				}
			} finally {
				for(BookingDay bd : this.voos){
					bd.l.unlock();
				}
			}
		} finally {
			this.l.unlock();
		}
		return voos;
	}

	public BookingDay getBookingDay(LocalDate date) {
		// TODO - implement BookingManager.getBookingDay
		throw new UnsupportedOperationException();
	}

	/**
	 * post:
	 * BookingManager.voos->contains(x | x.date == date) &&
	 * self.voosDiarios->forEach(y | self.voos->select(x | x.date ==
	 * date).contains(y)
	 * 
	 * @param date
	 */
	public BookingDay addBookingDay(LocalDate date) {
		// TODO - implement BookingManager.addBookingDay
		throw new UnsupportedOperationException();
	}

	public Reserva getReserva(String id) throws ReservaNaoExiste {
		if (!this.reservas.containsKey(id))
			throw new ReservaNaoExiste("Reserva +" + id + " não existe.");
		return this.reservas.get(id);
	}

	public String addReserva(String user, List<InfoVoo> infoVoos) {
		// TODO - implement BookingManager.addReserva
		throw new UnsupportedOperationException();
	}

	public Boolean existeVoo(String orig, String dest) {
		return this.voosDiarios.stream().anyMatch(v -> v.getOrigem().equals(orig) && v.getDestino().equals(dest));
	}

	public Voo getVooDiario(String orig, String dest) throws VooNaoExistente {
		if (!existeVoo(orig, dest))
			throw new VooNaoExistente();
		return this.voosDiarios.stream().filter(v -> v.getOrigem().equals(orig) && v.getDestino().equals(dest))
				.collect(Collectors.toList()).get(0);
	}
}