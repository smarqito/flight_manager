package g12.Server.FlightManager.BookingManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.time.LocalDate;

public class InfoVoo {

	public Integer id;
	public String origem;
	public String destino;
	public LocalDate data;

	public Integer getId() {
		return this.id;
	}

	public String getOrigem() {
		return this.origem;
	}

	public String getDestino() {
		return this.destino;
	}

	public LocalDate getData() {
		return this.data;
	}

	/**
	 * 
	 * @param dos
	 */
	public void serialize(DataOutputStream dos) {
		// TODO - implement InfoVoo.serialize
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param dis
	 */
	public void deserialize(DataInputStream dis) {
		// TODO - implement InfoVoo.deserialize
		throw new UnsupportedOperationException();
	}

}