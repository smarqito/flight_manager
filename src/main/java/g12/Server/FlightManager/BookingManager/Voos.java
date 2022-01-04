package g12.Server.FlightManager.BookingManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Voos extends ArrayList<InfoVoo> {

    public void serialize(DataOutputStream out) throws IOException {
        
    }

    public static Voos deserialize(DataInputStream in) throws IOException {
        throw new IOException();
    }
}
