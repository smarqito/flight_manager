package g12.Server.Persistence;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import g12.Server.FlightManager.FlightManagerFacade;
import g12.Server.FlightManager.IFlightManager;

public class FlightData {
    public static IFlightManager getState() throws FileNotFoundException, IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("flight_manager.obj"));
        IFlightManager flm = (FlightManagerFacade) ois.readObject();
        ois.close();
        return flm;
    }

    public static void saveState(IFlightManager fm) throws FileNotFoundException, IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("flight_manager.obj"));
        oos.writeObject(fm);
        oos.close();
    }
}
