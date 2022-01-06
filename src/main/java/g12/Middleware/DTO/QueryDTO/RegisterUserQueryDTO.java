package g12.Middleware.DTO.QueryDTO;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class RegisterUserQueryDTO extends QueryDTO {
	private String user;
	private String pass;

	public RegisterUserQueryDTO(String user, String pass) {
		this.user = user;
		this.pass = pass;
	}

	public RegisterUserQueryDTO(String token, String user, String pass) {
		super(token);
		this.user = user;
		this.pass = pass;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	@Override
	public void serialize(DataOutputStream out) throws IOException {
		super.serialize(out);
		out.writeUTF(user);
		out.writeUTF(pass);
	}

	/**
	 * int tag
	 * 
	 * @throws IOException
	 */
	public static RegisterUserQueryDTO deserialize(DataInputStream in) throws IOException {
		String token = in.readUTF();
		String user = in.readUTF();
		String pass = in.readUTF();
		return new RegisterUserQueryDTO(token, user, pass);
	}
}