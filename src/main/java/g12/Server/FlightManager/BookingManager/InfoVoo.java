package g12.Server.FlightManager.BookingManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;

public class InfoVoo {

	public String id;
	public String origem;
	public String destino;
	public LocalDate data;

	public InfoVoo(String id, String origem, String destino, LocalDate data) {
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

	public String getId() {
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
	public void serialize(DataOutputStream dos) throws IOException {
		dos.writeUTF(this.id);
		dos.writeUTF(this.origem);
		dos.writeUTF(this.destino);
		dos.writeUTF(String.valueOf(this.data));
	}

	/**
	 * 
	 * @param dis
	 */
	public void deserialize(DataInputStream dis) throws IOException {
		Integer id = dis.readInt();
		String origem = dis.readUTF();
		String destino = dis.readUTF();
		LocalDate date = LocalDate.parse(dis.readUTF());
	}

	public InfoVoo clone(){
		return new InfoVoo(this);
	}

	@Override
	public String toString() {
		return "InfoVoo{" +
				"id=" + id +
				", origem='" + origem + '\'' +
				", destino='" + destino + '\'' +
				", data=" + data +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		InfoVoo infoVoo = (InfoVoo) o;
		return this.id.equals(infoVoo.id) && this.origem.equals(infoVoo.origem) && this.destino.equals(infoVoo.destino) && this.data.equals(infoVoo.data);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, origem, destino, data);
	}
}