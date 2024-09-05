package parallelization;

import org.javagrader.Allow;
import org.javagrader.Grade;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Grade
@Allow("java.lang.Thread")
public class StadiumStatisticsTest {
    private static class MySupporter implements StadiumStatistics.Supporter {
        private String nationality;
        private int age;

        public MySupporter(String nationality,
                           int age) {
            this.nationality = nationality;
            this.age = age;
        }

        @Override
        public String getNationality() {
            return nationality;
        }

        @Override
        public int getAge() {
            return age;
        }
    }

    private void checkHistogram(StadiumStatistics.AgeHistogram histogram,
                                int between_0_and_9,
                                int between_10_and_19,
                                int between_20_and_29,
                                int between_30_and_39,
                                int between_40_and_49,
                                int between_50_and_59,
                                int between_60_and_69,
                                int between_70_and_79,
                                int above_80) {
        assertEquals(between_0_and_9, histogram.getNumberOfSupporters(StadiumStatistics.AgeCategory.BETWEEN_0_AND_9));
        assertEquals(between_10_and_19, histogram.getNumberOfSupporters(StadiumStatistics.AgeCategory.BETWEEN_10_AND_19));
        assertEquals(between_20_and_29, histogram.getNumberOfSupporters(StadiumStatistics.AgeCategory.BETWEEN_20_AND_29));
        assertEquals(between_30_and_39, histogram.getNumberOfSupporters(StadiumStatistics.AgeCategory.BETWEEN_30_AND_39));
        assertEquals(between_40_and_49, histogram.getNumberOfSupporters(StadiumStatistics.AgeCategory.BETWEEN_40_AND_49));
        assertEquals(between_50_and_59, histogram.getNumberOfSupporters(StadiumStatistics.AgeCategory.BETWEEN_50_AND_59));
        assertEquals(between_60_and_69, histogram.getNumberOfSupporters(StadiumStatistics.AgeCategory.BETWEEN_60_AND_69));
        assertEquals(between_70_and_79, histogram.getNumberOfSupporters(StadiumStatistics.AgeCategory.BETWEEN_70_AND_79));
        assertEquals(above_80, histogram.getNumberOfSupporters(StadiumStatistics.AgeCategory.ABOVE_80));
    }

    @Test
    @Grade(value = 2, cpuTimeout = 1)
    public void testHistogram() {
        StadiumStatistics.AgeHistogram h = new StadiumStatistics.AgeHistogram();
        checkHistogram(h, 0, 0, 0, 0, 0, 0, 0, 0, 0);

        for (int i = 0; i < 120; i++) {
            h.addSupporter(i);
            checkHistogram(h,
                    Math.min(10, i + 1),
                    Math.max(0, Math.min(20, i + 1) - 10),
                    Math.max(0, Math.min(30, i + 1) - 20),
                    Math.max(0, Math.min(40, i + 1) - 30),
                    Math.max(0, Math.min(50, i + 1) - 40),
                    Math.max(0, Math.min(60, i + 1) - 50),
                    Math.max(0, Math.min(70, i + 1) - 60),
                    Math.max(0, Math.min(80, i + 1) - 70),
                    Math.max(0, i + 1 - 80));
        }
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1)
    public void testHistogramException() {
        StadiumStatistics.AgeHistogram h = new StadiumStatistics.AgeHistogram();
        assertThrows(IllegalArgumentException.class, () -> h.addSupporter(-1));
    }

