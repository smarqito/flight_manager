package g12.Client;

import java.io.IOException;
import java.net.Socket;

import g12.Middleware.ClientConnection;
import g12.Middleware.Query;
import g12.Middleware.Response;

public class Client {

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Socket s = new Socket("localhost", 4444);
			ClientConnection c = new ClientConnection(s);
			Query q = new Query(1, "", "login", 3);
			q.addParam("marcoUser");
			q.addParam("marcoPass");
			c.send(q);
			try (c) {
				Response r = (Response) c.receive();
				System.out.println(r.tag + ": " + r.getRespCode() + r.getRespBody());
			}
		} catch (IOException e) {
			System.out.println("Nao foi possivel estabelecer ligacao!");
			e.printStackTrace();
		}
	}

}