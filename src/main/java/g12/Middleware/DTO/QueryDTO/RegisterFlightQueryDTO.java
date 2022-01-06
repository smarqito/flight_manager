package g12.Middleware.DTO.QueryDTO;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class RegisterFlightQueryDTO extends QueryDTO {
	private String origem;
	private String dest;
	private Integer capacidade;

	public RegisterFlightQueryDTO(int tag) {
		super(tag, RegisterFlightQueryDTO.class.getSimpleName());
	}

	public RegisterFlightQueryDTO(int tag, String origem, String dest, Integer capacidade) {
		this(tag);
		this.origem = origem;
		this.dest = dest;
		this.capacidade = capacidade;
	}

	public String getOrigem() {
		return origem;
	}

	public void setOrigem(String origem) {
		this.origem = origem;
	}

	public String getDest() {
		return dest;
	}

	public void setDest(String dest) {
		this.dest = dest;
	}

	public Integer getCapacidade() {
		return capacidade;
	}

	public void setCapacidade(Integer capacidade) {
		this.capacidade = capacidade;
	}

	@Override
	public void serialize(DataOutputStream out) throws IOException {
		super.serialize(out);
		out.writeUTF(origem);
		out.writeUTF(dest);
		out.writeInt(capacidade);
	}

	public static RegisterFlightQueryDTO deserialize(DataInputStream in) throws IOException {
		int tag = in.readInt();
		String org = in.readUTF();
		String dest = in.readUTF();
		int cap = in.readInt();
		return new RegisterFlightQueryDTO(tag, org, dest, cap);
	}

}