package parallelization;

import org.javagrader.Allow;
import org.javagrader.Grade;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.*;

@Grade
@Allow("java.lang.Thread")
public class DiskCatalogTest {
    private class MyDisk implements DiskCatalog.Disk {
        private String bandName;
        private String diskTitle;
        private int year;

        MyDisk(String bandName,
               String diskTitle,
               int year) {
            this.bandName = bandName;
            this.diskTitle = diskTitle;
            this.year = year;
        }

        @Override
        public String getBandName() {
            return bandName;
        }

        @Override
        public String getDiskTitle() {
            return diskTitle;
        }

        @Override
        public int getYear() {
            return year;
        }
    }

    private List<DiskCatalog.Disk> getTestCollection() {
        List<DiskCatalog.Disk> c = new LinkedList<DiskCatalog.Disk>();
        c.add(new MyDisk("Pink Floyd", "The Dark Side of the Moon", 1973));
        c.add(new MyDisk("Oasis", "Heathen Chemistry", 2002));
        c.add(new MyDisk("Guns N' Roses",  "Appetite for Destruction", 1987));
        c.add(new MyDisk("Imagine Dragons", "Night Visions", 2012));
        c.add(new MyDisk("Oasis", "Definitely Maybe", 1994));
        c.add(new MyDisk("Oasis", "Be Here Now", 1997));
        c.add(new MyDisk("Imagine Dragons", "Origins", 2018));
        c.add(new MyDisk("Imagine Dragons", "Evolve", 2017));
        c.add(new MyDisk("Nirvana", "Nevermind", 1991));
        c.add(new MyDisk("Radiohead", "OK Computer", 1997));
        c.add(new MyDisk("The Beatles", "Abbey Road", 1969));
        c.add(new MyDisk("The Smiths", "The Queen is Dead", 1986));
        c.add(new MyDisk("The Doors", "The Doors", 1967));
        c.add(new MyDisk("Queen", "A Night at the Opera", 1975));
        c.add(new MyDisk("Boston", "Boston", 1976));
        c.add(new MyDisk("The Beatles", "Let It Be", 1970));
        c.add(new MyDisk("The Replacements", "Let It Be", 1984));
        c.add(new MyDisk("The Cure", "Faith", 1981));
        c.add(new MyDisk("George Michael", "Faith", 1987));
        c.add(new MyDisk("U2", "Pop", 1997));
        c.add(new MyDisk("GAS", "Pop", 2000));
        c.add(new MyDisk("Badfinger", "Wish You Were Here", 1974));
        c.add(new MyDisk("Pink Floyd", "Wish You Were Here", 1975));
        return c;
    }

