package g12.Server.FlightManager.BookingManager;

import g12.Server.FlightManager.Exceptions.*;

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
	public Boolean closeDay() throws DiaFechado, BookingDayNaoExistente {
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

	public String bookFlight(String user, List<String> percurso, LocalDate de, LocalDate ate) throws VooNaoExistente, ReservaIndisponivel, PercusoNaoDisponivel {
		l.lock();
		try {
			for (int i = 0; i < percurso.size() - 1; i++) {
				if (!existeVoo(percurso.get(i), percurso.get(i + 1)))
					throw new PercusoNaoDisponivel("O percurso não está disponível.");
			}
		} finally {
			l.unlock();
		}
		Reserva r = new Reserva(user);
		while (!percurso.isEmpty() && de.isBefore(ate)) {
			l.lock();
			BookingDay bd;
			try {
				bd = this.getBookingDay(de);
				bd.l.lock();
			} finally {
				l.unlock();
			}
			try {
				List<InfoVoo> info = bd.addPassager(percurso);
				r.addVooInfo(info);
				info.forEach(x -> percurso.remove(x.origem));
				de = de.plusDays(1);
			} catch (DiaFechado e) {
				de = de.plusDays(1);
			} finally {
				bd.l.unlock();
			}
		}
		if (!percurso.isEmpty())
			throw new ReservaIndisponivel();
		l.lock();
		try {
			this.reservas.put(r.getId(), r);
			return r.getId();
		} finally {
			l.unlock();
		}
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
				bd = this.getBookingDay(v.getData());
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

	public List<Voo> getAvailableFlights() {
		this.l.lock();
		try {
			return this.voosDiarios.stream().map(Voo::clone).collect(Collectors.toList());
		} finally {
			this.l.unlock();
		}
	}

	public BookingDay getBookingDay(LocalDate date) throws BookingDayNaoExistente {
		this.l.lock();
		try {
			if(this.voos.stream().noneMatch(bd -> bd.getDate().equals(date))) throw new BookingDayNaoExistente();
			return this.voos.stream().filter(bday -> bday.getDate().equals(date)).collect(Collectors.toList()).get(0);
		}
		finally {
			this.l.unlock();
		}
	}

	/**
	 * post:
	 * BookingManager.voos->contains(x | x.date == date) &&
	 * self.voosDiarios->forEach(y | self.voos->select(x | x.date ==
	 * date).contains(y)
	 * 
	 * @param date
	 */
	public BookingDay addBookingDay(LocalDate date) throws DiaFechado, VooJaExiste {
		this.l.lock();
		BookingDay bd;
		try {
			bd = new BookingDay(date);
			this.voos.add(bd);

			bd.l.lock();
		}
		finally {
			this.l.unlock();
		}

		try{
			for(Voo v : this.voosDiarios) bd.addVoo(v);
			return bd;
		}
		finally {
			bd.l.unlock();
		}
	}

	public Reserva getReserva(String id) throws ReservaNaoExiste {
		this.l.lock();
		try {
			if (!this.reservas.containsKey(id)) throw new ReservaNaoExiste("Reserva +" + id + " não existe.");
			return this.reservas.get(id);
		}
		finally {
			this.l.unlock();
		}
	}

	public String addReserva(String user, List<InfoVoo> infoVoos) {
		this.l.lock();
		Reserva r;
		try {
			r = new Reserva(user);
			this.reservas.put(r.getId(), r);

			r.l.lock();
		}
		finally {
			this.l.unlock();
		}

		try {
			r.addVooInfo(infoVoos);
			return r.getId();
		}
		finally {
			r.l.unlock();
		}
	}

	public Voo getVooDiario(String orig, String dest) throws VooNaoExistente {
		this.l.lock();
		try {
			if (this.voosDiarios.stream().noneMatch(v -> v.getOrigem().equals(orig) && v.getDestino().equals(dest))) throw new VooNaoExistente();

			return this.voosDiarios.stream().filter(v -> v.getOrigem().equals(orig) && v.getDestino().equals(dest))
					.collect(Collectors.toList()).get(0);
		}
		finally {
			this.l.unlock();
		}
	}
}