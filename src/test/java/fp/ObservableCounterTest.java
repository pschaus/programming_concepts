package fp;

import fp.ObservableCounter;
import org.javagrader.Grade;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Grade
public class ObservableCounterTest {

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testValue() {
        ObservableCounter c = new ObservableCounter();

        assertEquals(1,c.increment());
        assertEquals(1,c.value());

        assertEquals(2,c.increment());
        assertEquals(2,c.value());
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testOneObserver() {
        ObservableCounter c = new ObservableCounter();

        int [] counter = new int[]{0};

        c.onChange(v -> {
            counter[0]++;
            assertEquals(v,Integer.valueOf(c.value()));
        });


        for (int i = 0; i < 10; i++) {
            assertEquals(c.increment(),counter[0]);
            assertEquals(counter[0],c.value());
        }
    }


    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testTwoObservers() {
        ObservableCounter c = new ObservableCounter();

        int [] counter = new int[] {0,0};

        c.onChange(v -> {
            assertEquals(v,Integer.valueOf(c.value()));
            counter[0]++;
        });

        for (int i = 0; i < 10; i++) {
            assertEquals(c.increment(),counter[0]);
            assertEquals(counter[0],c.value());
        }

        c.onChange(v -> {
            assertEquals(v,Integer.valueOf(c.value()));
            counter[1]++;
        });

        for (int i = 0; i < 10; i++) {
            c.increment();
            assertEquals(c.value(),counter[0]);
            assertEquals(c.value(),counter[1]+10);
        }
    }


    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testCountersUsedInParallel() {
        ObservableCounter c = new ObservableCounter();

        int [] counter = new int[] {0};

        c.onChange(v -> {
            counter[0]++;
        });


        try {

            int l = 100000;

            ExecutorService executor = Executors.newFixedThreadPool(2);

            Future f1 = executor.submit(() -> {
                for (int i = 0; i < l; i++) {
                    c.increment();
                }
            });

            Future f2 = executor.submit(() -> {
                for (int i = l; i < 2*l; i++) {
                    c.increment();
                }
            });

            f1.get();
            f2.get();

            assertEquals(l*2, counter[0]);
            executor.shutdown();

        } catch (ExecutionException e1) {

        } catch (InterruptedException e2) {

        }


    }



}