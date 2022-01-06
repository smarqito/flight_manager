package g12.Middleware.DTO.QueryDTO;

import java.io.DataOutputStream;
import java.io.IOException;

import g12.Middleware.DTO.DTO;

public abstract class QueryDTO extends DTO {
    private String token;

    public QueryDTO() {
    }

    public QueryDTO(String token) {
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
        out.writeUTF(token);
    }
    
}
