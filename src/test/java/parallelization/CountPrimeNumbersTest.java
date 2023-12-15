package parallelization;

import org.javagrader.Allow;
import org.javagrader.Grade;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

@Grade
@Allow("java.lang.Thread")
public class CountPrimeNumbersTest {

    @Test
    @Grade(value = 1, cpuTimeout = 2000)
    public void testIsPrime() {
        assertFalse(CountPrimeNumbers.isPrime(-10));
        assertFalse(CountPrimeNumbers.isPrime(-1));
        assertFalse(CountPrimeNumbers.isPrime(0));
        assertFalse(CountPrimeNumbers.isPrime(1));
        assertTrue(CountPrimeNumbers.isPrime(2));
        assertTrue(CountPrimeNumbers.isPrime(3));
        assertFalse(CountPrimeNumbers.isPrime(4));
        assertTrue(CountPrimeNumbers.isPrime(5));
        assertFalse(CountPrimeNumbers.isPrime(6));
        assertTrue(CountPrimeNumbers.isPrime(7));
        assertFalse(CountPrimeNumbers.isPrime(8));
        assertFalse(CountPrimeNumbers.isPrime(9));
        assertFalse(CountPrimeNumbers.isPrime(10));
        assertTrue(CountPrimeNumbers.isPrime(11));
        assertTrue(CountPrimeNumbers.isPrime(19));
        assertTrue(CountPrimeNumbers.isPrime(17));
        assertFalse(CountPrimeNumbers.isPrime(0));
        assertFalse(CountPrimeNumbers.isPrime(27));
        assertTrue(CountPrimeNumbers.isPrime(8191));
    }

    @Test
    @Grade(value = 1, cpuTimeout = 2000)
    public void testCountSequential() {
        assertEquals(0, CountPrimeNumbers.countPrimesInInterval(-1000, 2));
        assertEquals(1, CountPrimeNumbers.countPrimesInInterval(-1000, 3));
        assertEquals(24, CountPrimeNumbers.countPrimesInInterval(-1000, 97));
        assertEquals(25, CountPrimeNumbers.countPrimesInInterval(-1000, 98));
        assertEquals(6, CountPrimeNumbers.countPrimesInInterval(70, 100));
        assertEquals(1061, CountPrimeNumbers.countPrimesInInterval(1000, 10000));
        assertEquals(0, CountPrimeNumbers.countPrimesInInterval(10000, 1000));
    }

    private int executeRunnable(int startIndex, int endIndex) {
        CountPrimeNumbers.CountPrimesRunnable r = new CountPrimeNumbers.CountPrimesRunnable(startIndex, endIndex);
        r.run();
        return r.getResult();
    }        
        
    private int executeCallable(int startIndex, int endIndex) {
        return new CountPrimeNumbers.CountPrimesCallable(startIndex, endIndex).call();
    }        
        
    private int executeSharedCounter(int startIndex, int endIndex) {
        CountPrimeNumbers.SharedCounter counter = new CountPrimeNumbers.SharedCounter();
        counter.set(-42);
        CountPrimeNumbers.CountPrimesSharedCounter r = new CountPrimeNumbers.CountPrimesSharedCounter(counter, startIndex, endIndex);
        r.run();
        return counter.getValue() + 42;
    }        
        
    @Test
    @Grade(value = 1, cpuTimeout = 2000)
    public void testCountRunnable() {
        assertEquals(0, executeRunnable(-1000, 2));
        assertEquals(1, executeRunnable(-1000, 3));
        assertEquals(24, executeRunnable(-1000, 97));
        assertEquals(25, executeRunnable(-1000, 98));
        assertEquals(6, executeRunnable(70, 100));
        assertEquals(1061, executeRunnable(1000, 10000));
        assertEquals(14, executeRunnable(700, 800));
        assertEquals(92, executeRunnable(1300, 2000));
    }
        
    @Test
    @Grade(value = 1, cpuTimeout = 2000)
    public void testCountCallable() {
        assertEquals(0, executeCallable(-1000, 2));
        assertEquals(1, executeCallable(-1000, 3));
        assertEquals(24, executeCallable(-1000, 97));
        assertEquals(25, executeCallable(-1000, 98));
        assertEquals(6, executeCallable(70, 100));
        assertEquals(1061, executeCallable(1000, 10000));
        assertEquals(114, executeRunnable(5000, 6000));
    }
        