    private int execute(List<DiskCatalog.Disk> disks,
                        Optional<String> bandName,
                        Optional<String> diskTitle,
                        Optional<Integer> year,
                        int skip) {
        return new DiskCatalog.CountMatchingDisksCallable(disks.iterator(), bandName, diskTitle, year, skip).call();        
    }
    
    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testSequential() {
        List<DiskCatalog.Disk> c = getTestCollection();
        for (int i = 0; i < 10; i++) {
            assertEquals(2, execute(c, Optional.of("The Beatles"), Optional.empty(), Optional.empty(), 0));
            assertEquals(3, execute(c, Optional.of("Oasis"), Optional.empty(), Optional.empty(), 0));
            assertEquals(2, execute(c, Optional.empty(), Optional.of("Faith"), Optional.empty(), 0));
            assertEquals(2, execute(c, Optional.empty(), Optional.of("Wish You Were Here"), Optional.empty(), 0));
            assertEquals(3, execute(c, Optional.empty(), Optional.empty(), Optional.of(1997), 0));
            assertEquals(1, execute(c, Optional.empty(), Optional.empty(), Optional.of(2000), 0));

            assertEquals(1, execute(c, Optional.of("Pink Floyd"), Optional.of("Wish You Were Here"), Optional.empty(), 0));
            assertEquals(1, execute(c, Optional.of("Oasis"), Optional.empty(), Optional.of(1997), 0));
            assertEquals(1, execute(c, Optional.empty(), Optional.of("Pop"), Optional.of(1997), 0));
            assertEquals(1, execute(c, Optional.of("Oasis"), Optional.of("Be Here Now"), Optional.of(1997), 0));

            assertEquals(0, execute(c, Optional.of("Nope"), Optional.of("Be Here Now"), Optional.of(1997), 0));
            assertEquals(0, execute(c, Optional.of("Oasis"), Optional.of("Nope"), Optional.of(1997), 0));
            assertEquals(0, execute(c, Optional.of("Oasis"), Optional.of("Be Here Now"), Optional.of(2000), 0));

            assertEquals(23, execute(c, Optional.empty(), Optional.empty(), Optional.empty(), 0));  // Matches any album

            Collections.shuffle(c);
        }
    }
    
    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testSkip() {
        // Pink Floyd has albums #0 and #22
        List<DiskCatalog.Disk> c = getTestCollection();
        assertEquals(2, execute(c, Optional.of("Pink Floyd"), Optional.empty(), Optional.empty(), 0));
        assertEquals(2, execute(c, Optional.of("Pink Floyd"), Optional.empty(), Optional.empty(), 1));
        assertEquals(1, execute(c, Optional.of("Pink Floyd"), Optional.empty(), Optional.empty(), 2));
        assertEquals(1, execute(c, Optional.of("Pink Floyd"), Optional.empty(), Optional.empty(), 3));
        assertEquals(1, execute(c, Optional.of("Pink Floyd"), Optional.empty(), Optional.empty(), 4));
        assertEquals(1, execute(c, Optional.of("Pink Floyd"), Optional.empty(), Optional.empty(), 5));
        assertEquals(1, execute(c, Optional.of("Pink Floyd"), Optional.empty(), Optional.empty(), 6));
        assertEquals(1, execute(c, Optional.of("Pink Floyd"), Optional.empty(), Optional.empty(), 7));
        assertEquals(1, execute(c, Optional.of("Pink Floyd"), Optional.empty(), Optional.empty(), 8));
        assertEquals(1, execute(c, Optional.of("Pink Floyd"), Optional.empty(), Optional.empty(), 9));
        assertEquals(2, execute(c, Optional.of("Pink Floyd"), Optional.empty(), Optional.empty(), 10));
        assertEquals(1, execute(c, Optional.of("Pink Floyd"), Optional.empty(), Optional.empty(), 11));
        assertEquals(1, execute(c, Optional.of("Pink Floyd"), Optional.empty(), Optional.empty(), 12));
        assertEquals(1, execute(c, Optional.of("Pink Floyd"), Optional.empty(), Optional.empty(), 13));
        assertEquals(1, execute(c, Optional.of("Pink Floyd"), Optional.empty(), Optional.empty(), 14));
        assertEquals(1, execute(c, Optional.of("Pink Floyd"), Optional.empty(), Optional.empty(), 15));
        assertEquals(1, execute(c, Optional.of("Pink Floyd"), Optional.empty(), Optional.empty(), 16));
        assertEquals(1, execute(c, Optional.of("Pink Floyd"), Optional.empty(), Optional.empty(), 17));
        assertEquals(1, execute(c, Optional.of("Pink Floyd"), Optional.empty(), Optional.empty(), 18));
        assertEquals(1, execute(c, Optional.of("Pink Floyd"), Optional.empty(), Optional.empty(), 19));
        assertEquals(1, execute(c, Optional.of("Pink Floyd"), Optional.empty(), Optional.empty(), 20));
        assertEquals(2, execute(c, Optional.of("Pink Floyd"), Optional.empty(), Optional.empty(), 21));
        assertEquals(1, execute(c, Optional.of("Pink Floyd"), Optional.empty(), Optional.empty(), 22));
        assertEquals(1, execute(c, Optional.of("Pink Floyd"), Optional.empty(), Optional.empty(), 23));
        assertEquals(1, execute(c, Optional.of("Pink Floyd"), Optional.empty(), Optional.empty(), 24));
    }
    
    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testOneThread() throws ExecutionException, InterruptedException {
        List<DiskCatalog.Disk> c = getTestCollection();
        ExecutorService threadPool = Executors.newFixedThreadPool(2);
            
        try {
            for (int i = 0; i < 10; i++) {
                assertEquals(2, DiskCatalog.countMatchingDisks(c, Optional.of("The Beatles"), Optional.empty(), Optional.empty(), threadPool, 1));
                assertEquals(3, DiskCatalog.countMatchingDisks(c, Optional.of("Oasis"), Optional.empty(), Optional.empty(), threadPool, 1));
                assertEquals(2, DiskCatalog.countMatchingDisks(c, Optional.empty(), Optional.of("Faith"), Optional.empty(), threadPool, 1));
                assertEquals(2, DiskCatalog.countMatchingDisks(c, Optional.empty(), Optional.of("Wish You Were Here"), Optional.empty(), threadPool, 1));
                assertEquals(3, DiskCatalog.countMatchingDisks(c, Optional.empty(), Optional.empty(), Optional.of(1997), threadPool, 1));
                assertEquals(1, DiskCatalog.countMatchingDisks(c, Optional.empty(), Optional.empty(), Optional.of(2000), threadPool, 1));

                assertEquals(1, DiskCatalog.countMatchingDisks(c, Optional.of("Pink Floyd"), Optional.of("Wish You Were Here"), Optional.empty(), threadPool, 1));
                assertEquals(1, DiskCatalog.countMatchingDisks(c, Optional.of("Oasis"), Optional.empty(), Optional.of(1997), threadPool, 1));
                assertEquals(1, DiskCatalog.countMatchingDisks(c, Optional.empty(), Optional.of("Pop"), Optional.of(1997), threadPool, 1));
                assertEquals(1, DiskCatalog.countMatchingDisks(c, Optional.of("Oasis"), Optional.of("Be Here Now"), Optional.of(1997), threadPool, 1));

                assertEquals(0, DiskCatalog.countMatchingDisks(c, Optional.of("Nope"), Optional.of("Be Here Now"), Optional.of(1997), threadPool, 1));
                assertEquals(0, DiskCatalog.countMatchingDisks(c, Optional.of("Oasis"), Optional.of("Nope"), Optional.of(1997), threadPool, 1));
                assertEquals(0, DiskCatalog.countMatchingDisks(c, Optional.of("Oasis"), Optional.of("Be Here Now"), Optional.of(2000), threadPool, 1));

                assertEquals(23, DiskCatalog.countMatchingDisks(c, Optional.empty(), Optional.empty(), Optional.empty(), threadPool, 1));  // Matches any album
                Collections.shuffle(c);
            }
        } finally {
            threadPool.shutdown();
        }
    }

