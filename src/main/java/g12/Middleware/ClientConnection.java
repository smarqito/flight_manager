package g12.Middleware;

import java.io.IOException;
import java.net.Socket;

public class ClientConnection extends TaggedConnection {
  private String token;

  public ClientConnection(Socket s) throws IOException {
    super(s);
    token = "";
  }

  public void setToken(String token) {
    this.token = token;
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

  @Override
  public void send(Frame f) throws IOException {
    Query q = (Query) f;
    q.setToken(this.token);
    super.send(f);
  }
}
