package parallelization;

import org.javagrader.Allow;
import org.javagrader.Grade;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

@Grade
@Allow("java.lang.Thread")
class TrainStationsTest {
    // https://en.wikipedia.org/wiki/Haversine_formula
    private static class HaversineDistance implements TrainStations.DistanceFunction {
        private static double square(double a) {
            return a * a;
        }

        @Override
        public double compute(TrainStations.Location a,
                              TrainStations.Location b) {
            double R = 6371.0;  // Mean radius of Earth in kilometers: https://en.wikipedia.org/wiki/Earth_radius
            double phi1 = a.getLatitude() / 180.0 * Math.PI;
            double phi2 = b.getLatitude() / 180.0 * Math.PI;
            double dlambda = (b.getLongitude() - a.getLongitude()) / 180.0 * Math.PI;
            return 2.0 * R * Math.asin(
                    Math.sqrt(square(Math.sin((phi2 - phi1) / 2.0)) +
                            Math.cos(phi1) * Math.cos(phi2) * square(Math.sin(dlambda / 2.0))));
        }
    }

    private static class MyStation implements TrainStations.Station {
        private String name;
        private TrainStations.Location location;

        MyStation(String name,
                  double latitude,
                  double longitude) {
            this.name = name;
            this.location = new TrainStations.Location(latitude, longitude);
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public TrainStations.Location getLocation() {
            return location;
        }
    }

    private static final MyStation STATION_GUILLEMINS = new MyStation("Guillemins", 50.622935, 5.568461);
    private static final MyStation STATION_OTTIGNIES = new MyStation("Ottignies", 50.671632, 4.569615);
    private static final MyStation STATION_NAMUR = new MyStation("Namur", 50.469156, 4.862057);
    private static final MyStation STATION_LLN = new MyStation("Louvain-la-Neuve", 50.6696947, 4.6134822);
    private static final MyStation STATION_BRUXELLES_NORD = new MyStation("Bruxelles Nord", 50.859658, 4.360854);


    @Test
    @Grade(value = 1, cpuTimeout = 1)
    public void testGetClosestStation() {
        Optional<TrainStations.Station> c = TrainStations.getClosestStation(STATION_LLN.getLocation(), new HaversineDistance(),
                Optional.of(STATION_NAMUR), Optional.of(STATION_OTTIGNIES));
        assertNotEquals(c, null);
        assertTrue(c.isPresent());
        assertEquals("Ottignies", c.get().getName());
    }


    @Test
    @Grade(value = 1, cpuTimeout = 1)
    public void testFindClosestStationSequential() {
        List<TrainStations.Station> stations = Arrays.asList(new TrainStations.Station[]{STATION_GUILLEMINS, STATION_OTTIGNIES, STATION_NAMUR, STATION_BRUXELLES_NORD});

        for (int i = 0; i < 10; i++) {
            TrainStations.Station[] s = stations.toArray(new TrainStations.Station[0]);
            Optional<TrainStations.Station> c = TrainStations.findClosestStationSequential(STATION_LLN.getLocation(), new HaversineDistance(), s, 0, s.length);
            assertNotEquals(c, null);
            assertTrue(c.isPresent());
            assertEquals("Ottignies", c.get().getName());
            Collections.shuffle(stations);
        }
    }


    @Test
    @Grade(value = 1, cpuTimeout = 1)
    public void testFindClosestStationParallel() {
        List<TrainStations.Station> stations = Arrays.asList(new TrainStations.Station[]{STATION_GUILLEMINS, STATION_OTTIGNIES, STATION_NAMUR, STATION_BRUXELLES_NORD});

        ExecutorService executorService = Executors.newFixedThreadPool(2 /* numberOfThreads */);

        try {
            for (int i = 0; i < 20; i++) {
                TrainStations.Station[] s = stations.toArray(new TrainStations.Station[0]);
                Optional<TrainStations.Station> c = TrainStations.findClosestStationParallel(STATION_LLN.getLocation(), new HaversineDistance(), s, executorService);
                assertNotEquals(c, null);
                assertTrue(c.isPresent());
                assertEquals("Ottignies", c.get().getName());
                Collections.shuffle(stations);
            }
        } finally {
            executorService.shutdown();
        }
    }