    @Test
    @Grade(value = 1, cpuTimeout = 2000)
    public void testSharedCounter() {
        assertEquals(0, executeCallable(-1000, 2));
        assertEquals(1, executeCallable(-1000, 3));
        assertEquals(24, executeCallable(-1000, 97));
        assertEquals(25, executeCallable(-1000, 98));
        assertEquals(6, executeCallable(70, 100));
        assertEquals(59, executeCallable(500, 900));
        assertEquals(68, executeRunnable(4700, 5300));
    }


    // BEGIN STRIP
    private static class MyExecutorService implements ExecutorService {
        private int countCallableSubmits;
        private int countRunnableSubmits;
        private ExecutorService wrapped;

        public synchronized int getCountCallableSubmits() {
            return countCallableSubmits;
        }

        public synchronized int getCountRunnableSubmits() {
            return countRunnableSubmits;
        }

        public synchronized void resetCountSubmits() {
            countCallableSubmits = 0;
            countRunnableSubmits = 0;
        }

        MyExecutorService(int nThreads) {
            wrapped = Executors.newFixedThreadPool(nThreads);
        }

        @Override
        public void shutdown() {
            wrapped.shutdown();
        }

        @Override
        public List<Runnable> shutdownNow() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isShutdown() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isTerminated() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean awaitTermination(long l, TimeUnit timeUnit) throws InterruptedException {
            throw new UnsupportedOperationException();
        }

        @Override
        public <T> Future<T> submit(Callable<T> callable) {
            countCallableSubmits++;
            return wrapped.submit(callable);
        }

        @Override
        public <T> Future<T> submit(Runnable runnable, T t) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Future<?> submit(Runnable runnable) {
            countRunnableSubmits++;
            return wrapped.submit(runnable);
        }

        @Override
        public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> collection) throws InterruptedException {
            throw new UnsupportedOperationException();
        }

