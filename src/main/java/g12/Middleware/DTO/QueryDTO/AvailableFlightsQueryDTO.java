package g12.Middleware.DTO.QueryDTO;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class AvailableFlightsQueryDTO extends QueryDTO {

    public AvailableFlightsQueryDTO() {
    }

    public AvailableFlightsQueryDTO(String token) {
        super(token);
    }

    @Override
    public void serialize(DataOutputStream out) throws IOException {
        super.serialize(out);
    }

    public static AvailableFlightsQueryDTO deserialize(DataInputStream in) throws IOException {
        String token = in.readUTF();
        return new AvailableFlightsQueryDTO(token);
    }
}