    @Test
    @Grade(value = 2, cpuTimeout = 1)
    public void testCombine() {
        StadiumStatistics.AgeHistogram h1 = new StadiumStatistics.AgeHistogram();
        StadiumStatistics.AgeHistogram h2 = new StadiumStatistics.AgeHistogram();
        for (int i = 0; i < 100; i++) {
            h1.addSupporter(i);
            h2.addSupporter(i);
        }
        checkHistogram(StadiumStatistics.AgeHistogram.combine(h1, h2), 20, 20, 20, 20, 20, 20, 20, 20, 40);
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1)
    public void getEmpty() {
        StadiumStatistics.Supporter supporters[] = new StadiumStatistics.Supporter[0];
        StadiumStatistics.AgeHistogram h = StadiumStatistics.computeAgeHistogramSequential(supporters, "", 0, 0);
        checkHistogram(h, 0, 0, 0, 0, 0, 0, 0, 0, 0);
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1)
    public void testBasicSequential1() {
        StadiumStatistics.Supporter supporters[] = new StadiumStatistics.Supporter[100];
        for (int i = 0; i < supporters.length; i++) {
            supporters[i] = new MySupporter("Belgian", i);
        }

        StadiumStatistics.AgeHistogram h = StadiumStatistics.computeAgeHistogramSequential(supporters, "French", 0, supporters.length);
        checkHistogram(h, 0, 0, 0, 0, 0, 0, 0, 0, 0);

        h = StadiumStatistics.computeAgeHistogramSequential(supporters, "Belgian", 0, supporters.length);
        checkHistogram(h, 10, 10, 10, 10, 10, 10, 10, 10, 20);
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1)
    public void testBasicSequential2() {
        StadiumStatistics.Supporter supporters[] = new StadiumStatistics.Supporter[100];
        for (int i = 0; i < supporters.length / 2; i++) {
            supporters[i] = new MySupporter("French", i);
        }
        for (int i = supporters.length / 2; i < supporters.length; i++) {
            supporters[i] = new MySupporter("Belgian", i);
        }

        StadiumStatistics.AgeHistogram h = StadiumStatistics.computeAgeHistogramSequential(supporters, "French", 0, supporters.length);
        checkHistogram(h, 10, 10, 10, 10, 10, 0, 0, 0, 0);

        h = StadiumStatistics.computeAgeHistogramSequential(supporters, "Belgian", 0, supporters.length);
        checkHistogram(h, 0, 0, 0, 0, 0, 10, 10, 10, 20);
    }


    @Test
    @Grade(value = 1, cpuTimeout = 1)
    public void testBasicParallel1() {
        StadiumStatistics.Supporter supporters[] = new StadiumStatistics.Supporter[100];
        for (int i = 0; i < supporters.length; i++) {
            supporters[i] = new MySupporter("Belgian", i);
        }

        ExecutorService executorService = Executors.newFixedThreadPool(2 /* numberOfThreads */);

        try {
            StadiumStatistics.AgeHistogram h = StadiumStatistics.computeAgeHistogramParallel(executorService, supporters, "French");
            checkHistogram(h, 0, 0, 0, 0, 0, 0, 0, 0, 0);

            h = StadiumStatistics.computeAgeHistogramParallel(executorService, supporters, "Belgian");
            checkHistogram(h, 10, 10, 10, 10, 10, 10, 10, 10, 20);
        } finally {
            executorService.shutdown();
        }
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1)
    public void testBasicParallel2() {
        StadiumStatistics.Supporter supporters[] = new StadiumStatistics.Supporter[100];
        for (int i = 0; i < supporters.length / 2; i++) {
            supporters[i] = new MySupporter("French", i);
        }
        for (int i = supporters.length / 2; i < supporters.length; i++) {
            supporters[i] = new MySupporter("Belgian", i);
        }

        ExecutorService executorService = Executors.newFixedThreadPool(2 /* numberOfThreads */);

        try {
            StadiumStatistics.AgeHistogram h = StadiumStatistics.computeAgeHistogramParallel(executorService, supporters, "French");
            checkHistogram(h, 10, 10, 10, 10, 10, 0, 0, 0, 0);

            h = StadiumStatistics.computeAgeHistogramParallel(executorService, supporters, "Belgian");
            checkHistogram(h, 0, 0, 0, 0, 0, 10, 10, 10, 20);
        } finally {
            executorService.shutdown();
        }
    }

    // BEGIN STRIP

    private interface SupporterFactory {
        public StadiumStatistics.Supporter build(String nationality,
                                                 int age);
    }

    private class MySupporterFactory implements SupporterFactory {
        @Override
        public StadiumStatistics.Supporter build(String nationality,
                                                 int age) {
            return new MySupporter(nationality, age);
        }
    }

    private static class TestHistogram {
        private int count[] = new int[9];

        void add(int age) {
            count[Math.min(count.length - 1, age / 10)] += 1;
        }

