package g12.Middleware.DTO;

import g12.Middleware.DTO.ResponseDTO.AvailableFlightsDTO;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class VooDTO {
    private String origem;
    private String destino;

    public VooDTO(String origem, String destino){
        this.origem = origem;
        this.destino = destino;
    }

    public String getOrigem() {
        return origem;
    }

    public String getDestino() {
        return destino;
    }

    public void serialize(DataOutputStream out) throws IOException {
        out.writeUTF(this.getOrigem());
        out.writeUTF(this.getDestino());
    }

    public static VooDTO deserialize(DataInputStream in) throws IOException {
        String origem = in.readUTF();
        String destino = in.readUTF();
        return new VooDTO(origem, destino);
    }
}
