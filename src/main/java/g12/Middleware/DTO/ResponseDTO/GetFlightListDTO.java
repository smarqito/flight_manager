package g12.Middleware.DTO.ResponseDTO;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Map<String, Integer> mapa = new HashMap<>();
        int count = 0;
        out.writeInt(voos.size());
        for (List<String> lista : voos){
            out.writeInt(lista.size());
            for (String voo : lista){
                Integer v = mapa.get(voo);
                if (v == null){
                    mapa.put(voo, count++);
                    out.writeInt(-1);
                    out.writeUTF(voo);
                }else{
                    out.writeInt(v);
                }
            }
        }
    }

    public static GetFlightListDTO deserialize(DataInputStream in) throws IOException {
        int respCode = in.readInt();
        int sizeList = in.readInt();
        List<List<String>> voos = new ArrayList<>(sizeList);
        Map<Integer, String> mapa = new HashMap<>();
        int count = 0;
        for (int i = 0; i < sizeList; i++){
            int size = in.readInt();
            List<String> voos2 = new ArrayList<>(size);
            for (int j = 0; j < size; j++){
                int n = in.readInt();
                if (n == -1){
                    String v = in.readUTF();
                    mapa.put(count++, v);
                    voos2.add(v);
                } else {
                    String v = mapa.get(n);
                    voos2.add(v);
                }
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
