package g12.Server.FlightManager.BookingManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Voos extends ArrayList<InfoVoo> {

    public void serialize(DataOutputStream out) throws IOException {
        out.writeUTF("Reserva constituida por: " + this.size());
        for (InfoVoo voo : this) {
            out.writeUTF("ID: " + voo.id.toString());
            out.writeUTF("Origem: " + voo.origem);
            out.writeUTF("Destino: " + voo.destino);
        }
    }

    public static Voos deserialize(DataInputStream in) throws IOException {
        throw new IOException();
    }
}
