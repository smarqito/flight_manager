package g12.Middleware;

import java.io.IOException;
import java.net.Socket;

public class ClientConnection extends TaggedConnection {

    public ClientConnection(Socket s) throws IOException {
        super(s);
    }

    @Override
    public Frame receive() throws IOException {
		rcvLock.lock();
		try {
			return Response.deserialize(in);
		} finally {
			rcvLock.unlock();
		}
    }

    
}