        void check(StadiumStatistics.AgeHistogram histogram) {
            assertEquals(count[0], histogram.getNumberOfSupporters(StadiumStatistics.AgeCategory.BETWEEN_0_AND_9));
            assertEquals(count[1], histogram.getNumberOfSupporters(StadiumStatistics.AgeCategory.BETWEEN_10_AND_19));
            assertEquals(count[2], histogram.getNumberOfSupporters(StadiumStatistics.AgeCategory.BETWEEN_20_AND_29));
            assertEquals(count[3], histogram.getNumberOfSupporters(StadiumStatistics.AgeCategory.BETWEEN_30_AND_39));
            assertEquals(count[4], histogram.getNumberOfSupporters(StadiumStatistics.AgeCategory.BETWEEN_40_AND_49));
            assertEquals(count[5], histogram.getNumberOfSupporters(StadiumStatistics.AgeCategory.BETWEEN_50_AND_59));
            assertEquals(count[6], histogram.getNumberOfSupporters(StadiumStatistics.AgeCategory.BETWEEN_60_AND_69));
            assertEquals(count[7], histogram.getNumberOfSupporters(StadiumStatistics.AgeCategory.BETWEEN_70_AND_79));
            assertEquals(count[8], histogram.getNumberOfSupporters(StadiumStatistics.AgeCategory.ABOVE_80));
        }
    }

    private static String NATIONALITIES[] = {
            "Belgian",
            "French",
            "Spanish"
    };

    private static StadiumStatistics.Supporter createRandomSupporter(TestHistogram expectedBelgian,
                                                                     TestHistogram expectedFrench,
                                                                     TestHistogram expectedSpanish,
                                                                     Random random,
                                                                     SupporterFactory factory) {
        int nationality = random.nextInt(NATIONALITIES.length);
        int age = random.nextInt(100);
        if (nationality == 0) {
            expectedBelgian.add(age);
        } else if (nationality == 1) {
            expectedFrench.add(age);
        } else {
            expectedSpanish.add(age);
        }
        return factory.build(NATIONALITIES[nationality], age);
    }

    @Test
    @Grade(value = 2, cpuTimeout = 1)
    public void testRandomSequential() {
        TestHistogram expectedBelgian = new TestHistogram();
        TestHistogram expectedFrench = new TestHistogram();
        TestHistogram expectedSpanish = new TestHistogram();

        Random random = new Random(42 /* always generated the same sequence */);
        StadiumStatistics.Supporter supporters[] = new StadiumStatistics.Supporter[1000];
        for (int i = 0; i < supporters.length; i++) {
            supporters[i] = createRandomSupporter(expectedBelgian, expectedFrench, expectedSpanish, random, new MySupporterFactory());
        }

        expectedBelgian.check(StadiumStatistics.computeAgeHistogramSequential(supporters, "Belgian", 0, supporters.length));
        expectedFrench.check(StadiumStatistics.computeAgeHistogramSequential(supporters, "French", 0, supporters.length));
        expectedSpanish.check(StadiumStatistics.computeAgeHistogramSequential(supporters, "Spanish", 0, supporters.length));
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1)
    public void testRandomParallel() {
        TestHistogram expectedBelgian = new TestHistogram();
        TestHistogram expectedFrench = new TestHistogram();
        TestHistogram expectedSpanish = new TestHistogram();

        Random random = new Random(43 /* always generated the same sequence */);
        StadiumStatistics.Supporter supporters[] = new StadiumStatistics.Supporter[1000];
        for (int i = 0; i < supporters.length; i++) {
            supporters[i] = createRandomSupporter(expectedBelgian, expectedFrench, expectedSpanish, random, new MySupporterFactory());
        }

        ExecutorService executorService = Executors.newFixedThreadPool(2 /* numberOfThreads */);

        try {
            expectedBelgian.check(StadiumStatistics.computeAgeHistogramParallel(executorService, supporters, "Belgian"));
            expectedFrench.check(StadiumStatistics.computeAgeHistogramParallel(executorService, supporters, "French"));
            expectedSpanish.check(StadiumStatistics.computeAgeHistogramParallel(executorService, supporters, "Spanish"));
        } finally {
            executorService.shutdown();
        }
    }

    public static class InstrumentedThreadObject {
        private Set<Long> readerThreads;
        private Set<Long> writerThreads;

        private static Long getCurrentThreadId() {
            return Thread.currentThread().getId();
        }

        public InstrumentedThreadObject() {
            resetThreads();
        }

        public synchronized void logReadAccess() {
            readerThreads.add(getCurrentThreadId());
        }

        public synchronized void logWriteAccess() {
            writerThreads.add(getCurrentThreadId());
        }

