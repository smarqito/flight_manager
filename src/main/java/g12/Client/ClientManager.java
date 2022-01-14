package g12.Client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import g12.Middleware.QueryThread;
import g12.Middleware.StillWaitingException;
import g12.Middleware.DTO.DTO;
import g12.Middleware.DTO.ExceptionDTO.RequestExceptionDTO;

/**
 * Como cliente tem uma thread principal que efetua o controlo, nao havera
 * concorrencia nesta classe
 */
public class ClientManager {
    private List<QueryThread> ts;
    // private Lock l;

    public ClientManager() {
        // l = new ReentrantLock();
        ts = new ArrayList<>();
    }

    public void addThread(QueryThread t) {
        // l.lock();
        // try {
        ts.add(t);
        // } finally {
        // l.unlock();
        // }
    }

    public List<DTO> getEndedThreads() {
        // l.lock();
        // try {
        List<QueryThread> toRemove = new ArrayList<>();

        List<DTO> res = this.ts.stream().filter(x -> !x.isAlive()).map(x -> {
            try {
                toRemove.add(x);
                return x.getResponse();
            } catch (StillWaitingException | IOException e) {
                return new RequestExceptionDTO(e.getMessage());
            }
        }).collect(Collectors.toList());

        ts.removeAll(toRemove);

        return res;
        // } finally {
        // l.unlock();
        // }
    }

    public int hasPending() {
        // l.lock();
        // try {
        return this.ts.size();
        // } finally {
        // l.unlock();
        // }
    }

    public void removeThread(QueryThread qt) {
        // l.lock();
        // try {
        this.ts.remove(qt);
        // } finally {
        // l.unlock();
        // }
    }
}
