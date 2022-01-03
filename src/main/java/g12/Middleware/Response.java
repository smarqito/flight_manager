package g12.Middleware;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Response {

	private Integer respCode;
	private String respBody;

	/**
	 * 
	 * @param code
	 * @param body
	 */
	public Response(Integer code, String body) {
		this.respBody = body;
		this.respCode = code;
	}

	public Integer getRespCode() {
		return respCode;
	}

	public void setRespCode(Integer respCode) {
		this.respCode = respCode;
	}

	public String getRespBody() {
		return respBody;
	}

	public void setRespBody(String respBody) {
		this.respBody = respBody;
	}

	public void serialize(DataOutputStream out) throws IOException {
		out.writeInt(respCode);
		out.writeUTF(respBody);
	}

	public static Response deserialize(DataInputStream in) throws IOException {
		int rcode = in.readInt();
		String rBody = in.readUTF();
		return new Response(rcode, rBody);
	}
}