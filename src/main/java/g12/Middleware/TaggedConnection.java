package g12.Middleware;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TaggedConnection implements AutoCloseable {
	protected Socket s;
	protected DataInputStream in;
	protected DataOutputStream out;
	protected Lock sendLock = new ReentrantLock();
	protected Lock rcvLock = new ReentrantLock();

	/**
	 * 
	 * @param s
	 * @throws IOException
	 */
	public TaggedConnection(Socket s) throws IOException {
		this.s = s;
		in = new DataInputStream(new BufferedInputStream(s.getInputStream()));
		out = new DataOutputStream(new BufferedOutputStream(s.getOutputStream()));
	}

	/**
	 * 
	 * @param f
	 * @throws IOException
	 */
	public void send(Frame f) throws IOException {
		sendLock.lock();
		try {
			f.serialize(out);
			out.flush();
		} finally {
			sendLock.unlock();
		}
	}

	public Frame receive() throws IOException {
		rcvLock.lock();
		try {
			return Frame.deserialize(in);
		} finally {
			rcvLock.unlock();
		}
	}

	@Override
	public void close() throws IOException {
		s.close();
	}
}