    // BEGIN STRIP
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

        private MyThreadFactory factory;
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

        public void validateThreads(Set<Long> threads) {
            for (Long thread : threads) {
                if (!factory.createdThreadsId.contains(thread)) {
                    throw new IllegalStateException("You have used a thread that is not part of the thread pool");
                }
            }
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

    private class ThreadedDisk implements DiskCatalog.Disk {
        private DiskCatalog.Disk disk;
        private Set<Long> threads;

        private synchronized void registerCurrentThread() {
            threads.add(Thread.currentThread().getId());
        }

        ThreadedDisk(String bandName,
                     String diskTitle,
                     int year) {
            this.disk = new MyDisk(bandName, diskTitle, year);
            this.threads = new HashSet<>();
        }

        ThreadedDisk(DiskCatalog.Disk disk) {
            this(disk.getBandName(), disk.getDiskTitle(), disk.getYear());
        }

        @Override
        public String getBandName() {
            registerCurrentThread();
            return disk.getBandName();
        }

        @Override
        public String getDiskTitle() {
            registerCurrentThread();
            return disk.getDiskTitle();
        }

        @Override
        public int getYear() {
            registerCurrentThread();
            return disk.getYear();
        }
    }

    private int execute2(List<DiskCatalog.Disk> disks,
                         Optional<String> bandName,
                         Optional<String> diskTitle,
                         Optional<Integer> year,
                         MyExecutorService threadPool,
                         int countCallables) throws ExecutionException, InterruptedException {
        List<ThreadedDisk> d = new LinkedList<>();
        for (DiskCatalog.Disk disk: disks) {
            d.add(new ThreadedDisk(disk));
        }

        int result = DiskCatalog.countMatchingDisks(
            (List<DiskCatalog.Disk>) (List<?>) d,
            bandName, diskTitle, year, threadPool, countCallables);

        Map<Long, Integer> numberOfReadsPerThread = new HashMap<>();
        
        for (ThreadedDisk disk: d) {
            threadPool.validateThreads(disk.threads);

            assertTrue(disk.threads.size() <= 1);  // A disk can be read by at most 1 thread
            
            for (Long thread: disk.threads) {
                int count = 0;
                if (numberOfReadsPerThread.containsKey(thread)) {
                    count = numberOfReadsPerThread.get(thread);
                }
                numberOfReadsPerThread.put(thread, count + 1);
            }
        }

        for (Integer i : numberOfReadsPerThread.values()) {
            assertTrue(i >= 2);
        }
        
        return result;
    
    }

    private void checkCallables(List<DiskCatalog.Disk> disks,
                                MyExecutorService threadPool,
                                int countCallables) {
        assertEquals(0, threadPool.getCountRunnableSubmits());
        assertEquals(countCallables, threadPool.getCountCallableSubmits());
        threadPool.resetCountSubmits();
    }
    
    private void execute3(List<DiskCatalog.Disk> disks,
                          MyExecutorService threadPool,
                          int countCallables) throws ExecutionException, InterruptedException {
        for (int i = 0; i < 10; i++) {
            assertEquals(2, execute2(disks, Optional.of("The Beatles"), Optional.empty(), Optional.empty(), threadPool, countCallables));
            checkCallables(disks, threadPool, countCallables);
            
            assertEquals(3, execute2(disks, Optional.of("Oasis"), Optional.empty(), Optional.empty(), threadPool, countCallables));
            checkCallables(disks, threadPool, countCallables);
            
            assertEquals(2, execute2(disks, Optional.empty(), Optional.of("Faith"), Optional.empty(), threadPool, countCallables));
            checkCallables(disks, threadPool, countCallables);
            
            assertEquals(2, execute2(disks, Optional.empty(), Optional.of("Wish You Were Here"), Optional.empty(), threadPool, countCallables));
            checkCallables(disks, threadPool, countCallables);
            
            assertEquals(3, execute2(disks, Optional.empty(), Optional.empty(), Optional.of(1997), threadPool, countCallables));
            checkCallables(disks, threadPool, countCallables);
            
            assertEquals(1, execute2(disks, Optional.empty(), Optional.empty(), Optional.of(2000), threadPool, countCallables));
            checkCallables(disks, threadPool, countCallables);
            
            assertEquals(1, execute2(disks, Optional.of("Pink Floyd"), Optional.of("Wish You Were Here"), Optional.empty(), threadPool, countCallables));
            checkCallables(disks, threadPool, countCallables);
            
            assertEquals(1, execute2(disks, Optional.of("Oasis"), Optional.empty(), Optional.of(1997), threadPool, countCallables));
            checkCallables(disks, threadPool, countCallables);
            
            assertEquals(1, execute2(disks, Optional.empty(), Optional.of("Pop"), Optional.of(1997), threadPool, countCallables));
            checkCallables(disks, threadPool, countCallables);

            assertEquals(1, execute2(disks, Optional.of("Oasis"), Optional.of("Be Here Now"), Optional.of(1997), threadPool, countCallables));
            checkCallables(disks, threadPool, countCallables);
            
            assertEquals(0, execute2(disks, Optional.of("Nope"), Optional.of("Be Here Now"), Optional.of(1997), threadPool, countCallables));
            checkCallables(disks, threadPool, countCallables);
            
            assertEquals(0, execute2(disks, Optional.of("Oasis"), Optional.of("Nope"), Optional.of(1997), threadPool, countCallables));
            checkCallables(disks, threadPool, countCallables);
            
            assertEquals(0, execute2(disks, Optional.of("Oasis"), Optional.of("Be Here Now"), Optional.of(2000), threadPool, countCallables));
            checkCallables(disks, threadPool, countCallables);
            
            assertEquals(23, execute2(disks, Optional.empty(), Optional.empty(), Optional.empty(), threadPool, countCallables));  // Matches any album
            checkCallables(disks, threadPool, countCallables);

            Collections.shuffle(disks);
        }
    }

    @Test
    @Grade(value = 1, cpuTimeout = 2000)
    public void testOneCallable() throws ExecutionException, InterruptedException {
        List<DiskCatalog.Disk> disks = getTestCollection();
        MyExecutorService threadPool = new MyExecutorService(2);
        try {
            execute3(disks, threadPool, 1);
        } finally {
            threadPool.shutdown();
        }
    }

    @Test
    @Grade(value = 2, cpuTimeout = 2000)
    public void testTwoCallables() throws ExecutionException, InterruptedException {
        List<DiskCatalog.Disk> disks = getTestCollection();
        MyExecutorService threadPool = new MyExecutorService(2);
        try {
            execute3(disks, threadPool, 2);
        } finally {
            threadPool.shutdown();
        }
    }

    @Test
    @Grade(value = 4, cpuTimeout = 2000)
    public void testNCallables() throws ExecutionException, InterruptedException {
        List<DiskCatalog.Disk> disks = getTestCollection();
        MyExecutorService threadPool = new MyExecutorService(4);
        try {
            for (int i = 3; i < 10; i++) {
                execute3(disks, threadPool, i);
            }
        } finally {
            threadPool.shutdown();
        }
    }
    // END STRIP
}
