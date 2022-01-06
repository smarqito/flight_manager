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
	/**
	 * Inicio [sem login]:
	 * 1. login
	 * 2. registar
	 * 
	 * 1: Login
	 * 1.1. Pedir user
	 * 1.2 Pedir pass
	 * 
	 * 2: Registar
	 * 2.1 Pedir user
	 * 2.2 Pedir pass
	 * 
	 * Homepage [com login]:
	 * 1. Cliente
	 * 2. Admin
	 * 
	 * 1. Cliente : Homepage
	 * 1.1 Reservar Voo
	 * 1.2 Cancelar Voo
	 * 
	 * 1.1. : Reservar Voo
	 * 1.1.1 Ver voos disponiveis
	 * 1.1.2 Fazer reserva
	 * 
	 * 1.1.2 : Fazer reserva
	 * 1.1.2.1 Pedir percurso, separados por virgula
	 * 1.1.2.2 Pedir data minima
	 * 1.1.2.3 Pedir data maxima
	 * 
	 * 2. Admin : Homepage
	 * 2.1 Registar Voo
	 * 2.2 Encerrar dia
	 * 
	 * 2.1 : Registar Voo
	 * 2.1.1 Pedir origem
	 * 2.1.2 Pedir destino
	 * 2.1.3 Pedir capacidade
	 */

}