    @Test
    @Grade(value = 1, cpuTimeout = 1)
    public void testLimitCasesGetClosestStation() {
        Optional<TrainStations.Station> c = TrainStations.getClosestStation(
                new TrainStations.Location(0, 0), new HaversineDistance(),
                Optional.of(new MyStation("a", 1, 0)), Optional.empty());
        assertNotEquals(c, null);
        assertTrue(c.isPresent());
        assertEquals("a", c.get().getName());

        c = TrainStations.getClosestStation(
                new TrainStations.Location(0, 0), new HaversineDistance(),
                Optional.empty(), Optional.of(new MyStation("b", 2, 0)));
        assertNotEquals(c, null);
        assertTrue(c.isPresent());
        assertEquals("b", c.get().getName());

        c = TrainStations.getClosestStation(
                new TrainStations.Location(0, 0), new HaversineDistance(),
                Optional.empty(), Optional.empty());
        assertNotEquals(c, null);
        assertFalse(c.isPresent());
    }


    @Test
    @Grade(value = 1, cpuTimeout = 1)
    public void testSequentialIndices() {
        TrainStations.Station[] stations = new TrainStations.Station[]{STATION_GUILLEMINS, STATION_OTTIGNIES, STATION_NAMUR, STATION_BRUXELLES_NORD};

        Optional<TrainStations.Station> c;
        c = TrainStations.findClosestStationSequential(STATION_LLN.getLocation(), new HaversineDistance(), stations, 0, 0);
        assertFalse(c.isPresent());
        c = TrainStations.findClosestStationSequential(STATION_LLN.getLocation(), new HaversineDistance(), stations, 1, 1);
        assertFalse(c.isPresent());
        c = TrainStations.findClosestStationSequential(STATION_LLN.getLocation(), new HaversineDistance(), stations, 2, 2);
        assertFalse(c.isPresent());
        c = TrainStations.findClosestStationSequential(STATION_LLN.getLocation(), new HaversineDistance(), stations, 3, 3);
        assertFalse(c.isPresent());
        c = TrainStations.findClosestStationSequential(STATION_LLN.getLocation(), new HaversineDistance(), stations, 4, 4);
        assertFalse(c.isPresent());
        c = TrainStations.findClosestStationSequential(STATION_LLN.getLocation(), new HaversineDistance(), stations, 0, 1);
        assertTrue(c.isPresent());
        assertEquals("Guillemins", c.get().getName());
        c = TrainStations.findClosestStationSequential(STATION_LLN.getLocation(), new HaversineDistance(), stations, 1, 2);
        assertTrue(c.isPresent());
        assertEquals("Ottignies", c.get().getName());
        c = TrainStations.findClosestStationSequential(STATION_LLN.getLocation(), new HaversineDistance(), stations, 2, 3);
        assertTrue(c.isPresent());
        assertEquals("Namur", c.get().getName());
        c = TrainStations.findClosestStationSequential(STATION_LLN.getLocation(), new HaversineDistance(), stations, 3, 4);
        assertTrue(c.isPresent());
        assertEquals("Bruxelles Nord", c.get().getName());
        c = TrainStations.findClosestStationSequential(STATION_LLN.getLocation(), new HaversineDistance(), stations, 0, 2);
        assertTrue(c.isPresent());
        assertEquals("Ottignies", c.get().getName());
        c = TrainStations.findClosestStationSequential(STATION_LLN.getLocation(), new HaversineDistance(), stations, 1, 3);
        assertTrue(c.isPresent());
        assertEquals("Ottignies", c.get().getName());
        c = TrainStations.findClosestStationSequential(STATION_LLN.getLocation(), new HaversineDistance(), stations, 2, 4);
        assertTrue(c.isPresent());
        assertEquals("Bruxelles Nord", c.get().getName());
        c = TrainStations.findClosestStationSequential(STATION_LLN.getLocation(), new HaversineDistance(), stations, 0, 3);
        assertTrue(c.isPresent());
        assertEquals("Ottignies", c.get().getName());
        c = TrainStations.findClosestStationSequential(STATION_LLN.getLocation(), new HaversineDistance(), stations, 1, 4);
        assertTrue(c.isPresent());
        assertEquals("Ottignies", c.get().getName());
        c = TrainStations.findClosestStationSequential(STATION_LLN.getLocation(), new HaversineDistance(), stations, 0, 4);
        assertTrue(c.isPresent());
        assertEquals("Ottignies", c.get().getName());
    }


