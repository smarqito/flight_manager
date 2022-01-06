package g12.Middleware.DTO.ResponseDTO;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class BookFlightDTO extends ResponseDTO {
    private String bookId;

    public BookFlightDTO(Integer respCode) {
        super(respCode);
    }

    public BookFlightDTO(Integer respCode, String bookId) {
        super(respCode);
        this.bookId = bookId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    @Override
    public void serialize(DataOutputStream out) throws IOException {
        super.serialize(out);
        out.writeUTF(bookId);
    }

    public static BookFlightDTO deserialize(DataInputStream in) throws IOException {
        int respCode = in.readInt();
        String bookid = in.readUTF();
        return new BookFlightDTO(respCode, bookid);
    }
}