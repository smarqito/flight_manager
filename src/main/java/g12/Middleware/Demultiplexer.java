package g12.Middleware;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import g12.Middleware.DTO.DTO;

public class Demultiplexer extends ClientConnection {
    private Map<Integer, Entry> mapa = new HashMap<>();
    Lock mLock = new ReentrantLock();
    private IOException exc = null;
    private Thread receiver;

    public Demultiplexer(Socket s) throws IOException {
        super(s);
    }

    private class Entry {
        final Condition cond = mLock.newCondition();
        final ArrayDeque<DTO> queue = new ArrayDeque<>();
        int waiters = 0;
    }

    private Entry get(int tag) {
        mLock.lock();
        try {
            Entry e = mapa.get(tag);
            if (e == null) {
                e = new Entry();
                mapa.put(tag, e);
            }
            return e;
        } finally {
            mLock.unlock();
        }
    }

    public void start() {
        this.receiver = new Thread(() -> {
            try {
                for (;;) {
                    Frame f = super.receive();
                    mLock.lock();
                    try {
                        Entry e = get(f.tag);
                        e.queue.add(f.getDto());
                        e.cond.signal();
                    } finally {
                        mLock.unlock();
                    }
                }
            } catch (IOException e) {
                mLock.lock();
                try {
                    this.exc = e;
                    mapa.values().stream().forEach(x -> x.cond.signalAll());
                } finally {
                    mLock.unlock();
                }
            }
        });
        this.receiver.start();
    }

    public DTO receive(int tag) throws IOException {
        mLock.lock();
        try {
            Entry e = get(tag);
            e.waiters++;

            for (;;) {
                if (!e.queue.isEmpty()) {
                    DTO res = e.queue.poll();
                    e.waiters--;
                    if (e.queue.isEmpty() && e.waiters == 0) {
                        mapa.remove(tag);
                    }
                    return res;
                }
                if (this.exc != null) { // por exemplo socket ser fechada
                    throw this.exc;
                }
                try {
                    e.cond.await();
                } catch (InterruptedException e1) {
                }
            }
        } finally {
            mLock.unlock();
        }
    }

    @Override
    public void close() throws IOException {
        super.close();
        this.receiver.interrupt(); // fecha thread que esta a receber info da socket
    }
}
