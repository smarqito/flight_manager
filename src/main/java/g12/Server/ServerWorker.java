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
						TaggedConnection.Frame frame = this.c.receive();
						this.requestHandler(frame);
					}
				} catch (IOException ignored) {
				}
			}
		} catch (Exception alsoignored) {
			// faz nada
		}
	}

	public void requestHandler(TaggedConnection.Frame f) {
		Query q = new Query("method", 0);
		switch (q.getMethod()) {
			case "login":
				break;
			case "registerUser":
				break;
			case "registerFlight":
				break;
			case "closeDay":
				break;
			case "bookFlight":
				break;
			case "cancelBook":
				break;
			case "availableFlights":
				break;
			default:
				// responder pedido mal feito;
				// erro 400
				break;
		}
	}

	public void checkToken(String token) {
		// add verify token to flight Manager
	}

	public void loginHandler(Query q) throws BadRequest {
		Params p = q.getParams();
		if(p.size() == 2) {
			throw new BadRequest("Pedido mal construido, parametros nao correspondem");
		}
		model.login(p.get(0), p.get(1)); // colocar o login a retornar uma string que sera o token
	}

	public void registerUser(Query q) {

	}

	public void registerFlight(Query q) {

	}

	public void closeDay(Query q) {

	}

	public void bookFlight(Query q) {

	}

	public void cancelBook(Query q) {

	}

	public void availableFlights(Query q) {

	}

}