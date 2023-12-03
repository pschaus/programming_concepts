package fp;

import org.javagrader.Allow;
import org.javagrader.Grade;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Function;

@Grade
public class BrickCounterTest {
    private static class MyBrick implements BrickCounter.Brick {
        private final String color;
        private final int size;

        public MyBrick(String color, int size) {
            this.color = color;
            this.size = size;
        }

        @Override
        public String getColor() {
            return color;
        }

        @Override
        public int getSize() {
            return size;
        }
    }

    @Test()
    @Grade(value = 2, cpuTimeout = 1000)
    public void testThreadsTwoBricks() {
        BrickCounter.Brick[] bricks = new BrickCounter.Brick[]{
                new MyBrick("red", 1),
                new MyBrick("green", 2)
        };

        ExecutorService executor = Executors.newFixedThreadPool(2);
        try {
            int[] counts = BrickCounter.countBricksTwoThreads(bricks, 2, brick -> brick.getSize() - 1, executor);
            assertEquals(2, counts.length);
            assertEquals(1, counts[0]);
            assertEquals(1, counts[1]);
        } finally {
            executor.shutdown();
        }
    }

    @Test()
    @Grade(value = 1, cpuTimeout = 1000)
    public void testThreeBricks() {
        // three bricks
        BrickCounter.Brick[] bricks = new BrickCounter.Brick[]{
                new MyBrick("red", 1),
                new MyBrick("green", 5),
                new MyBrick("red", 2),
        };

        // we have three boxes (box 0, box 1, and box 2)
        int numberOfBoxes = 3;

        // we put all red bricks in box 0 and all other bricks in box 2.
        // Note that box 1 is not used.
        Function<BrickCounter.Brick, Integer> sorter = brick -> {
            if(brick.getColor().equals("red")) {
                return 0; // box 0
            }
            else {
                return 2; // box 2
            }
        };

        // count how many bricks go into box 0, box 1, box 2
        int[] counts = BrickCounter.countBricks(bricks, numberOfBoxes, sorter);
        assertEquals(numberOfBoxes, counts.length);

        // there should be two (red) bricks in box 0, no bricks in box 1,
        // and one (green) brick in box 2.
        assertEquals(2, counts[0]);
        assertEquals(0, counts[1]);
        assertEquals(1, counts[2]);
    }

    // BEGIN STRIP

    // THIS IS A COPY-PASTE FROM "FuturesMatrixProductTest.java"
    private interface ThreadTracker {
        Map<Long, Integer> getAccessesCountPerThread();
    }

    private static class MyExecutorService implements ExecutorService {
        private static class MyThreadFactory implements ThreadFactory {
            private final Set<Long> createdThreadsId = new HashSet<>();

            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                createdThreadsId.add(thread.getId());
                return thread;
            }

            public int getPoolSize() {
                return createdThreadsId.size();
            }
        }

        private int countSubmits;
        private ExecutorService wrapped;
        private MyThreadFactory factory;

        public int getCountSubmits() {
            return countSubmits;
        }

        public boolean checkThreads(ThreadTracker tracker) {
            Set<Long> allowedThreads = factory.createdThreadsId;

            for (Long threadId: tracker.getAccessesCountPerThread().keySet()) {
                if (!allowedThreads.contains(threadId)) {
                    // The tracker was accessed by a thread that is not part
                    // of the thread pool (presumably the main thread)
                    return false;
                }
            }

            return true;
        }

        MyExecutorService(int nThreads) {
            factory = new MyThreadFactory();
            wrapped = Executors.newFixedThreadPool(nThreads, factory);
        }

        public int getPoolSize() {
            return factory.getPoolSize();
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
            countSubmits++;
            return wrapped.submit(callable);
        }

