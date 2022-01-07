package g12.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import g12.Middleware.ServerConnection;
import g12.Server.FlightManager.FlightManagerFacade;
import g12.Server.FlightManager.IFlightManager;

class ThreadHandler extends ArrayList<Thread> implements Runnable {
	Lock l = new ReentrantLock();
	Condition c = l.newCondition();

	@Override
	public void run() {
		while (true) {
			try {
				c.await();
				clearThreads();
			} catch (InterruptedException e) {
			}
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
					return false;
				}
			}
		} finally {
			l.unlock();
		}
		return true;
	}
}

class ServerHandler implements Runnable {
	private IFlightManager model;
	private ServerSocket ss;

	public ServerHandler(IFlightManager model) {
		this.model = model;
	}

	@Override
	public void run() {
		try {
			ss = new ServerSocket(4444);
			while (true) {
				Socket s = ss.accept();
				ServerConnection tg = new ServerConnection(s);
				new Thread(new ServerWorker(tg, this.model)).start();
			}
		} catch (IOException e) {
		}
	}

	public void stopServer() {
		try {
			ss.close();
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

		ServerHandler server = new ServerHandler(model);
		Thread t = new Thread(server);
		t.run();

		Scanner s = new Scanner(System.in);
		while (true) {
			String r = s.nextLine();
			if (r.equals("exit")) {
				// colocar o Server Socket a nao receber mais ligacoes
				server.stopServer();
				t.interrupt();
				// verificar se o model ainda tem operacoes pendentes
				s.close();
			}

		}

	}

}