        public synchronized void resetThreads() {
            readerThreads = new HashSet<>();
            writerThreads = new HashSet<>();
        }

        public synchronized Set<Long> getReaderThreads() {
            return new HashSet<>(readerThreads);
        }

        public synchronized Set<Long> getWriterThreads() {
            return new HashSet<>(writerThreads);
        }
    }

    private static class InstrumentedPoolThread implements ExecutorService {
        private static class MyThreadFactory implements ThreadFactory {
            private final Set<Long> createdThreadsId = new HashSet<>();

            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                synchronized (this) {
                    createdThreadsId.add(thread.getId());
                }
                return thread;
            }

            public synchronized int getPoolSize() {
                return createdThreadsId.size();
            }

            public synchronized boolean isManagedThread(long threadId) {
                return createdThreadsId.contains(threadId);
            }
        }

        private int countSubmits;
        private long mainThreadId;
        private ExecutorService wrapped;
        private MyThreadFactory factory;

        private static Long getCurrentThreadId() {
            return Thread.currentThread().getId();
        }

        private void checkIsMainThread() {
            if (getCurrentThreadId() != mainThreadId) {
                throw new IllegalStateException("This method can only be called from the main thread");
            }
        }

        public synchronized void resetCountSubmits() {
            countSubmits = 0;
        }

        public synchronized int getCountSubmits() {
            return countSubmits;
        }

        public InstrumentedPoolThread(int nThreads) {
            mainThreadId = getCurrentThreadId();
            factory = new MyThreadFactory();
            wrapped = Executors.newFixedThreadPool(nThreads, factory);
        }

        @Override
        public void shutdown() {
            checkIsMainThread();
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
            checkIsMainThread();
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
            checkIsMainThread();
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

        private static void addAccessesPerThread(Map<Long, Long> accesses,
                                                 Set<Long> threadIds) {
            for (Long threadId : threadIds) {
                if (accesses.containsKey(threadId)) {
                    accesses.put(threadId, accesses.get(threadId) + 1);
                } else {
                    accesses.put(threadId, 1L);
                }
            }
        }

        private static Map<Long, Long> countReadAccessesPerThread(Collection<InstrumentedThreadObject> objects) {
            Map<Long, Long> accesses = new HashMap<>();
            for (InstrumentedThreadObject o : objects) {
                addAccessesPerThread(accesses, o.getReaderThreads());
            }
            return accesses;
        }

        private static Map<Long, Long> countWriteAccessesPerThread(Collection<InstrumentedThreadObject> objects) {
            Map<Long, Long> accesses = new HashMap<>();
            for (InstrumentedThreadObject o : objects) {
                addAccessesPerThread(accesses, o.getWriterThreads());
            }
            return accesses;
        }

        public void checkOnlyAccessedFromThreadPool(Collection<InstrumentedThreadObject> objects) {
            checkIsMainThread();
            for (InstrumentedThreadObject o : objects) {
                boolean ok = true;
                for (Long threadId : o.getReaderThreads()) {
                    if (!factory.isManagedThread(threadId)) {
                        ok = false;
                    }
                }
                for (Long threadId : o.getWriterThreads()) {
                    if (!factory.isManagedThread(threadId)) {
                        ok = false;
                    }
                }
                if (!ok) {
                    throw new IllegalStateException("You are accessing an object from a thread that is not part of the provided thread pool");
                }
            }
        }

        public void checkAccessedByOneSingleThread(Collection<InstrumentedThreadObject> objects) {
            checkIsMainThread();
            for (InstrumentedThreadObject o : objects) {
                Set<Long> threadIs = new HashSet<>();
                threadIs.addAll(o.getReaderThreads());
                threadIs.addAll(o.getWriterThreads());
                if (threadIs.size() == 0) {
                    throw new IllegalStateException("One of the provided objects has never been accessed");
                } else if (threadIs.size() > 1) {
                    throw new IllegalStateException("One of the provided objects has been access by more than 1 thread");
                }
            }
        }

        private static long integerCeiling(long a,
                                           long b) {
            if (a % b == 0) {
                return a / b;
            } else {
                return a / b + 1;
            }
        }

        private void checkBalancedAccesses(Collection<InstrumentedThreadObject> objects,
                                           int numberOfThreads,
                                           Map<Long, Long> accesses,
                                           String typeOfThreads) {
            if (objects.size() > numberOfThreads &&
                    getCountSubmits() < numberOfThreads) {
                throw new IllegalStateException("You have not created enough " + typeOfThreads + " threads");
            }

            long totalAccesses = 0;
            for (Long threadId : accesses.keySet()) {
                totalAccesses += accesses.get(threadId);
            }
            if (totalAccesses < objects.size()) {
                throw new IllegalStateException("Your " + typeOfThreads + " threads have not access all the objects in the collection");
            }
            if (totalAccesses > objects.size()) {
                throw new IllegalStateException("Your " + typeOfThreads + " threads have access all the same objects in the collection multiple times");
            }

            long minimumAccesses = objects.size() / numberOfThreads;
            long maximumAccesses = integerCeiling(objects.size(), numberOfThreads);
            for (Long threadId : accesses.keySet()) {
                long v = accesses.get(threadId);
                if (v < minimumAccesses ||
                        v > maximumAccesses) {
                    throw new IllegalStateException("Your accesses are not balanced between the " + typeOfThreads + " threads");
                }
            }
        }

        public void checkBalancedReads(Collection<InstrumentedThreadObject> objects,
                                       int numberOfThreads) {
            checkBalancedAccesses(objects, numberOfThreads, countReadAccessesPerThread(objects), "reader");
        }

        public void checkBalancedWrites(Collection<InstrumentedThreadObject> objects,
                                        int numberOfThreads) {
            checkBalancedAccesses(objects, numberOfThreads, countWriteAccessesPerThread(objects), "writer");
        }

        public void checkNoRead(Collection<InstrumentedThreadObject> objects) {
            checkIsMainThread();
            Map<Long, Long> accesses = countReadAccessesPerThread(objects);
            if (accesses.size() > 0) {
                throw new IllegalStateException("Your threads are doing read accesses, which is not allowed");
            }
        }

        public void checkNoWrite(Collection<InstrumentedThreadObject> objects) {
            checkIsMainThread();
            Map<Long, Long> accesses = countWriteAccessesPerThread(objects);
            if (accesses.size() > 0) {
                throw new IllegalStateException("Your threads are doing write accesses, which is not allowed");
            }
        }
    }

