package g12.Server.FlightManager.BookingManager;

import javax.sound.sampled.Line;
import java.util.*;
import java.util.stream.Collectors;

public class Reserva {

	private Voos infoVoos;
	private String id;
	private String user;

	public Reserva(String user) {
		this.infoVoos= new HashSet<>();
		this.id=null;
		this.user=user;
	}

	public Reserva(Reserva reserva) {
		this.infoVoos=reserva.getInfoVoos();
		this.id=reserva.getId();
		this.user=reserva.getUser();
	}

	public Collection<InfoVoo> getInfoVoos() {
		return this.infoVoos.stream().map(InfoVoo::clone).collect(Collectors.toSet());
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
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Reserva reserva = (Reserva) o;
		return this.infoVoos.equals(reserva.infoVoos) && this.id.equals(reserva.id) && this.user.equals(reserva.user);
	}

	@Override
	public int hashCode() {
		return Objects.hash(infoVoos, id, user);
	}
}