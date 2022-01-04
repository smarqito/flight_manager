package g12.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import g12.Middleware.ServerConnection;
import g12.Server.FlightManager.FlightManagerFacade;
import g12.Server.FlightManager.IFlightManager;

public class Server {
	/**
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		ServerSocket ss = new ServerSocket(4444);
		try (ss) {
			IFlightManager model = new FlightManagerFacade();
			while (true) {
				Socket s = ss.accept();
				ServerConnection tg = new ServerConnection(s);
				new Thread(new ServerWorker(tg, model)).start();
			}
		}
	}

}