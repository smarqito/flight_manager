package g12.Client;

import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import g12.Client.UI.ClientUI;
import g12.Middleware.BadRequest;
import g12.Middleware.Demultiplexer;
import g12.Middleware.QueryThread;
import g12.Middleware.StillWaitingException;
import g12.Middleware.DTO.DTO;
import g12.Middleware.DTO.ExceptionDTO.RequestExceptionDTO;
import g12.Middleware.DTO.ResponseDTO.LoginDTO;

public class Client {

	/**
	 * Apenas uma thread vai aceder à tag.
	 * Por esse motivo, não se implementa Lock
	 */
	private int tag = 0;

	private Demultiplexer c;
	public final ClientManager cm = new ClientManager();

	public int getTag() {
		return tag++;
	}

	public Client(Demultiplexer c) {
		this.c = c;
	}

	public DTO queryHandler(DTO dto) throws IOException, BadRequest {
		QueryThread query = asyncHandler(dto);
		DTO respDTO = query.result();
		try {
			respDTO = query.getResponse();
			if (respDTO.getClass().getSimpleName().equals(RequestExceptionDTO.class.getSimpleName())) {
				RequestExceptionDTO rq = (RequestExceptionDTO) respDTO;
				throw new BadRequest(rq.getMessage());
			}
		} catch (StillWaitingException e) {
			// nao vai acontecer pq faz join previamente ate a thread terminar (receber
			// pacote)
		}
		return respDTO;
	}

	public LoginDTO loginHandler(DTO dto) throws IOException, BadRequest {
		LoginDTO r = (LoginDTO) queryHandler(dto);
		if (r.getRespCode().equals(200)) {
			this.c.setToken(r.getToken());
		}
		return r;
	}

	/**
	 * Efetua o envio do DTO, não ficando a espera do mesmo.
	 * 
	 * @param dto DTO a enviar
	 * @return Número do pedido que foi criado, para que a instância que chamou
	 *         possa fazer wait quando pretender
	 * @throws IOException
	 * @throws BadRequest
	 */
	public QueryThread asyncHandler(DTO dto) throws IOException {
		int tag = this.getTag();
		QueryThread query = new QueryThread(dto, tag, c);
		this.cm.addThread(query);
		query.start();
		return query;
	}

	public void clientRunnable() throws IOException {
		ClientUI cUi = new ClientUI(this);
		this.c.start(); // inicia receive socket
		cUi.run();
		this.c.close();
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
	 * 1.1.3 Ver reservas pendentes
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