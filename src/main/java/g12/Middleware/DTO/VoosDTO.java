package g12.Middleware.DTO;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class VoosDTO extends ArrayList<VooDTO> {

    public VoosDTO() {
    }

    public VoosDTO(int initialCapacity) {
        super(initialCapacity);
    }

    public void serialize(DataOutputStream out) throws IOException {
        out.writeInt(this.size());
        for (int i = 0; i < this.size(); i++) {
            this.get(i).serialize(out);
        }
    }

    public static VoosDTO deserialize(DataInputStream in) throws IOException {
        int size = in.readInt();
        VoosDTO voos = new VoosDTO(size);
        for (int i = 0; i < size; i++) {
            voos.add(VooDTO.deserialize(in));
        }
        return voos;
    }

}
