package g12.Server;

import java.io.IOException;
import java.util.Scanner;

import g12.Server.FlightManager.FlightManagerFacade;
import g12.Server.FlightManager.IFlightManager;
import g12.Server.Logger.Logger;

public class ServerApp {

    /**
     * 
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        IFlightManager model = FlightManagerFacade.getState();
        Logger.InitLogger();
        Server server = new Server(model);
        Thread t = new Thread(server);
        t.start();
        Scanner s = new Scanner(System.in);
        while (true) {
            String r = s.nextLine();
            if (r.equals("exit")) {
                // colocar o Server Socket a nao receber mais ligacoes
                server.stopServer();
                t.interrupt();
                // verificar se o model ainda tem operacoes pendentes
                s.close();
                model.saveState();
                break;
            }
        }
    }
}
