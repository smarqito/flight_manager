package g12.Server;

import java.io.IOException;

import g12.Middleware.*;
import g12.Server.FlightManager.*;
import g12.Server.FlightManager.Exceptions.LoginInvalido;

public class ServerWorker implements Runnable {

	private final ServerConnection c;
	private final IFlightManager model;

	/**
	 * 
	 * @param c
	 * @param model
	 */
	public ServerWorker(ServerConnection c, IFlightManager model) {
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
				try {
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
				} catch (TokenInvalido e) {
					// handle wrong token
					c.send(new Response(q.tag, 401, e.getMessage()));
				}
			}
		} catch (BadRequest br) {
			// code 400
		} catch (IOException io) {
			System.out.println(io.toString());
		}
	}

	public String checkToken(String token) throws TokenInvalido {
		return model.verifyToken(token);
	}

	public Response loginHandler(Query q) throws BadRequest {
		Params p = q.getParams();
		if (p.size() != 2) {
			throw new BadRequest("Pedido mal construido, parametros nao correspondem");
		}
		Response r;
		try {
			String token = model.login(p.get(0), p.get(1));
			r = new Response(q.tag, 200, token);
		} catch (LoginInvalido e) {
			r = new Response(q.tag, 403, e.getMessage());
		}
		return r;
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