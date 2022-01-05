package g12.Middleware;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;

public class Query extends Frame {

	private final String token;
	private final String method;
	private final Params params;

	/**
	 * 
	 * @param method
	 * @param numParams
	 */
	public Query(int tag, String token, String method, Integer numParams) {
		super(tag);
		this.token = token;
		this.method = method;
		this.params = new Params(numParams);
	}

	public Query(int tag, String token, String method, Params params) {
		super(tag);
		this.token = token;
		this.method = method;
		this.params = params;
	}

	public String getToken() {
		return token;
	}

	public String getMethod() {
		return method;
	}

	public Params getParams() {
		return params;
	}

	public void addParam(String arg) {
		this.params.add(arg);
	}

	public void addParam(LocalDate date) {
		this.params.add(date.toString());
	}

	public void addParam(Collection<String> args) {
		for (String str : args) {
			this.addParam(str);
		}
	}

	@Override
	public void serialize(DataOutputStream out) throws IOException {
		super.serialize(out);
		out.writeUTF(this.token);
		out.writeUTF(this.method);
		this.params.serialize(out);
	}

	public static Query deserialize(DataInputStream in) throws IOException {
		int tag = in.readInt();
		String token = in.readUTF();
		String method = in.readUTF();
		Params p = Params.deserialize(in);
		return new Query(tag, token, method, p);
	}
}