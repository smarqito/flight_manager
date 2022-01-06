package g12.Middleware.DTO.ResponseDTO;

import java.io.DataOutputStream;
import java.io.IOException;

import g12.Middleware.DTO.*;

public abstract class ResponseDTO extends DTO {

	private Integer respCode;

	public ResponseDTO(Integer respCode) {
		this.respCode = respCode;
	}

	public Integer getRespCode() {
		return this.respCode;
	}

	public void setRespCode(Integer respCode) {
		this.respCode = respCode;
	}

	@Override
	public void serialize(DataOutputStream out) throws IOException {
		out.writeInt(respCode);
	}

	/**
	 * int tag
	 * respCode
	 */
}