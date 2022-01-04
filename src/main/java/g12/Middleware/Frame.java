package g12.Middleware;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Frame {

    public final int tag;

    public Frame(int tag) {
        this.tag = tag;
    }

    public void serialize(DataOutputStream out) throws IOException {
        out.writeInt(tag);
    }

    public static Frame deserialize(DataInputStream in) throws IOException {
        int tag = in.readInt();
        return new Frame(tag);
    }
}