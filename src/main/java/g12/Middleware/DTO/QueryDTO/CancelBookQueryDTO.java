package g12.Middleware.DTO.QueryDTO;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class CancelBookQueryDTO extends QueryDTO {
	private String bookId;

	public CancelBookQueryDTO(String bookId) {
		this.bookId = bookId;
	}

	public CancelBookQueryDTO(String token, String bookId) {
		super(token);
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

	public static CancelBookQueryDTO deserialize(DataInputStream in) throws IOException {
		String token = in.readUTF();
		String bookid = in.readUTF();
		return new CancelBookQueryDTO(token, bookid);
	}

}