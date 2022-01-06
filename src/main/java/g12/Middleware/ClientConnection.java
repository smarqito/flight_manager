package g12.Middleware;

import java.io.IOException;
import java.net.Socket;

import g12.Middleware.DTO.QueryDTO.QueryDTO;

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
  public void send(Frame f) throws IOException {
    QueryDTO q = (QueryDTO) f.getDto();
    q.setToken(this.token);
    super.send(f);
  }
}
