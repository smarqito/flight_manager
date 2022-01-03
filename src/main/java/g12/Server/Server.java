package g12.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import g12.Middleware.TaggedConnection;
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
		IFlightManager model = new FlightManagerFacade();
		try {
			IFlightManager.class.getMethod("login", String.class, String.class);
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (true) {
			Socket s = ss.accept();
			TaggedConnection tg = new TaggedConnection(s);
			new Thread(new ServerWorker(tg, model)).start();
		}
	}

}