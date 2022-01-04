package g12.Server.FlightManager.BookingManager;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BookingManager implements IBookingManager {

	private Reserva reservas;
	private Collection<BookingDay> voos;
	private Collection<Voo> voosDiarios;
	private Lock lock = new ReentrantLock();

	/**
	 * 
	 * @param orig
	 * @param dest
	 * @param cap
	 */
	public void addFlight(String orig, String dest, Integer cap) {
		// TODO - implement BookingManager.addFlight
		throw new UnsupportedOperationException();
	}

	public Boolean closeDay() {
		// TODO - implement BookingManager.closeDay
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param user
	 * @param percurso
	 * @param de
	 * @param ate
	 */
	public String bookFlight(String user, List<String> percurso, LocalDate de, LocalDate ate) {
		// TODO - implement BookingManager.bookFlight
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param bookId
	 */
	public void removeBooking(String bookId) {
		// TODO - implement BookingManager.removeBooking
		throw new UnsupportedOperationException();
	}

	public Voos getAvailableFlights() {
		// TODO - implement BookingManager.getAvailableFlights
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param date
	 */
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

	/**
	 * 
	 * @param id
	 */
	public Reserva getReserva(String id) {
		// TODO - implement BookingManager.getReserva
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param user
	 * @param infoVoos
	 */
	public String addReserva(String user, List<InfoVoo> infoVoos) {
		// TODO - implement BookingManager.addReserva
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param orig
	 * @param dest
	 */
	public Voo getVooDiario(String orig, String dest) {
		// TODO - implement BookingManager.getVooDiario
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param orig
	 * @param dest
	 * @param cap
	 */
	public void addVooDiario(String orig, String dest, Integer cap) {
		// TODO - implement BookingManager.addVooDiario
		throw new UnsupportedOperationException();
	}

}