package g12.Middleware.DTO.ResponseDTO;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * Representa o retorno de um boolean
 * closeDay
 * cancelBook
 */
public class UnitDTO extends ResponseDTO {

    public UnitDTO(Integer respCode) {
        super(respCode);
    }

    public static UnitDTO deserialize(DataInputStream in) throws IOException {
        int rcode = in.readInt();
        return new UnitDTO(rcode);
    }
}