    // BEGIN STRIP
    @Test
    @Grade(value = 1, cpuTimeout = 1)
    public void testSequential1D() {
        HaversineDistance d = new HaversineDistance();
        assertEquals(d.compute(new TrainStations.Location(51.510357, -0.116773),
                        new TrainStations.Location(38.889931, -77.009003)),
                5897.658, 0.01);

        TrainStations.Station[] s = new TrainStations.Station[10];
        for (int i = 0; i < s.length; i++) {
            s[i] = new MyStation("s" + i, i, s.length - 1 - i);
        }

        assertEquals("s0", TrainStations.findClosestStationSequential(new TrainStations.Location(-100, 13),
                (a, b) -> Math.abs(a.getLatitude() - b.getLatitude()), s, 0, s.length).get().getName());
        assertEquals("s0", TrainStations.findClosestStationSequential(new TrainStations.Location(0.4999, 1),
                (a, b) -> Math.abs(a.getLatitude() - b.getLatitude()), s, 0, s.length).get().getName());
        assertEquals("s1", TrainStations.findClosestStationSequential(new TrainStations.Location(0.5001, 7),
                (a, b) -> Math.abs(a.getLatitude() - b.getLatitude()), s, 0, s.length).get().getName());
        assertEquals("s8", TrainStations.findClosestStationSequential(new TrainStations.Location(8.4999, 10),
                (a, b) -> Math.abs(a.getLatitude() - b.getLatitude()), s, 0, s.length).get().getName());
        assertEquals("s9", TrainStations.findClosestStationSequential(new TrainStations.Location(8.5001, 9),
                (a, b) -> Math.abs(a.getLatitude() - b.getLatitude()), s, 0, s.length).get().getName());
        assertEquals("s9", TrainStations.findClosestStationSequential(new TrainStations.Location(100, 3),
                (a, b) -> Math.abs(a.getLatitude() - b.getLatitude()), s, 0, s.length).get().getName());

        assertEquals("s9", TrainStations.findClosestStationSequential(new TrainStations.Location(13, -100),
                (a, b) -> Math.abs(a.getLongitude() - b.getLongitude()), s, 0, s.length).get().getName());
        assertEquals("s9", TrainStations.findClosestStationSequential(new TrainStations.Location(1, 0.4999),
                (a, b) -> Math.abs(a.getLongitude() - b.getLongitude()), s, 0, s.length).get().getName());
        assertEquals("s8", TrainStations.findClosestStationSequential(new TrainStations.Location(7, 0.5001),
                (a, b) -> Math.abs(a.getLongitude() - b.getLongitude()), s, 0, s.length).get().getName());
        assertEquals("s1", TrainStations.findClosestStationSequential(new TrainStations.Location(10, 8.4999),
                (a, b) -> Math.abs(a.getLongitude() - b.getLongitude()), s, 0, s.length).get().getName());
        assertEquals("s0", TrainStations.findClosestStationSequential(new TrainStations.Location(9, 8.5001),
                (a, b) -> Math.abs(a.getLongitude() - b.getLongitude()), s, 0, s.length).get().getName());
        assertEquals("s0", TrainStations.findClosestStationSequential(new TrainStations.Location(3, 100),
                (a, b) -> Math.abs(a.getLongitude() - b.getLongitude()), s, 0, s.length).get().getName());
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

            public void checkAllowedThread(long threadId) {
                if (!createdThreadsId.contains(threadId)) {
                    throw new RuntimeException("You are using a thread that is not managed by the provided thread pool!");
                }
            }
        }

