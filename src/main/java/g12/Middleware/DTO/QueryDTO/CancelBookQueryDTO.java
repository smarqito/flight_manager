package g12.Middleware.DTO.QueryDTO;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class CancelBookQueryDTO extends QueryDTO {
	private String bookId;

	public CancelBookQueryDTO(int tag) {
		super(tag, CancelBookQueryDTO.class.getSimpleName());
	}

	public CancelBookQueryDTO(int tag, String bookId) {
		this(tag);
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
		int tag = in.readInt();
		String bookid = in.readUTF();
		return new CancelBookQueryDTO(tag, bookid);
	}

}