        @Override
        public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> collection, long l, TimeUnit timeUnit) throws InterruptedException {
            throw new UnsupportedOperationException();
        }

        @Override
        public <T> T invokeAny(Collection<? extends Callable<T>> collection) throws InterruptedException,
            ExecutionException {
            throw new UnsupportedOperationException();
        }

        @Override
        public <T> T invokeAny(Collection<? extends Callable<T>> collection, long l, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void execute(Runnable runnable) {
            throw new UnsupportedOperationException();
        }
    }
    
    @Test
    @Grade(value = 3, cpuTimeout = 2000)
    public void testCountRunnableInsideThreadPool() throws ExecutionException, InterruptedException {
        MyExecutorService threadPool = new MyExecutorService(2);

        try {
            assertEquals(0, CountPrimeNumbers.countPrimesWithRunnable(threadPool, 2, 10));
            assertEquals(10, threadPool.getCountRunnableSubmits());
            assertEquals(0, threadPool.getCountCallableSubmits());
            threadPool.resetCountSubmits();
        
            assertEquals(1, CountPrimeNumbers.countPrimesWithRunnable(threadPool, 3, 10));
            assertEquals(10, threadPool.getCountRunnableSubmits());
            assertEquals(0, threadPool.getCountCallableSubmits());
            threadPool.resetCountSubmits();
        
            assertEquals(2, CountPrimeNumbers.countPrimesWithRunnable(threadPool, 4, 10));
            assertEquals(10, threadPool.getCountRunnableSubmits());
            assertEquals(0, threadPool.getCountCallableSubmits());
            threadPool.resetCountSubmits();
        
            assertEquals(24, CountPrimeNumbers.countPrimesWithRunnable(threadPool, 97, 3));
            assertEquals(3, threadPool.getCountRunnableSubmits());
            assertEquals(0, threadPool.getCountCallableSubmits());
            threadPool.resetCountSubmits();
        
            assertEquals(25, CountPrimeNumbers.countPrimesWithRunnable(threadPool, 98, 4));
            assertEquals(4, threadPool.getCountRunnableSubmits());
            assertEquals(0, threadPool.getCountCallableSubmits());
            threadPool.resetCountSubmits();
        
            assertEquals(25, CountPrimeNumbers.countPrimesWithRunnable(threadPool, 100, 5));
            assertEquals(5, threadPool.getCountRunnableSubmits());
            assertEquals(0, threadPool.getCountCallableSubmits());
            threadPool.resetCountSubmits();
        
            assertEquals(25, CountPrimeNumbers.countPrimesWithRunnable(threadPool, 101, 5));
            assertEquals(5, threadPool.getCountRunnableSubmits());
            assertEquals(0, threadPool.getCountCallableSubmits());
            threadPool.resetCountSubmits();
        
            assertEquals(26, CountPrimeNumbers.countPrimesWithRunnable(threadPool, 102, 5));
            assertEquals(5, threadPool.getCountRunnableSubmits());
            assertEquals(0, threadPool.getCountCallableSubmits());
            threadPool.resetCountSubmits();
        
            assertEquals(1229, CountPrimeNumbers.countPrimesWithRunnable(threadPool, 10000, 6));
            assertEquals(6, threadPool.getCountRunnableSubmits());
            assertEquals(0, threadPool.getCountCallableSubmits());
            threadPool.resetCountSubmits();
        
            assertEquals(139, CountPrimeNumbers.countPrimesWithRunnable(threadPool, 800, 7));
            assertEquals(7, threadPool.getCountRunnableSubmits());
            assertEquals(0, threadPool.getCountCallableSubmits());
            threadPool.resetCountSubmits();
        
            assertEquals(303, CountPrimeNumbers.countPrimesWithRunnable(threadPool, 2000, 8));
            assertEquals(8, threadPool.getCountRunnableSubmits());
            assertEquals(0, threadPool.getCountCallableSubmits());
            threadPool.resetCountSubmits();        
        } finally {        
            threadPool.shutdown();
        }
    }
    
    @Test
    @Grade(value = 3, cpuTimeout = 2000)
    public void testCountCallableInsideThreadPool() throws ExecutionException, InterruptedException {
        MyExecutorService threadPool = new MyExecutorService(2);

        try {
            assertEquals(0, CountPrimeNumbers.countPrimesWithCallable(threadPool, 2, 10));
            assertEquals(10, threadPool.getCountCallableSubmits());
            assertEquals(0, threadPool.getCountRunnableSubmits());
            threadPool.resetCountSubmits();
        
            assertEquals(1, CountPrimeNumbers.countPrimesWithCallable(threadPool, 3, 10));
            assertEquals(10, threadPool.getCountCallableSubmits());
            assertEquals(0, threadPool.getCountRunnableSubmits());
            threadPool.resetCountSubmits();
        
            assertEquals(2, CountPrimeNumbers.countPrimesWithCallable(threadPool, 4, 10));
            assertEquals(10, threadPool.getCountCallableSubmits());
            assertEquals(0, threadPool.getCountRunnableSubmits());
            threadPool.resetCountSubmits();
        
            assertEquals(24, CountPrimeNumbers.countPrimesWithCallable(threadPool, 97, 3));
            assertEquals(3, threadPool.getCountCallableSubmits());
            assertEquals(0, threadPool.getCountRunnableSubmits());
            threadPool.resetCountSubmits();
        
            assertEquals(25, CountPrimeNumbers.countPrimesWithCallable(threadPool, 98, 4));
            assertEquals(4, threadPool.getCountCallableSubmits());
            assertEquals(0, threadPool.getCountRunnableSubmits());
            threadPool.resetCountSubmits();
        
            assertEquals(25, CountPrimeNumbers.countPrimesWithCallable(threadPool, 100, 5));
            assertEquals(5, threadPool.getCountCallableSubmits());
            assertEquals(0, threadPool.getCountRunnableSubmits());
            threadPool.resetCountSubmits();
        
            assertEquals(25, CountPrimeNumbers.countPrimesWithCallable(threadPool, 101, 5));
            assertEquals(5, threadPool.getCountCallableSubmits());
            assertEquals(0, threadPool.getCountRunnableSubmits());
            threadPool.resetCountSubmits();
        
            assertEquals(26, CountPrimeNumbers.countPrimesWithCallable(threadPool, 102, 5));
            assertEquals(5, threadPool.getCountCallableSubmits());
            assertEquals(0, threadPool.getCountRunnableSubmits());
            threadPool.resetCountSubmits();
        
            assertEquals(1229, CountPrimeNumbers.countPrimesWithCallable(threadPool, 10000, 6));
            assertEquals(6, threadPool.getCountCallableSubmits());
            assertEquals(0, threadPool.getCountRunnableSubmits());
            threadPool.resetCountSubmits();
        
            assertEquals(139, CountPrimeNumbers.countPrimesWithCallable(threadPool, 800, 7));
            assertEquals(7, threadPool.getCountCallableSubmits());
            assertEquals(0, threadPool.getCountRunnableSubmits());
            threadPool.resetCountSubmits();
        
            assertEquals(303, CountPrimeNumbers.countPrimesWithCallable(threadPool, 2000, 8));
            assertEquals(8, threadPool.getCountCallableSubmits());
            assertEquals(0, threadPool.getCountRunnableSubmits());
            threadPool.resetCountSubmits();        
        } finally {        
            threadPool.shutdown();
        }
    }
    
    @Test
    @Grade(value = 3, cpuTimeout = 2000)
    public void testCountSharedCounterInsideThreadPool() throws ExecutionException, InterruptedException {
        MyExecutorService threadPool = new MyExecutorService(2);
        CountPrimeNumbers.SharedCounter counter = new CountPrimeNumbers.SharedCounter();

        try {
            CountPrimeNumbers.countPrimesWithSharedCounter(counter, threadPool, 2, 10);
            assertEquals(0, counter.getValue());
            assertEquals(10, threadPool.getCountRunnableSubmits());
            assertEquals(0, threadPool.getCountCallableSubmits());
            threadPool.resetCountSubmits();

            CountPrimeNumbers.countPrimesWithSharedCounter(counter, threadPool, 3, 10);
            assertEquals(1, counter.getValue());
            assertEquals(10, threadPool.getCountRunnableSubmits());
            assertEquals(0, threadPool.getCountCallableSubmits());
            threadPool.resetCountSubmits();

            CountPrimeNumbers.countPrimesWithSharedCounter(counter, threadPool, 4, 10);
            assertEquals(2, counter.getValue());
            assertEquals(10, threadPool.getCountRunnableSubmits());
            assertEquals(0, threadPool.getCountCallableSubmits());
            threadPool.resetCountSubmits();

            CountPrimeNumbers.countPrimesWithSharedCounter(counter, threadPool, 97, 3);
            assertEquals(24, counter.getValue());
            assertEquals(3, threadPool.getCountRunnableSubmits());
            assertEquals(0, threadPool.getCountCallableSubmits());
            threadPool.resetCountSubmits();

            CountPrimeNumbers.countPrimesWithSharedCounter(counter, threadPool, 98, 4);
            assertEquals(25, counter.getValue());
            assertEquals(4, threadPool.getCountRunnableSubmits());
            assertEquals(0, threadPool.getCountCallableSubmits());
            threadPool.resetCountSubmits();

            CountPrimeNumbers.countPrimesWithSharedCounter(counter, threadPool, 100, 5);
            assertEquals(25, counter.getValue());
            assertEquals(5, threadPool.getCountRunnableSubmits());
            assertEquals(0, threadPool.getCountCallableSubmits());
            threadPool.resetCountSubmits();

            CountPrimeNumbers.countPrimesWithSharedCounter(counter, threadPool, 101, 5);
            assertEquals(25, counter.getValue());
            assertEquals(5, threadPool.getCountRunnableSubmits());
            assertEquals(0, threadPool.getCountCallableSubmits());
            threadPool.resetCountSubmits();

            CountPrimeNumbers.countPrimesWithSharedCounter(counter, threadPool, 102, 5);
            assertEquals(26, counter.getValue());
            assertEquals(5, threadPool.getCountRunnableSubmits());
            assertEquals(0, threadPool.getCountCallableSubmits());
            threadPool.resetCountSubmits();

            CountPrimeNumbers.countPrimesWithSharedCounter(counter, threadPool, 10000, 6);
            assertEquals(1229, counter.getValue());
            assertEquals(6, threadPool.getCountRunnableSubmits());
            assertEquals(0, threadPool.getCountCallableSubmits());
            threadPool.resetCountSubmits();

            CountPrimeNumbers.countPrimesWithSharedCounter(counter, threadPool, 800, 7);
            assertEquals(139, counter.getValue());
            assertEquals(7, threadPool.getCountRunnableSubmits());
            assertEquals(0, threadPool.getCountCallableSubmits());
            threadPool.resetCountSubmits();

            CountPrimeNumbers.countPrimesWithSharedCounter(counter, threadPool, 2000, 8);
            assertEquals(303, counter.getValue());
            assertEquals(8, threadPool.getCountRunnableSubmits());
            assertEquals(0, threadPool.getCountCallableSubmits());
            threadPool.resetCountSubmits();        
        } finally {        
            threadPool.shutdown();
        }
    }
    
    // END STRIP
}
