package g12.Middleware;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collection;

public class Query {

	private String method;
	private Params params;

	/**
	 * 
	 * @param method
	 * @param numParams
	 */
	public Query(String method, Integer numParams) {
		this.method = method;
		this.params = new Params(numParams);
	}

	public Query(String method, Params params) {
		this.method = method;
		this.params = params;
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

	public void addParam(Collection<String> args) {
		for (String str : args) {
			this.addParam(str);
		}
	}

	public void serialize(DataOutputStream out) throws IOException {
		out.writeUTF(this.method);
		this.params.serialize(out);
	}

	public static Query deserialize(DataInputStream in) throws IOException {
		String method = in.readUTF();
		Params p = Params.deserialize(in);
		return new Query(method, p);
	}

}