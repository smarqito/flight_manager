package g12.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import g12.Middleware.ServerConnection;
import g12.Server.FlightManager.FlightManagerFacade;
import g12.Server.FlightManager.IFlightManager;
import g12.Server.Logger.Logger;

class ThreadHandler extends ArrayList<Thread> implements Runnable {
	Lock l = new ReentrantLock();
	Condition c = l.newCondition();
	boolean isAlive = true;

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

class ServerHandler implements Runnable {
	private IFlightManager model;
	private ServerSocket ss;
	private ThreadHandler th;
	private Thread t;

	public ServerHandler(IFlightManager model) {
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

public class Server {
	/**
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		IFlightManager model = FlightManagerFacade.getState();
		Logger.InitLogger();
		ServerHandler server = new ServerHandler(model);
		Thread t = new Thread(server);
		t.start();

		Scanner s = new Scanner(System.in);
		while (true) {
			String r = s.nextLine();
			if (r.equals("exit")) {
				// colocar o Server Socket a nao receber mais ligacoes
				server.stopServer();
				t.interrupt();
				// verificar se o model ainda tem operacoes pendentes
				s.close();
				break;
			}
		}

	}

}