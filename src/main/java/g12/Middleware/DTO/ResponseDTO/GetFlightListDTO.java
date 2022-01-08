package g12.Middleware.DTO.ResponseDTO;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetFlightListDTO extends ResponseDTO{
    private List<List<String>> voos;

    public GetFlightListDTO(Integer respCode) {
        super(respCode);
    }

    public GetFlightListDTO(Integer respCode, List<List<String>> voos){
        super(respCode);
        this.voos = voos;
    }

    @Override
    public void serialize(DataOutputStream out) throws IOException {
        super.serialize(out);
        out.writeInt(voos.size());
        for (List<String> lista : voos){
            out.writeInt(lista.size());
            for (String voo : lista){
                out.writeUTF(voo);
            }
        }
    }

    public static GetFlightListDTO deserialize(DataInputStream in) throws IOException {
        int respCode = in.readInt();
        int sizeList = in.readInt();
        List<List<String>> voos = new ArrayList<>(sizeList);
        for (int i = 0; i < sizeList; i++){
            int size = in.readInt();
            List<String> voos2 = new ArrayList<>(size);
            for (int j = 0; j < size; j++){
                String v = in.readUTF();
                voos2.add(v);
            }
            voos.add(voos2);
        }
        return new GetFlightListDTO(respCode, voos);
    }

    @Override
    public String toString() {
        return "voos=" + voos;
    }
}
