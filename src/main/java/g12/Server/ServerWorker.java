package g12.Server;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import g12.Middleware.*;
import g12.Server.FlightManager.*;
import g12.Server.FlightManager.Exceptions.DiaFechado;
import g12.Server.FlightManager.Exceptions.LoginInvalido;
import g12.Server.FlightManager.Exceptions.NotAllowed;
import g12.Server.FlightManager.Exceptions.ReservaNaoExiste;
import g12.Server.FlightManager.Exceptions.UserIsNotClient;
import g12.Server.FlightManager.Exceptions.UserJaExisteException;
import g12.Server.FlightManager.Exceptions.UserNaoExistente;

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
						System.out.println(frame.toString());
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
			Response r;
			if (method.equals("login")) {
				r = loginHandler(q);
			} else if (method.equals("registerUser")) {
				r = registerUser(q);
			} else {
				try {
					String user = checkToken(q.getToken());
					switch (method) {
						case "registerFlight":
							r = registerFlight(q);
							break;
						case "closeDay":
							r = closeDay(user, q);
							break;
						case "bookFlight":
							r = bookFlight(user, q);
							break;
						case "cancelBook":
							r = cancelBook(user, q);
							break;
						case "availableFlights":
							r = availableFlights(q);
							break;
						default:
							// responder pedido mal feito;
							// erro 400
							r = new Response(q.tag, 400, "Pedido nao existe");
							break;
					}
				} catch (TokenInvalido e) {
					// handle wrong token
					r = new Response(q.tag, 401, e.getMessage());
				}
			}
			c.send(r);
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

	public Response registerUser(Query q) throws BadRequest {
		Params p = q.getParams();
		if (p.size() != 2) {
			throw new BadRequest("Pedido mal construido, parametros nao correspondem");
		}
		try {
			this.model.registerUser(p.get(0), p.get(1));
			return new Response(q.tag, 200, "Registo efetuado com sucesso!");
		} catch (UserJaExisteException e) {
			return new Response(q.tag, 400, "Ja existe um utilizador com o mesmo nome");
		}
	}

	public Response registerFlight(Query q) throws BadRequest {
		Params p = q.getParams();
		if (p.size() != 3) {
			throw new BadRequest("Pedido mal construido, parametros nao correspondem");
		}
		try {
			this.model.registerFlight(p.get(0), p.get(1), Integer.parseInt(p.get(2)));
			return new Response(q.tag, 200, "Registo efetuado com sucesso!");
		} catch (NumberFormatException e) {
			throw new BadRequest("Verifique os parametros inseridos!");
		}

	}

	public Response closeDay(String user, Query q) throws BadRequest {
		Params p = q.getParams();
		if (p.size() != 0) {
			throw new BadRequest("Pedido mal construido, parametros nao correspondem");
		}
		try {
			this.model.closeDay(user);
			return new Response(q.tag, 200, "Dia encerrado com sucesso!");
		} catch (UserNaoExistente | NotAllowed e) {
			return new Response(q.tag, 400, "Nao tem permissoes para encerrar o dia!");
		} catch (DiaFechado e) {
			return new Response(q.tag, 400, "Nao tem permissoes para encerrar o dia!");
		}
	}

	public Response bookFlight(String user, Query q) throws BadRequest {
		// pelo menos 4 parametros
		// 1o ate n-2 -> percurso
		// n-1 e n -> de; ate
		Params p = q.getParams();
		int size = p.size();
		if (size < 4) {
			throw new BadRequest("Pedido mal construido, parametros nao correspondem");
		}
		List<String> perc = new ArrayList<>(size - 2);
		for (int i = 0; i < size - 2; i++) {
			perc.add(p.get(i));
		}
		try {
			String bookId = this.model.bookFlight(user, perc, LocalDate.parse(p.get(size - 2)),
					LocalDate.parse(p.get(size - 1)));
			return new Response(q.tag, 200, bookId);
		} catch (UserIsNotClient | UserNaoExistente e) {
			return new Response(q.tag, 404, "Utilizador nao existe ou nao é cliente!");
		}
	}

	public Response cancelBook(String user, Query q) throws BadRequest {
		Params p = q.getParams();
		int size = p.size();
		if (size != 1) {
			throw new BadRequest("Pedido mal construido, parametros nao correspondem");
		}
		try {
			if (this.model.cancelBook(user, p.get(0))) {
				return new Response(q.tag, 200, "Cancelamento com sucesso");
			}
			return new Response(q.tag, 300, "Cancelamento nao sucedido");
		} catch (UserNaoExistente | UserIsNotClient e) {
			return new Response(q.tag, 404, "Utilizador nao existe ou nao e cliente");
		} catch (ReservaNaoExiste e) {
			return new Response(q.tag, 404, "Utilizador nao existe ou nao e cliente");
		}

	}

	public Response availableFlights(Query q) throws BadRequest {
		Params p = q.getParams();
		int size = p.size();
		if (size != 0) {
			throw new BadRequest("Pedido mal construido, parametros nao correspondem");
		}
		this.model.availableFlights();
		// TODO
		return new Response(q.tag, 200, "login sucesso");

	}

}