        @Override
        public <T> Future<T> submit(Runnable runnable, T t) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Future<?> submit(Runnable runnable) {
            countSubmits++;
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
        public <T> T invokeAny(Collection<? extends Callable<T>> collection) throws InterruptedException, ExecutionException {
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
    // END OF COPY-PASTE

    private static class ThreadsCheckingBrick implements ThreadTracker, BrickCounter.Brick {
        private final Map<Long, Integer> accessesCountPerThread = new HashMap<>();
        private final String color;
        private final int size;

        public ThreadsCheckingBrick(String color, int size) {
            this.color = color;
            this.size = size;
        }

        private void incrementAccessesCount() {
            long threadId = Thread.currentThread().getId();

            int previous = 0;
            if (accessesCountPerThread.containsKey(threadId)) {
                previous = accessesCountPerThread.get(threadId);
            }

            accessesCountPerThread.put(threadId, previous + 1);
        }

        @Override
        public String getColor() {
            incrementAccessesCount();
            return color;
        }

        @Override
        synchronized public int getSize() {
            incrementAccessesCount();
            return size;
        }

        @Override
        synchronized public Map<Long, Integer> getAccessesCountPerThread() {
            return accessesCountPerThread;
        }
    }

    @Test
    @Grade(value = 3, cpuTimeout = 1000)
    public void testManyBricks() {
        // make 50 red bricks with different sizes
        BrickCounter.Brick[] bricks = new BrickCounter.Brick[50];
        for(int i=0;i<bricks.length;i++) {
            bricks[i] = new MyBrick("red", i);
        }
        Collections.shuffle(Arrays.asList(bricks));

        // six boxes
        int numberOfBoxes = 6;

        // calculate box number by size
        Function<BrickCounter.Brick, Integer> sorter = brick -> {
            // bricks with sizes 0 to 8 go to box 0
            // bricks with sizes 9 to 17 go to box 1
            // etc.
            return brick.getSize()/9;
        };

        // count
        int[] counts = BrickCounter.countBricks(bricks, numberOfBoxes, sorter);
        assertEquals(numberOfBoxes, counts.length);

        // boxes 0 to 4 must contain 9 bricks.
        // box 5 must contain 5 bricks.
        assertEquals(9, counts[0]);
        assertEquals(9, counts[1]);
        assertEquals(9, counts[2]);
        assertEquals(9, counts[3]);
        assertEquals(9, counts[4]);
        assertEquals(5, counts[5]);
    }

    @Test()
    @Grade(value = 1, cpuTimeout = 1000)
    @Allow("all")
    public void testThreadsNoBrick() {
        ThreadsCheckingBrick[] bricks = new ThreadsCheckingBrick[0];

        MyExecutorService executor = new MyExecutorService(2);
        try {
            int[] counts = BrickCounter.countBricksTwoThreads(bricks, 2, brick -> 0, executor);
            assertEquals(2, counts.length);
            assertEquals(0, counts[0]);
            assertEquals(0, counts[1]);
            assertTrue(executor.getCountSubmits() <= 2);
        } finally {
            executor.shutdown();
        }
    }

    @Test()
    @Grade(value = 1, cpuTimeout = 1000)
    @Allow("all")
    public void testThreadsOneBrick() {
        ThreadsCheckingBrick[] bricks = new ThreadsCheckingBrick[]{
                new ThreadsCheckingBrick("red", 1)
        };

        MyExecutorService executor = new MyExecutorService(2);
        try {
            int[] counts = BrickCounter.countBricksTwoThreads(bricks, 2, brick -> 0, executor);
            assertEquals(2, counts.length);
            assertEquals(1, counts[0]);
            assertEquals(0, counts[1]);
            assertTrue(executor.getCountSubmits() <= 2);
            assertTrue(executor.checkThreads(bricks[0]));
            assertEquals(0, bricks[0].getAccessesCountPerThread().size());  // One brick, so no comparison is done
        } finally {
            executor.shutdown();
        }
    }

    @Test()
    @Grade(value = 4, cpuTimeout = 10000)
    @Allow("all")
    public void testThreadsManyBricks() {
        // make 499 red bricks with random sizes between 0 and 9
        Random random = new Random();
        ThreadsCheckingBrick[] bricks = new ThreadsCheckingBrick[499];
        int[] correctCounts = new int[10];
        for(int i=0;i<bricks.length;i++) {
            int size = random.nextInt(10);
            bricks[i] = new ThreadsCheckingBrick("red", size);
            // we count them, so we can later check the result
            correctCounts[size]++;
        }

        // ten boxes (one box for each brick size)
        int numberOfBoxes = 10;

        Function<BrickCounter.Brick, Integer> sorter = brick -> {
            // to make things a bit more interesting for the threads,
            // we randomly wait a few milliseconds
            try {
                Thread.sleep(random.nextInt(3));
            }
            catch(InterruptedException e) {
                throw new RuntimeException(e);
            }
            // brick goes to the box matching its size
            return brick.getSize();
        };

        // count
        MyExecutorService executor = new MyExecutorService(2);
        try {
            int[] counts = BrickCounter.countBricksTwoThreads(bricks, numberOfBoxes, sorter, executor);

            assertEquals(numberOfBoxes, counts.length);
            assertEquals(2, executor.getCountSubmits());

            // check the result. The counts returned by the method must be identical
            // to what we counted when we created the bricks
            assertArrayEquals(correctCounts, counts);

            Map<Long, Integer> allAccessesPerThread = new HashMap<>();
            for (int i = 0; i < bricks.length; i++) {
                assertTrue(executor.checkThreads(bricks[i]));
                for (Long threadId : bricks[i].getAccessesCountPerThread().keySet()) {
                    int previous = 0;
                    if (allAccessesPerThread.containsKey(threadId)) {
                        previous = allAccessesPerThread.get(threadId);
                    }
                    allAccessesPerThread.put(threadId, previous + bricks[i].getAccessesCountPerThread().get(threadId));
                }
            }
            assertEquals(2, allAccessesPerThread.size());

            int countAllAccesses = 0;
            for (Long threadId : allAccessesPerThread.keySet()) {
                countAllAccesses += allAccessesPerThread.get(threadId);
            }

            assertTrue(countAllAccesses >= 499);
            // Check that the number of accesses to the bricks are balanced per thread
            for (Long threadId : allAccessesPerThread.keySet()) {
                assertTrue(allAccessesPerThread.get(threadId) >= countAllAccesses / 2 - 10 /* some tolerance */);
            }
        } finally {
            executor.shutdown();
        }
    }
    // END STRIP
}
