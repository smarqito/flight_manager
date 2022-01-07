package g12.Middleware.DTO.ResponseDTO;

import g12.Middleware.DTO.VooDTO;
import g12.Middleware.DTO.VoosDTO;
import g12.Server.FlightManager.BookingManager.Voo;
import g12.Server.FlightManager.BookingManager.Voos;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AvailableFlightsDTO extends ResponseDTO {
    private VoosDTO voos;

    // TODO
    public AvailableFlightsDTO(Integer respCode) {
        super(respCode);
    }

    public AvailableFlightsDTO(Integer respCode, VoosDTO voos){
        super(respCode);
        this.voos = voos;
    }

    public VoosDTO getVoos() {
        return voos;
    }

    @Override
    public void serialize(DataOutputStream out) throws IOException {
        super.serialize(out);
        this.voos.serialize(out);
    }

    public static AvailableFlightsDTO deserialize(DataInputStream in) throws IOException {
        int respCode = in.readInt();
        VoosDTO voos = VoosDTO.deserialize(in);
        return new AvailableFlightsDTO(respCode, voos);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Voos Disponíveis:\n");
        for (VooDTO v : this.voos){
            sb.append("Origem: " + v.getOrigem() + ";\n");
            sb.append("Destino: " + v.getDestino() + ";\n");
        }
        return sb.toString();
    }
}