        private int countSubmits;
        private ExecutorService wrapped;
        private MyThreadFactory factory;

        public synchronized void resetCountSubmits() {
            countSubmits = 0;
        }

        public synchronized int getCountSubmits() {
            return countSubmits;
        }

        public synchronized void checkAllowedThread(long threadId) {
            factory.checkAllowedThread(threadId);
        }

        MyExecutorService(int nThreads) {
            factory = new MyThreadFactory();
            wrapped = Executors.newFixedThreadPool(nThreads, factory);
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
            synchronized (this) {
                countSubmits++;
            }
            return wrapped.submit(callable);
        }

        @Override
        public <T> Future<T> submit(Runnable runnable, T t) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Future<?> submit(Runnable runnable) {
            synchronized (this) {
                countSubmits++;
            }
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


    private static class ThreadedStation implements TrainStations.Station {
        private String name;
        private TrainStations.Location location;
        private Set<Long> threads;

        ThreadedStation(String name,
                        double latitude,
                        double longitude) {
            resetThreads();
            this.name = name;
            this.location = new TrainStations.Location(latitude, longitude);
        }

        @Override
        public synchronized String getName() {
            threads.add(Thread.currentThread().getId());
            return name;
        }

        @Override
        public synchronized TrainStations.Location getLocation() {
            threads.add(Thread.currentThread().getId());
            return location;
        }

        public synchronized void resetThreads() {
            threads = new HashSet<>();
            threads.add(Thread.currentThread().getId());
        }

        // This method must be executed only by the main thread
        public synchronized void checkThreads(MyExecutorService executorService) {
            long mainThreadId = Thread.currentThread().getId();
            assertEquals(2, threads.size(), "The same station is accessed by multiple threads from the thread pool!");
            assertTrue(threads.contains(mainThreadId));

            for (Long threadId: threads) {
                if (threadId != mainThreadId) {
                    executorService.checkAllowedThread(threadId);
                }
            }
        }
    }


    private static void checkAndResetStations(MyExecutorService executorService,
                                              ThreadedStation[] stations) {
        assertEquals(2, executorService.getCountSubmits(), "You don't use two threads from the thread pool!");

        for (int i = 0; i < stations.length; i++) {
            stations[i].checkThreads(executorService);
            stations[i].resetThreads();
        }

        executorService.resetCountSubmits();
    }


    @Test
    @Grade(value = 2, cpuTimeout = 1)
    public void testParallel1D() {
        ThreadedStation[] s = new ThreadedStation[10];
        for (int i = 0; i < s.length; i++) {
            s[i] = new ThreadedStation("s" + i, i, s.length - 1 - i);
        }

        MyExecutorService executorService = new MyExecutorService(2 /* numberOfThreads */);

        try {
            assertEquals("s0", TrainStations.findClosestStationParallel(new TrainStations.Location(-100, 13),
                    (a, b) -> Math.abs(a.getLatitude() - b.getLatitude()), s, executorService).get().getName());
            checkAndResetStations(executorService, s);

            assertEquals("s0", TrainStations.findClosestStationParallel(new TrainStations.Location(0.4999, 1),
                    (a, b) -> Math.abs(a.getLatitude() - b.getLatitude()), s, executorService).get().getName());
            checkAndResetStations(executorService, s);

            assertEquals("s1", TrainStations.findClosestStationParallel(new TrainStations.Location(0.5001, 7),
                    (a, b) -> Math.abs(a.getLatitude() - b.getLatitude()), s, executorService).get().getName());
            checkAndResetStations(executorService, s);

            assertEquals("s8", TrainStations.findClosestStationParallel(new TrainStations.Location(8.4999, 10),
                    (a, b) -> Math.abs(a.getLatitude() - b.getLatitude()), s, executorService).get().getName());
            checkAndResetStations(executorService, s);

            assertEquals("s9", TrainStations.findClosestStationParallel(new TrainStations.Location(8.5001, 9),
                    (a, b) -> Math.abs(a.getLatitude() - b.getLatitude()), s, executorService).get().getName());
            checkAndResetStations(executorService, s);

            assertEquals("s9", TrainStations.findClosestStationParallel(new TrainStations.Location(100, 3),
                    (a, b) -> Math.abs(a.getLatitude() - b.getLatitude()), s, executorService).get().getName());
            checkAndResetStations(executorService, s);

            assertEquals("s9", TrainStations.findClosestStationParallel(new TrainStations.Location(13, -100),
                    (a, b) -> Math.abs(a.getLongitude() - b.getLongitude()), s, executorService).get().getName());
            checkAndResetStations(executorService, s);

            assertEquals("s9", TrainStations.findClosestStationParallel(new TrainStations.Location(1, 0.4999),
                    (a, b) -> Math.abs(a.getLongitude() - b.getLongitude()), s, executorService).get().getName());
            checkAndResetStations(executorService, s);

            assertEquals("s8", TrainStations.findClosestStationParallel(new TrainStations.Location(7, 0.5001),
                    (a, b) -> Math.abs(a.getLongitude() - b.getLongitude()), s, executorService).get().getName());
            checkAndResetStations(executorService, s);

            assertEquals("s1", TrainStations.findClosestStationParallel(new TrainStations.Location(10, 8.4999),
                    (a, b) -> Math.abs(a.getLongitude() - b.getLongitude()), s, executorService).get().getName());
            checkAndResetStations(executorService, s);

            assertEquals("s0", TrainStations.findClosestStationParallel(new TrainStations.Location(9, 8.5001),
                    (a, b) -> Math.abs(a.getLongitude() - b.getLongitude()), s, executorService).get().getName());
            checkAndResetStations(executorService, s);

            assertEquals("s0", TrainStations.findClosestStationParallel(new TrainStations.Location(3, 100),
                    (a, b) -> Math.abs(a.getLongitude() - b.getLongitude()), s, executorService).get().getName());
            checkAndResetStations(executorService, s);
        } finally {
            executorService.shutdown();
        }
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1)
    public void testLimitCasesParallel() {
        MyExecutorService executorService = new MyExecutorService(2 /* numberOfThreads */);

        try {
            assertEquals("Ottignies", TrainStations.findClosestStationParallel(STATION_LLN.getLocation(), new HaversineDistance(),
                    new TrainStations.Station[]{STATION_GUILLEMINS, STATION_OTTIGNIES}, executorService).get().getName());
            assertEquals(2, executorService.getCountSubmits());
            executorService.resetCountSubmits();

            assertEquals("Ottignies", TrainStations.findClosestStationParallel(STATION_LLN.getLocation(), new HaversineDistance(),
                    new TrainStations.Station[]{STATION_OTTIGNIES}, executorService).get().getName());
            assertEquals(2, executorService.getCountSubmits());
            executorService.resetCountSubmits();

            assertEquals("Guillemins", TrainStations.findClosestStationParallel(STATION_LLN.getLocation(), new HaversineDistance(),
                    new TrainStations.Station[]{STATION_GUILLEMINS}, executorService).get().getName());
            assertEquals(2, executorService.getCountSubmits());
            executorService.resetCountSubmits();

            assertFalse(TrainStations.findClosestStationParallel(STATION_LLN.getLocation(), new HaversineDistance(),
                    new TrainStations.Station[0], executorService).isPresent());
            assertEquals(2, executorService.getCountSubmits());
            executorService.resetCountSubmits();
        } finally {
            executorService.shutdown();
        }
    }
    // END STRIP
}
