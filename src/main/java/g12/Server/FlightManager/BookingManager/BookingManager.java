package g12.Server.FlightManager.BookingManager;

import g12.Server.FlightManager.Exceptions.ReservaNaoExiste;
import g12.Server.FlightManager.Exceptions.VooNaoExistente;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class BookingManager implements IBookingManager {

	private Map<String, Reserva> reservas;
	private Collection<BookingDay> voos;
	private Collection<Voo> voosDiarios;
	//private Lock lock = new ReentrantLock();

	public void addFlight(String orig, String dest, Integer cap) {
		this.voos.forEach(v -> v.addVoo(new Voo(orig, dest, cap)));
	}

	public Boolean closeDay() {
		// TODO - implement BookingManager.closeDay
		throw new UnsupportedOperationException();
	}

	public String bookFlight(String user, List<String> percurso, LocalDate de, LocalDate ate) {
		// TODO - implement BookingManager.bookFlight
		throw new UnsupportedOperationException();
	}

	public void removeBooking(String bookId) {
		// TODO - implement BookingManager.removeBooking
		throw new UnsupportedOperationException();
	}

	public List<InfoVoo> getAvailableFlights() {
		// TODO - implement BookingManager.getAvailableFlights
		throw new UnsupportedOperationException();
	}

	public BookingDay getBookingDay(LocalDate date) {
		// TODO - implement BookingManager.getBookingDay
		throw new UnsupportedOperationException();
	}

	/**
	 * post:
	 * BookingManager.voos->contains(x | x.date == date) &&
	 * self.voosDiarios->forEach(y | self.voos->select(x | x.date == date).contains(y)
	 * @param date
	 */
	public BookingDay addBookingDay(LocalDate date) {
		// TODO - implement BookingManager.addBookingDay
		throw new UnsupportedOperationException();
	}

	public Reserva getReserva(String id) throws ReservaNaoExiste {
		if(!this.reservas.containsKey(id)) throw new ReservaNaoExiste("Reserva +"+id+" não existe.");
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
		if(!existeVoo(orig, dest)) throw new VooNaoExistente();
		return this.voosDiarios.stream().filter(v -> v.getOrigem().equals(orig) && v.getDestino().equals(dest)).collect(Collectors.toList()).get(0);
	}

	public void addVooDiario(String orig, String dest, Integer cap) {
		this.voosDiarios.add(new Voo(orig, dest, cap));
	}

}