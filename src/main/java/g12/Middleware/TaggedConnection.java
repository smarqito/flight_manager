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
	public static class Frame {
		public final int tag;
		public final byte[] data;

		public Frame(int tag, byte[] data) {
			this.tag = tag;
			this.data = data;
		}
	}

	private Socket s;
	private DataInputStream in;
	private DataOutputStream out;
	private Lock sendLock = new ReentrantLock();
	private Lock rcvLock = new ReentrantLock();

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
		this.send(f.tag, f.data);
	}

	/**
	 * 
	 * @param tag
	 * @param data
	 * @throws IOException
	 */
	public void send(int tag, byte[] data) throws IOException {
		sendLock.lock();
		try {
			out.writeInt(data.length + 4);
			out.writeInt(tag);
			out.write(data);
			out.flush();
		} finally {
			sendLock.unlock();
		}
	}

	public Frame receive() throws IOException {
		rcvLock.lock();
		try {
			int size = in.readInt();
			int tag = in.readInt();
			byte[] data = new byte[size-4];
			in.readFully(data);
			return new Frame(tag, data);
		} finally {
			rcvLock.unlock();
		}
	}

	@Override
	public void close() throws IOException {
		s.close();
	}
}