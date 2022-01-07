package g12.Middleware.DTO.ExceptionDTO;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import g12.Middleware.DTO.DTO;

public class RequestExceptionDTO extends DTO {
    private String message;

    public RequestExceptionDTO(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void serialize(DataOutputStream out) throws IOException {
        out.writeUTF(message);
    }

    public static RequestExceptionDTO deserialize(DataInputStream in) throws IOException {
        return new RequestExceptionDTO(in.readUTF());
    }

}
