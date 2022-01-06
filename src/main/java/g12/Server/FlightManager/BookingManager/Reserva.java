package g12.Server.FlightManager.BookingManager;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class Reserva {
	private static int ID = 0;
	static Lock idl = new ReentrantLock();

	private static int GetId() {
		idl.lock();
		int id;
		try {
			id = ID++;
		} finally {
			idl.unlock();
		}
		return id;
	}


	private Voos infoVoos;
	private String id;
	private String user;
	public Lock l = new ReentrantLock();
	
	public Reserva(String user) {
		this.infoVoos = new Voos();
		this.id = GetId() + "";
		this.user = user;
	}

	public Reserva(Reserva reserva) {
		this.infoVoos = reserva.getInfoVoos();
		this.id = reserva.getId();
		this.user = reserva.getUser();
	}

	public Voos getInfoVoos() {
		return this.infoVoos.stream().map(InfoVoo::clone).collect(Collectors.toCollection(() -> new Voos()));
	}

	public String getId() {
		return this.id;
	}

	public String getUser() {
		return this.user;
	}

	public void addVooInfo(InfoVoo info) {
		this.infoVoos.add(info);
	}

	public void addVooInfo(List<InfoVoo> info) {
		this.infoVoos.addAll(info);
	}

	public Reserva clone() {
		return new Reserva(this);
	}

	@Override
	public String toString() {
		return "Reserva{" +
				"infoVoos=" + infoVoos +
				", id='" + id + '\'' +
				", user='" + user + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Reserva reserva = (Reserva) o;
		return this.infoVoos.equals(reserva.infoVoos) && this.id.equals(reserva.id) && this.user.equals(reserva.user);
	}

	@Override
	public int hashCode() {
		return Objects.hash(infoVoos, id, user);
	}
}