    private static class InstrumentedSupporter extends InstrumentedThreadObject implements StadiumStatistics.Supporter {
        private String nationality;
        private int age;

        public InstrumentedSupporter(String nationality,
                                     int age) {
            this.nationality = nationality;
            this.age = age;
        }

        @Override
        public String getNationality() {
            logReadAccess();
            return nationality;
        }

        @Override
        public int getAge() {
            logReadAccess();
            return age;
        }
    }

    private class InstrumentedSupporterFactory implements SupporterFactory {
        @Override
        public StadiumStatistics.Supporter build(String nationality,
                                                 int age) {
            return new InstrumentedSupporter(nationality, age);
        }
    }

    @Test
    @Grade(value = 4, cpuTimeout = 1)
    public void testBalancedThreads() {
        TestHistogram expectedBelgian = new TestHistogram();
        TestHistogram expectedFrench = new TestHistogram();
        TestHistogram expectedSpanish = new TestHistogram();

        Random random = new Random(44 /* always generated the same sequence */);
        StadiumStatistics.Supporter supporters[] = new StadiumStatistics.Supporter[1000];
        List<InstrumentedThreadObject> instrumentedSupporters = new LinkedList<>();
        for (int i = 0; i < supporters.length; i++) {
            supporters[i] = createRandomSupporter(expectedBelgian, expectedFrench, expectedSpanish, random, new InstrumentedSupporterFactory());
            instrumentedSupporters.add((InstrumentedThreadObject) supporters[i]);
        }

        InstrumentedPoolThread executorService = new InstrumentedPoolThread(2);

        try {
            expectedBelgian.check(StadiumStatistics.computeAgeHistogramParallel(executorService, supporters, "Belgian"));
            executorService.checkOnlyAccessedFromThreadPool(instrumentedSupporters);
            executorService.checkAccessedByOneSingleThread(instrumentedSupporters);
            executorService.checkBalancedReads(instrumentedSupporters, 2);
            executorService.checkNoWrite(instrumentedSupporters);
        } finally {
            executorService.shutdown();
        }
    }
    // END STRIP
}
