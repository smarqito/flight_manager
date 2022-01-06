package g12.Middleware;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import g12.Middleware.DTO.DTO;

public class Frame {

    public final int tag;
    private final String className;
    private DTO dto;

    public Frame(int tag, String className) {
        this.tag = tag;
        this.className = className;
    }

    public DTO getDto() {
        return dto;
    }

    public void setDto(DTO dto) {
        this.dto = dto;
    }

    public String getClassName() {
        return className;
    }

    public void serialize(DataOutputStream out) throws IOException {
        out.writeUTF(className);
        out.writeInt(tag);
    }

    public static Frame deserialize(DataInputStream in) throws IOException {
        String cName = in.readUTF();
        int tag = in.readInt();
        return new Frame(tag, cName);
    }
}