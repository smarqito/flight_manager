package g12.Server;

import java.io.IOException;

import g12.Middleware.*;
import g12.Server.FlightManager.*;

public class ServerWorker implements Runnable {

	private final TaggedConnection c;
	private final IFlightManager model;

	/**
	 * 
	 * @param c
	 * @param model
	 */
	public ServerWorker(TaggedConnection c, IFlightManager model) {
		this.model = model;
		this.c = c;
	}

	@Override
	public void run() {
		try {
			try (c) {
				try {
					for (;;) {
						Query frame = (Query) this.c.receive();
						this.requestHandler(frame);
					}
				} catch (IOException ignored) {
				}
			}
		} catch (Exception alsoignored) {
			// faz nada
		}
	}

	public void requestHandler(Query q) {
		String method = q.getMethod();
		try {
			if (method.equals("login")) {
				loginHandler(q);
			} else {
				checkToken(q.getToken());
				switch (q.getMethod()) {
					case "login":
						break;
					case "registerUser":
						registerUser(q);
						break;
					case "registerFlight":
						registerFlight(q);
						break;
					case "closeDay":
						closeDay(q);
						break;
					case "bookFlight":
						bookFlight(q);
						break;
					case "cancelBook":
						cancelBook(q);
						break;
					case "availableFlights":
						availableFlights(q);
						break;
					default:
						// responder pedido mal feito;
						// erro 400
						break;
				}
			}
		} catch (BadRequest br) {
			// code 400
		}
	}

	public void checkToken(String token) {
		// add verify token to flight Manager
		// resp code 401 Unauthorized
	}

	public void loginHandler(Query q) throws BadRequest {
		Params p = q.getParams();
		if (p.size() != 2) {
			throw new BadRequest("Pedido mal construido, parametros nao correspondem");
		}
		Response r = new Response(q.tag, 200, "login sucesso");
		// model.login(p.get(0), p.get(1)); // colocar o login a retornar uma string que
		// sera o token
	}

	public void registerUser(Query q) {

		Response r = new Response(q.tag, 200, "login sucesso");
	}

	public void registerFlight(Query q) {
		Response r = new Response(q.tag, 200, "login sucesso");

	}

	public void closeDay(Query q) {
		Response r = new Response(q.tag, 200, "login sucesso");

	}

	public void bookFlight(Query q) {
		// pelo menos 4 parametros
		// 1o ate n-2 -> percurso
		// n-1 e n -> de; ate
		Response r = new Response(q.tag, 200, "login sucesso");
	}

	public void cancelBook(Query q) {
		Response r = new Response(q.tag, 200, "login sucesso");

	}

	public void availableFlights(Query q) {
		Response r = new Response(q.tag, 200, "login sucesso");

	}

}