package g12.Server.FlightManager.BookingManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.time.LocalDate;

public class InfoVoo {

	public Integer id;
	public String origem;
	public String destino;
	public LocalDate data;

	public InfoVoo(Integer id, String origem, String destino, LocalDate data) {
		this.id = id;
		this.origem = origem;
		this.destino = destino;
		this.data = data;
	}

	public InfoVoo(InfoVoo voo) {
		this.id=voo.getId();
		this.origem=voo.getOrigem();
		this.destino=voo.getDestino();
		this.data=voo.getData();
	}

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

	public InfoVoo clone(){
		return new InfoVoo(this);
	}

}