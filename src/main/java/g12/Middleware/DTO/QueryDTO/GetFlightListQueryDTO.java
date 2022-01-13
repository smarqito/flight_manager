package g12.Middleware.DTO.QueryDTO;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class GetFlightListQueryDTO extends QueryDTO{
    private String origem;
    private String dest;

    public GetFlightListQueryDTO(String token){
        super(token);
    }
    public GetFlightListQueryDTO(String origem, String dest) {
        this.origem = origem;
        this.dest = dest;
    }

    public GetFlightListQueryDTO(String token, String origem, String dest) {
        super(token);
        this.origem = origem;
        this.dest = dest;
    }

    public String getOrigem() {
        return origem;
    }

    public String getDest() {
        return dest;
    }

    @Override
    public void serialize(DataOutputStream out) throws IOException {
        super.serialize(out);
        out.writeUTF(origem);
        out.writeUTF(dest);
    }

    public static GetFlightListQueryDTO deserialize(DataInputStream in) throws IOException {
        String token = in.readUTF();
        String origem = in.readUTF();
        String dest = in.readUTF();
        return new GetFlightListQueryDTO(token, origem, dest);
    }
}
