package g12.Middleware.DTO.QueryDTO;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.time.LocalDate;

import g12.Middleware.Params;

public class BookFlightQueryDTO extends QueryDTO {
	private Params percurso;
	private LocalDate de;
	private LocalDate ate;

	public BookFlightQueryDTO(int tag) {
		super(tag, BookFlightQueryDTO.class.getSimpleName());
	}

	public BookFlightQueryDTO(int tag, Params percurso, LocalDate de, LocalDate ate) {
		this(tag);
		this.percurso = percurso;
		this.de = de;
		this.ate = ate;
	}

	public Params getPercurso() {
		return percurso;
	}

	public void setPercurso(Params percurso) {
		this.percurso = percurso;
	}

	public LocalDate getDe() {
		return de;
	}

	public void setDe(LocalDate de) {
		this.de = de;
	}

	public LocalDate getAte() {
		return ate;
	}

	public void setAte(LocalDate ate) {
		this.ate = ate;
	}

	@Override
	public void serialize(DataOutputStream out) throws IOException {
		super.serialize(out);
		percurso.serialize(out);
		out.writeLong(de.toEpochDay());
		out.writeLong(ate.toEpochDay());
	}

	public static BookFlightQueryDTO deserialize(DataInputStream in) throws IOException {
		int tag = in.readInt();
		Params p = Params.deserialize(in);
		LocalDate de = LocalDate.ofEpochDay(in.readLong());
		LocalDate ate = LocalDate.ofEpochDay(in.readLong());
		return new BookFlightQueryDTO(tag, p, de, ate);
	}
}