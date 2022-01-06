package g12.Server.FlightManager.BookingManager;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import g12.Server.FlightManager.Exceptions.VooNaoExistente;

public class BookingManagerTest {
    private BookingManager bm;

    public BookingManagerTest() {
        bm = new BookingManager();
    }

    @Test
    public void testAddFlight() throws InterruptedException, VooNaoExistente {
        int N = 10;
        Thread[] ts = new Thread[N];
        for (int i = 0; i < N; i++) {
            final int e = i;
            Runnable r = () -> {
                bm.addFlight(e + "orig", e + "dest", 100 + e * 10 + e * 2);
            };
            Thread t = new Thread(r);
            ts[i] = t;
        }
        for (int i = 0; i < N; i++) {
            ts[i].start();
        }

        for (int i = 0; i < N; i++) {
            ts[i].join();
        }

        for (int i = 0; i < N; i++) {
            assertTrue(bm.getVooDiario(i+"orig", i+"dest") != null);
        }
    }

    @Test
    public void testCloseDay() {

    }
}
