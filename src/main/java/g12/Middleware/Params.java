package g12.Middleware;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Params extends ArrayList<String> {
    public Params(int initialCapacity) {
        super(initialCapacity);
    }

    public void addAll(String[] lst) {
        for(String s : lst) {
            this.add(s);
        }
    }
    public void serialize(DataOutputStream out) throws IOException {
        out.writeInt(this.size());
        for (String string : this) {
            out.writeUTF(string);
        }
    }

    public static Params deserialize(DataInputStream in) throws IOException {
        int size = in.readInt();
        Params p = new Params(size);
        for (int i = 0; i < size; i++) {
            p.add(in.readUTF());
        }
        return p;
    }
}
