package g12.Middleware;

import java.io.IOException;

import g12.Middleware.DTO.DTO;

public class QueryThread extends Thread {
    private final DTO queryDTO;
    private DTO responseDTO;
    private final int tag;
    private final Demultiplexer c;
    private IOException e = null;
    private boolean waiting = false;

    public QueryThread(DTO queryDTO, int tag, Demultiplexer c) {
        this.queryDTO = queryDTO;
        this.tag = tag;
        this.c = c;
    }

    @Override
    public void run() {
        Frame f = new Frame(tag, queryDTO);
        try {
            this.c.send(f);
            this.waiting = true;
            this.responseDTO = this.c.receive(tag);
            this.waiting = false;
        } catch (IOException e) {
            this.e = e;
        }
    }

    public DTO getResponse() throws StillWaitingException, IOException {
        if (waiting) {
            throw new StillWaitingException();
        }
        // houve uma exception, tem de retornar
        if (e != null) {
            throw e;
        }

        return this.responseDTO;
    }

    /**
     * transforma o metodo async num metodo sync, esperando pelo resultado
     * 
     * @return
     * @throws IOException
     */
    public DTO result() throws IOException {
        for (;;) {
            try {
                this.join();
                break;
            } catch (InterruptedException e) {
            }
        }
        try {
            return this.getResponse();
        } catch (StillWaitingException e) {
            throw new IOException(e.getMessage()); // nao acontece, fez wait previamente
        }
    }

}
