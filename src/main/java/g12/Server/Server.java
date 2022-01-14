package g12.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import g12.Middleware.ServerConnection;
import g12.Server.FlightManager.IFlightManager;

class ThreadHandler extends ArrayList<Thread> implements Runnable {
	Lock l = new ReentrantLock();
	Condition c = l.newCondition();
	boolean isAlive = true;

	/**
	 * Continuamente vai limpando as threads até:
	 * socket seja fechado &&
	 * tamanho da lista seja 0 (permite que threads pendentes terminem)
	 */
	@Override
	public void run() {
		l.lock();
		try {
			while (true) {
				if (!isAlive && size() == 0) {
					break;
				}
				try {
					c.await();
					clearThreads();
				} catch (InterruptedException e) {
				}
			}
		} finally {
			l.unlock();
		}
	}

	/**
	 * Limpa as threads que não se encontrem ativas
	 */
	public void clearThreads() {
		l.lock();
		try {
			for (Thread thread : this) {
				if (!thread.isAlive()) {
					this.remove(thread);
				}
			}
		} finally {
			l.unlock();
		}
	}

	/**
	 * Verifica se há mais threads ativas no servidor
	 */
	public void wait_all_finish() {
		l.lock();
		try {
			boolean active = hasActiveThread();
			while (active) {
				try {
					c.await(4, TimeUnit.SECONDS);
				} catch (InterruptedException e) {
				}
				active = hasActiveThread();
			}
		} finally {
			l.unlock();
		}
	}

	@Override
	public boolean add(Thread e) {
		l.lock();
		try {
			c.signalAll();
			return super.add(e);
		} finally {
			l.unlock();
		}
	}

	public boolean hasActiveThread() {
		l.lock();
		try {
			for (Thread thread : this) {
				if (thread.isAlive()) {
					return true;
				}
			}
		} finally {
			l.unlock();
		}
		return false;
	}
}

public class Server implements Runnable {
	private IFlightManager model;
	private ServerSocket ss;
	private ThreadHandler th;
	private Thread t;

	public Server(IFlightManager model) {
		this.model = model;
		this.th = new ThreadHandler();
	}

	@Override
	public void run() {
		try {
			ss = new ServerSocket(4444);
			this.t = new Thread(th);
			t.start();
			while (true) {
				Socket s = ss.accept();
				ServerConnection tg = new ServerConnection(s);
				new Thread(new ServerWorker(tg, this.model, this.th)).start();
			}
		} catch (IOException e) {
		}
	}

	public void stopServer() {
		try {
			ss.close();
			this.th.isAlive = false;
			this.th.wait_all_finish();
			this.t.interrupt();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}