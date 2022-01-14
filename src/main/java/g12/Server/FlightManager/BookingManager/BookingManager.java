package g12.Server.FlightManager.BookingManager;

import g12.Server.FlightManager.Exceptions.*;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class BookingManager implements IBookingManager {

	private Map<String, Reserva> reservas;
	private Set<BookingDay> voos;
	private List<Voo> voosDiarios;
	private Map<String, List<String>> grafo;
	private Lock l = new ReentrantLock();
	private Lock g = new ReentrantLock();

	public BookingManager() {
		reservas = new HashMap<>();
		voos = new TreeSet<>();
		voosDiarios = new ArrayList<>();
		grafo = new HashMap<>();
	}

	@Override
	public void addFlight(String orig, String dest, Integer cap) {
		this.l.lock();
		try {
			Voo v = new Voo(orig, dest, cap);
			this.voosDiarios.add(v);
			g.lock();
			try {
				if (grafo.containsKey(v.getOrigem())) {
					List<String> arestas = grafo.get(v.getOrigem());
					arestas.add(v.getDestino());
					grafo.put(v.getOrigem(), arestas);
				} else {
					List<String> arestas = new ArrayList<>();
					arestas.add(v.getDestino());
					grafo.put(v.getOrigem(), arestas);

				}
			} finally {
				g.unlock();
			}

			Set<BookingDay> days = this.voos.stream()
					.filter(x -> x.getDate().compareTo(LocalDate.now()) >= 0 && x.isOpen())
					.collect(Collectors.toSet());
			// obtem locks por ordem de data
			days.stream().forEach(d -> d.l.lock());
			try {
				days.stream().forEach(d -> {
					try {
						d.addVoo(new Voo(v));
					} catch (VooJaExiste | DiaFechado e) {
						// ja controlado nos streams anteriores. Voo e novo ID
					}
				});
			} finally {
				// unlock pela mesma ordem
				days.stream().forEach(d -> d.l.unlock());
			}
		} finally {
			this.l.unlock();
		}
	}

	@Override
	public Boolean closeDay() throws DiaFechado, BookingDayNaoExistente {
		this.l.lock();
		BookingDay bd;
		try {
			bd = this.getBookingDay(LocalDate.now());
			bd.l.lock();
		} finally {
			this.l.unlock();
		}
		try {
			bd.closeDay();
			return true;
		} finally {
			bd.l.unlock();
		}
	}

	public String bookFlight(String user, List<String> percurso, LocalDate de, LocalDate ate) throws VooNaoExistente,
			ReservaIndisponivel, PercusoNaoDisponivel, BookingDayJaExiste, DiaFechado, VooJaExiste {
		if (de.compareTo(LocalDate.now()) < 0 || de.compareTo(ate) > 0) {
			throw new ReservaIndisponivel("Datas erradas");
		}
		l.lock();
		try {
			for (int i = 0; i < percurso.size() - 1; i++) {
				if (!existeVoo(percurso.get(i), percurso.get(i + 1)))
					throw new PercusoNaoDisponivel("O percurso não está disponível.");
			}
		} finally {
			l.unlock();
		}
		Reserva r = new Reserva(user);
		while (percurso.size() >= 1 && de.compareTo(ate) < 0) {
			l.lock();
			BookingDay bd;
			try {
				try {
					bd = this.getBookingDay(de);
				} catch (BookingDayNaoExistente e) {
					bd = this.addBookingDay(de);
				}
				bd.l.lock();
			} finally {
				l.unlock();
			}
			try {
				List<InfoVoo> info = bd.addPassager(percurso);
				r.addVooInfo(info);
				info.forEach(x -> percurso.remove(x.origem));
				de = de.plusDays(1);
			} catch (DiaFechado e) {
				de = de.plusDays(1);
			} finally {
				bd.l.unlock();
			}
		}
		if (percurso.size() > 1)
			throw new ReservaIndisponivel();
		l.lock();
		try {
			this.reservas.put(r.getId(), r);
			return r.getId();
		} finally {
			l.unlock();
		}
	}

	class VooIdData implements Comparable<VooIdData> {
		private List<String> idList = new ArrayList<>();
		private LocalDate data;

		public VooIdData(String id, LocalDate data) {
			this.idList.add(id);
			this.data = data;
		}

		public List<String> getIdList() {
			return new ArrayList<>(idList);
		}

		public LocalDate getData() {
			return data;
		}

		public void addId(String id) {
			this.idList.add(id);
		}

		@Override
		public int compareTo(VooIdData o) {
			return this.data.compareTo(o.getData());
		}
	}

	@Override
	public void removeBooking(String bookId) throws ReservaNaoExiste, VooNaoExistente, BookingDayNaoExistente {
		this.l.lock();
		Reserva r;
		try {
			r = this.getReserva(bookId);
			// falta verificar se existe algum lock à espera
			this.reservas.remove(bookId);
			r.l.lock();
		} finally {
			this.l.unlock();
		}
		Set<VooIdData> voosReservados = new HashSet<>();
		try {
			for (InfoVoo i : r.getInfoVoos()) {
				try {
					VooIdData v = voosReservados.stream().filter(x -> x.getData().equals(i.getData())).findFirst()
							.get();
					v.addId(i.getId());
				} catch (NoSuchElementException e) {
					voosReservados.add(new VooIdData(i.getId(), i.getData()));
				}
			}
		} finally {
			r.l.unlock();
		}
		for (VooIdData v : voosReservados) {
			this.l.lock();
			BookingDay bd;
			try {
				bd = this.getBookingDay(v.getData());
				bd.l.lock();
			} finally {
				l.unlock();
			}
			Voo voo;
			for (String id : v.getIdList()) {
				try {
					voo = bd.getVoo(id);
					voo.l.lock();
					try {
						voo.removeUser();
					} finally {
						voo.l.unlock();
					}
				} finally {
					bd.l.unlock();
				}
			}
		}
	}

	public List<Voo> getAvailableFlights() {
		this.l.lock();
		try {
			return voosDiarios.stream().map(Voo::clone).collect(Collectors.toList());
		} finally {
			this.l.unlock();
		}
	}

	public List<List<String>> getFlightList(String origem, String destino) {
		Queue<List<String>> queue = new LinkedList<>();
		List<List<String>> voos = new ArrayList<>();
		this.g.lock();
		try {
			// Path vector to store the current path
			List<String> path = new ArrayList<>();
			path.add(origem);
			queue.offer(path);
			// minimizar o custo (Set)
			while (!queue.isEmpty()) {
				path = queue.poll();
				String last = path.get(path.size() - 1);

				// If last vertex is the desired destination
				// then print the path
				if (last.equals(destino) && path.size() <= 3) {
					voos.add(path);
				}

				// Traverse to all the nodes connected to
				// current vertex and push new path to queue
				List<String> lastNode = grafo.get(last);
				if (lastNode != null) {
					for (int i = 0; i < lastNode.size(); i++) {
						if (isNotVisited(lastNode.get(i), path)) {
							List<String> newpath = new ArrayList<>(path);
							newpath.add(lastNode.get(i));
							queue.offer(newpath);
						}
					}
				}
			}
		} finally {
			this.g.unlock();
		}
		return voos;
	}

	private boolean isNotVisited(String x, List<String> path) {
		int size = path.size();
		for (int i = 0; i < size; i++)
			if (path.get(i).equals(x))
				return false;

		return true;
	}

	public BookingDay getBookingDay(LocalDate date) throws BookingDayNaoExistente {
		this.l.lock();
		try {
			try {
				return this.voos.stream().filter(bday -> bday.getDate().equals(date)).findFirst().get();
			} catch (NoSuchElementException e) {
				throw new BookingDayNaoExistente();
			}
		} finally {
			this.l.unlock();
		}
	}

	/**
	 * post:
	 * BookingManager.voos->contains(x | x.date == date) &&
	 * self.voosDiarios->forEach(y | self.voos->select(x | x.date ==
	 * date).contains(y)
	 * 
	 * @param date
	 */
	public BookingDay addBookingDay(LocalDate date) throws DiaFechado, VooJaExiste, BookingDayJaExiste {
		this.l.lock();
		if (this.voos.stream().anyMatch(bd -> bd.getDate().equals(date)))
			throw new BookingDayJaExiste();
		BookingDay bd;
		List<Voo> vdiarios;
		try {
			bd = new BookingDay(date);
			this.voos.add(bd);
			vdiarios = this.voosDiarios;
			bd.l.lock();
		} finally {
			this.l.unlock();
		}
		try {
			for (Voo v : vdiarios)
				bd.addVoo(v);
			return bd;
		} finally {
			bd.l.unlock();
		}
	}

	public Reserva getReserva(String id) throws ReservaNaoExiste {
		this.l.lock();
		try {
			if (!this.reservas.containsKey(id))
				throw new ReservaNaoExiste("Reserva +" + id + " não existe.");
			return this.reservas.get(id);
		} finally {
			this.l.unlock();
		}
	}

	public String addReserva(String user, List<InfoVoo> infoVoos) {
		Reserva r = new Reserva(user);
		r.addVooInfo(infoVoos);
		this.l.lock();
		try {
			this.reservas.put(r.getId(), r);
			r.l.lock();
		} finally {
			this.l.unlock();
		}
		try {
			return r.getId();
		} finally {
			r.l.unlock();
		}
	}

	public Boolean existeVoo(String orig, String dest) {
		return this.voosDiarios.stream().anyMatch(v -> v.getOrigem().equals(orig) && v.getDestino().equals(dest));
	}

	public Voo getVooDiario(String orig, String dest) throws VooNaoExistente {
		this.l.lock();
		try {
			if (this.voosDiarios.stream().noneMatch(v -> v.getOrigem().equals(orig) && v.getDestino().equals(dest)))
				throw new VooNaoExistente();

			return this.voosDiarios.stream().filter(v -> v.getOrigem().equals(orig) && v.getDestino().equals(dest))
					.collect(Collectors.toList()).get(0);
		} finally {
			this.l.unlock();
		}
	}
}