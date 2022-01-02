package g12.Server.FlightManager.BookingManager;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BookingDay {

	private Voo voos;
	private LocalDate date;
	private Boolean isOpen;
	private Lock lock = new ReentrantLock();
	private Condition cond = this.lock.newCondition();

	public LocalDate getDate() {
		return this.date;
	}

	public Boolean getIsOpen() {
		return this.isOpen;
	}

	public void setIsOpen(Boolean isOpen) {
		this.isOpen = isOpen;
	}

	/**
	 * 
	 * @param date
	 */
	public BookingDay(LocalDate date) {
		// TODO - implement BookingDay.BookingDay
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param bookD
	 */
	public int compareTo(BookingDay bookD) {
		// TODO - implement BookingDay.compareTo
		throw new UnsupportedOperationException();
	}

	public void closeDay() {
		// TODO - implement BookingDay.closeDay
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param percurso
	 */
	public List<InfoVoo> addPassager(List<String> percurso) {
		// TODO - implement BookingDay.addPassager
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param voo
	 */
	public void addVoo(Voo voo) {
		// TODO - implement BookingDay.addVoo
		throw new UnsupportedOperationException();
	}

}