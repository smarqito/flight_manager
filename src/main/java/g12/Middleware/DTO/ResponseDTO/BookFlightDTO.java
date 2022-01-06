package g12.Middleware.DTO.ResponseDTO;

import java.io.DataOutputStream;
import java.io.IOException;

public class BookFlightDTO extends ResponseDTO {
    private String bookId;

    public BookFlightDTO(int tag, Integer respCode) {
        super(tag, BookFlightDTO.class.getSimpleName(), respCode);
    }

    @Override
    public void serialize(DataOutputStream out) throws IOException {
        super.serialize(out);
        out.writeUTF(bookId);
    }
}