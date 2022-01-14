package g12.Client;

import java.io.IOException;
import java.net.Socket;

import g12.Middleware.Demultiplexer;

public class ClientApp {
    
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		if(args.length != 1) {
			System.out.println("Client <ip>");
		} else {
			try {
				Socket s = new Socket(args[0], 4444);
				new Client(new Demultiplexer(s)).clientRunnable();
			} catch (IOException e) {
				System.out.println("Nao foi possivel estabelecer ligacao!");
			}
		}
	}
}
