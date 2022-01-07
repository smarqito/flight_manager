package g12.Server;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.Map.entry;

import g12.Middleware.*;
import g12.Middleware.DTO.DTO;
import g12.Middleware.DTO.ExceptionDTO.RequestExceptionDTO;
import g12.Middleware.DTO.QueryDTO.AvailableFlightsQueryDTO;
import g12.Middleware.DTO.QueryDTO.BookFlightQueryDTO;
import g12.Middleware.DTO.QueryDTO.CancelBookQueryDTO;
import g12.Middleware.DTO.QueryDTO.CloseDayQueryDTO;
import g12.Middleware.DTO.QueryDTO.LoginQueryDTO;
import g12.Middleware.DTO.QueryDTO.QueryDTO;
import g12.Middleware.DTO.QueryDTO.RegisterFlightQueryDTO;
import g12.Middleware.DTO.QueryDTO.RegisterUserQueryDTO;
import g12.Middleware.DTO.ResponseDTO.AvailableFlightsDTO;
import g12.Middleware.DTO.ResponseDTO.BookFlightDTO;
import g12.Middleware.DTO.ResponseDTO.LoginDTO;
import g12.Middleware.DTO.ResponseDTO.UnitDTO;
import g12.Server.FlightManager.*;
import g12.Server.FlightManager.BookingManager.Voo;
import g12.Server.FlightManager.Exceptions.BookingDayJaExiste;
import g12.Server.FlightManager.Exceptions.BookingDayNaoExistente;
import g12.Server.FlightManager.Exceptions.DiaFechado;
import g12.Server.FlightManager.Exceptions.LoginInvalido;
import g12.Server.FlightManager.Exceptions.NotAllowed;
import g12.Server.FlightManager.Exceptions.PercusoNaoDisponivel;
import g12.Server.FlightManager.Exceptions.ReservaIndisponivel;
import g12.Server.FlightManager.Exceptions.ReservaNaoExiste;
import g12.Server.FlightManager.Exceptions.UserIsNotClient;
import g12.Server.FlightManager.Exceptions.UserJaExisteException;
import g12.Server.FlightManager.Exceptions.UserNaoExistente;
import g12.Server.FlightManager.Exceptions.VooJaExiste;
import g12.Server.FlightManager.Exceptions.VooNaoExistente;

public class ServerWorker implements Runnable {

	private final ServerConnection c;
	private final IFlightManager model;
	private Map<String, Function<QueryDTO, DTO>> mapping = Map.ofEntries(
			entry(LoginQueryDTO.class.getSimpleName(), (x) -> this.loginHandler(x)),
			entry(RegisterUserQueryDTO.class.getSimpleName(), (x) -> this.registerUser(x)),
			entry(RegisterFlightQueryDTO.class.getSimpleName(), (x) -> this.registerFlight(x)),
			entry(CloseDayQueryDTO.class.getSimpleName(), (x) -> this.closeDay(x)),
			entry(BookFlightQueryDTO.class.getSimpleName(), (x) -> this.bookFlight(x)),
			entry(CancelBookQueryDTO.class.getSimpleName(), (x) -> this.cancelBook(x)),
			entry(AvailableFlightsQueryDTO.class.getSimpleName(), (x) -> this.availableFlights(x)));

	private Function<QueryDTO, DTO> getMapping(String m) {
		return mapping.get(m);
	}

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
		int tag = 0;
		try {
			try (c) {
				for (;;) {
					try {
						try {
							Frame frame = this.c.receive();
							tag = frame.tag;
							System.out.println(frame.toString());
							this.requestHandler(frame);
						} catch (IOException ignored) {
							this.c.send(new Frame(tag, new RequestExceptionDTO(ignored.getMessage())));
						}
					} catch (Exception alsoignored) {
						this.c.send(new Frame(tag, new RequestExceptionDTO(alsoignored.getMessage())));
					}
				}
			}
		} catch (Exception e) {
		}
	}

	public void requestHandler(Frame f) throws IOException {
		QueryDTO dto = (QueryDTO) f.getDto();
		String method = dto.getClass().getSimpleName();
		DTO r = getMapping(method).apply(dto);

		this.c.send(new Frame(f.tag, r));
	}

	public String checkToken(String token) throws TokenInvalido {
		return model.verifyToken(token);
	}

	public DTO loginHandler(QueryDTO dto) {
		LoginQueryDTO q = (LoginQueryDTO) dto;
		DTO r;
		try {
			String token = model.login(q.getUser(), q.getPass());
			r = new LoginDTO(200, token);
		} catch (LoginInvalido e) {
			r = new LoginDTO(403, "");
		}
		return r;
	}

	public DTO registerUser(QueryDTO dto) {
		RegisterUserQueryDTO q = (RegisterUserQueryDTO) dto;
		try {
			this.model.registerUser(q.getUser(), q.getPass());
			return new UnitDTO(200);
		} catch (UserJaExisteException e) {
			return new UnitDTO(400);
		}
	}

	public DTO registerFlight(QueryDTO dto) {
		RegisterFlightQueryDTO q = (RegisterFlightQueryDTO) dto;
		this.model.registerFlight(q.getOrigem(), q.getDest(), q.getCapacidade());
		return new UnitDTO(200);

	}

	public DTO closeDay(QueryDTO dto) {
		try {
			String user = checkToken(dto.getToken());
			this.model.closeDay(user);
			return new UnitDTO(200);
		} catch (UserNaoExistente | NotAllowed e) {
			return new UnitDTO(400);
		} catch (DiaFechado e) {
			return new UnitDTO(401);
		} catch (TokenInvalido e) {
			return new UnitDTO(401);
		} catch (BookingDayNaoExistente e) {
			return new UnitDTO(402);
		}
	}

	public DTO bookFlight(QueryDTO dto) {
		// pelo menos 4 parametros
		// 1o ate n-2 -> percurso
		// n-1 e n -> de; ate
		BookFlightQueryDTO q = (BookFlightQueryDTO) dto;
		try {
			String user = checkToken(dto.getToken());
			String bookId = this.model.bookFlight(user, q.getPercurso(), q.getDe(), q.getAte());
			return new BookFlightDTO(200, bookId);
		} catch (UserIsNotClient | UserNaoExistente | BookingDayJaExiste | VooJaExiste | BookingDayNaoExistente
				| ReservaIndisponivel | DiaFechado e) {
			return new UnitDTO(404);
		} catch (TokenInvalido e) {
			return new UnitDTO(401);
		} catch (VooNaoExistente e) {
			return new UnitDTO(402);
		} catch (PercusoNaoDisponivel e) {
			return new UnitDTO(403);
		}
	}

	public DTO cancelBook(QueryDTO dto) {
		CancelBookQueryDTO q = (CancelBookQueryDTO) dto;
		try {
			String user = checkToken(dto.getToken());
			if (this.model.cancelBook(user, q.getBookId())) {
				return new UnitDTO(200);
			}
			return new UnitDTO(300);
		} catch (UserNaoExistente | UserIsNotClient | VooNaoExistente | BookingDayNaoExistente e) {
			return new UnitDTO(404);
		} catch (ReservaNaoExiste e) {
			return new UnitDTO(404);
		} catch (TokenInvalido e) {
			return new UnitDTO(404);
		}
	}

	public DTO availableFlights(QueryDTO dto) {
		List<Voo> voos = this.model.availableFlights();
		// TODO
		return new AvailableFlightsDTO(200);
		// TODO
	}

}