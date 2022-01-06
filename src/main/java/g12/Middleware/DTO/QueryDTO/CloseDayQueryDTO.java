package g12.Middleware.DTO.QueryDTO;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class CloseDayQueryDTO extends QueryDTO {
    public CloseDayQueryDTO() {
    }

    public CloseDayQueryDTO(String token) {
        super(token);
    }

    @Override
    public void serialize(DataOutputStream out) throws IOException {
        super.serialize(out);
    }

    public static CloseDayQueryDTO deserialize(DataInputStream in) throws IOException {
        String token = in.readUTF();
        return new CloseDayQueryDTO(token);
    }

}