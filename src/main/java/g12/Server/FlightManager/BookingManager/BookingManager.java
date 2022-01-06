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

	public String bookFlight(String user, List<String> percurso, LocalDate de, LocalDate ate) {
		// TODO - implement BookingManager.bookFlight
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeBooking(String bookId) throws ReservaNaoExiste {
		this.l.lock();
		Reserva r;
		try {
			r = this.getReserva(bookId);
			r.l.lock();
		} finally {
			this.l.unlock();			
		}
		try {
			// iterar o infoVoos
			// preencher lista de datas e lista de ids
			// ir buscar os bookingDays associados
			// obter lock dos booking days por ordem data
			// remover um passageiro a cada voo
		} finally {

		}

	}

	public Voos getAvailableFlights() {
		// TODO - implement BookingManager.getAvailableFlights
		throw new UnsupportedOperationException();
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