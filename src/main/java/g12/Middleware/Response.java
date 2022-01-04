package g12.Middleware;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Response extends Frame {

	private Integer respCode;
	private String respBody;

	/**
	 * 
	 * @param code
	 * @param body
	 */
	public Response(Integer tag, Integer code, String body) {
		super(tag);
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

	@Override
	public void serialize(DataOutputStream out) throws IOException {
		super.serialize(out);
		out.writeInt(respCode);
		out.writeUTF(respBody);
	}

	public static Response deserialize(DataInputStream in) throws IOException {
		int tag = in.readInt();
		int rcode = in.readInt();
		String rBody = in.readUTF();
		return new Response(tag, rcode, rBody);
	}
}