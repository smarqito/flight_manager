package g12.Middleware;

import java.io.IOException;
import java.net.Socket;

public class ServerConnection extends TaggedConnection {

    public ServerConnection(Socket s) throws IOException {
        super(s);
    }

    @Override
    public String toString() {
        return this.s.toString();
    }

}
