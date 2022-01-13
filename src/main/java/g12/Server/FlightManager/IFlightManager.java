package g12.Server.FlightManager;

import java.time.LocalDate;
import java.util.List;

import g12.Middleware.TokenInvalido;
import g12.Server.FlightManager.BookingManager.*;
import g12.Server.FlightManager.Exceptions.*;

public interface IFlightManager {

	/**
	 * Efetua o login de um utilizador, validando o seu username e pass
	 * 
	 * @param user Username
	 * @param pass Password
	 * @return Token gerado e associado ao user (deve ser utilizado em comunicacoes
	 *         subsequentes!)
	 * @throws LoginInvalido Caso o user nao exista ou pass errada
	 */
	String login(String user, String pass) throws LoginInvalido;

	/**
	 * Regista um utilizador novo
	 * 
	 * @param user Nome user
	 * @param pass Password
	 * @throws UserJaExisteException Caso ja existe um utilizador com o mesmo nome
	 */
	void registerUser(String user, String pass) throws UserJaExisteException;

	/**
	 * Regista um novo voo diário
	 * 
	 * @param origem Origem
	 * @param dest   Destino
	 * @param cap    Capacidade do voo
	 */
	void registerFlight(String origem, String dest, Integer cap);

	/**
	 * Atualiza o dia de hoje, colocando-o como fechado
	 * 
	 * @param user Nome do utilizador a pedir o encerramento do dia
	 * @throws UserNaoExistente Caso o utilizador nao exista
	 * @throws NotAllowed       Caso o utilizador nao tenha permissoes (nao seja
	 *                          admin)
	 * @throws DiaFechado
	 */
	Boolean closeDay(String user) throws UserNaoExistente, NotAllowed, DiaFechado, BookingDayNaoExistente;

	/**
	 * Efetua a marcação de uma reserva que passe pelo percurso pretendido
	 * Verifica se o utilizador existe
	 * Efetua lock ao user
	 * adiciona reserva no book
	 * adiciona id da reserva ao user
	 * 
	 * @param user     Utilizador a pedir a reserva
	 * @param percurso Percurso que pretende
	 * @param de       Data de inicio
	 * @param ate      Data de fim
	 * @throws UserNaoExistente Caso o utilizador nao exista
	 * @throws UserIsNotClient  Caso o utilizador nao seja cliente (pode ser admin)
	 */
	String bookFlight(String user, List<String> percurso, LocalDate de, LocalDate ate)
			throws UserIsNotClient, UserNaoExistente, VooNaoExistente, ReservaIndisponivel, PercusoNaoDisponivel,
			BookingDayNaoExistente, BookingDayJaExiste, DiaFechado, VooJaExiste;

	/**
	 * Cancela uma reserva;
	 * Verifica se o utilizador existe
	 * Efetua lock ao user
	 * verifica se o user tem a reserva associada
	 * elimina a reserva do booking
	 * elimina a reserva do user
	 * 
	 * @param user Utilizador que se vai remover a reserva
	 * @param id   Id da reserva
	 * @throws UserIsNotClient  Caso o utilizador nao seja cliente
	 * @throws UserNaoExistente Caso o user nao exista
	 * @throws ReservaNaoExiste
	 */
	Boolean cancelBook(String user, String id)
			throws UserNaoExistente, UserIsNotClient, ReservaNaoExiste, VooNaoExistente, BookingDayNaoExistente;

	/**
	 * Calcula os voos disponiveis para o dia
	 * 
	 * @return Lista de Voos diponiveis
	 */
	List<Voo> availableFlights();

	/**
	 *  Obtem lista com todos os percursos possíveis para viajar entre uma origem e um destino, limitados a duas escalas
	 *
	 * @return Lista de todos os percursos possíveis
	 */
	List<List<String>> getFlightList(String origem, String destino);

	/**
	 * Verifica se um token é válido (algoritmo + claim)
	 * 
	 * @param token Token a ser validado
	 * @return Nome do utilizador
	 * @throws TokenInvalido Caso a assinatura seja inválida, claim nao exista ou
	 *                       user nao exista!
	 */
	String verifyToken(String token) throws TokenInvalido;

	/**
	 * Guarda o manager num ficheiro de objetos
	 * 
	 * @return True se foi possivel
	 */
	boolean saveState();
}