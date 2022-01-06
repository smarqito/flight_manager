package g12.Middleware.DTO.ResponseDTO;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class LoginDTO extends ResponseDTO {
    private String token;

    public LoginDTO(Integer respCode) {
        super(respCode);
    }

    public LoginDTO(Integer respCode, String token) {
        super(respCode);
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public void serialize(DataOutputStream out) throws IOException {
        super.serialize(out);
        out.writeUTF(token);
    }

    /**
     * int tag
     * respCode
     * 
     * @throws IOException
     */
    public static LoginDTO deserialize(DataInputStream in) throws IOException {
        int respCode = in.readInt();
        String token = in.readUTF();
        return new LoginDTO(respCode, token);
    }
}