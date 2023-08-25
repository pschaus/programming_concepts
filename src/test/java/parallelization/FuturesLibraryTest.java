package parallelization;// This file must *not* be modified!

import org.javagrader.Grade;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Predicate;


public class FuturesLibraryTest {
    private static class MyBook implements FuturesLibrary.Book {
        private String author;
        private ArrayList<String> words = new ArrayList<String>();

        MyBook(String author) {
            this.author = author;
        }

        public MyBook addWord(String word) {
            words.add(word);
            return this;
        }

        @Override
        public String getAuthor() {
            return author;
        }

        @Override
        public int getNumberOfWords() {
            return words.size();
        }

        @Override
        public String getWord(int index) {
            return words.get(index);
        }
    }

    private static class MyLibrary implements FuturesLibrary.Library {
        private ArrayList<FuturesLibrary.Book> books = new ArrayList<FuturesLibrary.Book>();

        MyBook addBook(String author) {
            MyBook book = new MyBook(author);
            books.add(book);
            return book;
        }

        FuturesLibrary.Book addBook(FuturesLibrary.Book book) {
            books.add(book);
            return book;
        }

        @Override
        public int getNumberOfBooks() {
            return books.size();
        }

        @Override
        public FuturesLibrary.Book getBook(int index) {
            return books.get(index);
        }
    }

    public static boolean containsWord(FuturesLibrary.Book book,
                                       String word) {
        for (int i = 0; i < book.getNumberOfWords(); i++) {
            if (book.getWord(i).equals(word)) {
                return true;
            }
        }
        return false;
    }

    @Test
    @Grade(value = 1, cpuTimeout = 100)
    public void testSimpleSequential() {
        MyLibrary library = new MyLibrary();
        library.addBook("Schaus").addWord("Hello").addWord("World").addWord("42");
        library.addBook("Jodogne").addWord("Never").addWord("Gonna").addWord("Give").addWord("You").addWord("Up");
        library.addBook("Sadre").addWord("10").addWord("World").addWord("Hello");

        Predicate<FuturesLibrary.Book> p = (book) -> containsWord(book, "World");
        assertEquals(0, FuturesLibrary.countMatchingBooks(library, p, 0, 0));
        assertEquals(1, FuturesLibrary.countMatchingBooks(library, p, 0, 1));
        assertEquals(1, FuturesLibrary.countMatchingBooks(library, p, 0, 2));
        assertEquals(2, FuturesLibrary.countMatchingBooks(library, p, 0, 3));
        assertEquals(0, FuturesLibrary.countMatchingBooks(library, p, 1, 0));
        assertEquals(0, FuturesLibrary.countMatchingBooks(library, p, 1, 1));
        assertEquals(1, FuturesLibrary.countMatchingBooks(library, p, 1, 2));
        assertEquals(0, FuturesLibrary.countMatchingBooks(library, p, 2, 0));
        assertEquals(1, FuturesLibrary.countMatchingBooks(library, p, 2, 1));

        assertEquals(1, FuturesLibrary.countMatchingBooks(library, (book) -> book.getAuthor().equals("Schaus"),
                0, library.getNumberOfBooks()));
    }

    @Test
    @Grade(value = 1, cpuTimeout = 100)
    public void testSimpleThreads() {
        MyLibrary library = new MyLibrary();
        library.addBook("Schaus").addWord("Hello").addWord("World").addWord("42");
        library.addBook("Jodogne").addWord("Never").addWord("Gonna").addWord("Give").addWord("You").addWord("Up");
        library.addBook("Sadre").addWord("10").addWord("World").addWord("Hello");

        ExecutorService service = Executors.newFixedThreadPool(2);
        try {
            assertEquals(2, FuturesLibrary.countMatchingBooksWithThreads(library, (book) -> containsWord(book, "World"),
                    service, 2));
            assertEquals(1, FuturesLibrary.countMatchingBooksWithThreads(library, (book) -> book.getAuthor().equals(
                    "Schaus"), service, 2));
        } finally {
            service.shutdown();
        }
    }

    // BEGIN STRIP

    @Test
    @Grade(value = 1, cpuTimeout = 100)
    public void testExceptions() {
        MyLibrary library = new MyLibrary();
        library.addBook("Schaus");
        library.addBook("Jodogne");
        library.addBook("Sadre");

        Predicate<FuturesLibrary.Book> p = (book) -> book.getAuthor().equals("Sadre");

        assertEquals(0, FuturesLibrary.countMatchingBooks(library, p, 2, 0));
        assertEquals(1, FuturesLibrary.countMatchingBooks(library, p, 2, 1));

        try {
            FuturesLibrary.countMatchingBooks(library, p, 2, -1);
            fail();
        } catch (IllegalArgumentException e) {
        }

        try {
            FuturesLibrary.countMatchingBooks(library, p, -1, 1);
            fail();
        } catch (IllegalArgumentException e) {
        }

        try {
            FuturesLibrary.countMatchingBooks(library, p, 2, 2);
            fail();
        } catch (IllegalArgumentException e) {
        }

        // The following test was removed after the exam, as many
        // students had failed it

        /*try {
            FuturesLibrary.countMatchingBooks(library, p, 3, 0);
            fail();
        } catch (IllegalArgumentException e) {
        }*/
    }

    @Test
    @Grade(value = 2, cpuTimeout = 1000)
    public void testManyBooksSequential() {
        MyLibrary library = new MyLibrary();

        for (int i = 0; i < 100; i++) {
            MyBook book = new MyBook("author " + Integer.toString(i % 4));
            for (int j = 0; j < 100; j++) {
                book.addWord("word " + Integer.toString(i + j));
            }
            library.addBook(book);
        }

        assertEquals(0, FuturesLibrary.countMatchingBooks(library,
                (x) -> x.getAuthor().equals("nope"), 0, library.getNumberOfBooks()));

        assertEquals(25, FuturesLibrary.countMatchingBooks(library,
                (x) -> x.getAuthor().equals("author 0"), 0, library.getNumberOfBooks()));

        assertEquals(25, FuturesLibrary.countMatchingBooks(library,
                (x) -> x.getAuthor().equals("author 3"), 0, library.getNumberOfBooks()));

        assertEquals(10, FuturesLibrary.countMatchingBooks(library,
                (x) -> containsWord(x, "word 9"), 0, library.getNumberOfBooks()));

        assertEquals(1, FuturesLibrary.countMatchingBooks(library,
                (x) -> containsWord(x, "word 198"), 0, library.getNumberOfBooks()));
    }

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
                throw new RuntimeException("Trying to access an object from outside of the thread pool");
            }
        }
    }

    private static class MyExecutorService implements ExecutorService {
        private int countSubmits;
        private ExecutorService wrapped;
        private MyThreadFactory factory;
        private final Map<Object, Set<Long>> objectToAccessingThreads = new HashMap<>();
        private final Map<Long, Set<Object>> threadsToAccessedObjects = new HashMap<>();

        public synchronized int getCountSubmits() {
            return countSubmits;
        }

        public synchronized void logAccess(Object object) {
            final long threadId = Thread.currentThread().getId();

            factory.checkAllowedThread(threadId);

            if (objectToAccessingThreads.containsKey(object)) {
                objectToAccessingThreads.get(object).add(threadId);
            } else {
                HashSet<Long> threads = new HashSet<>();
                threads.add(threadId);
                objectToAccessingThreads.put(object, threads);
            }

            if (threadsToAccessedObjects.containsKey(threadId)) {
                threadsToAccessedObjects.get(threadId).add(object);
            } else {
                HashSet<Object> objects = new HashSet<>();
                objects.add(object);
                threadsToAccessedObjects.put(threadId, objects);
            }
        }

        public synchronized int getNumberOfThreadsAccessingObject(Object object) {
            if (objectToAccessingThreads.containsKey(object)) {
                return objectToAccessingThreads.get(object).size();
            } else {
                return 0;
            }
        }

        public synchronized void checkAllObjectsWereAccessedByOneSingleThread() {
            for (Map.Entry<Object, Set<Long>> entry : objectToAccessingThreads.entrySet()) {
                if (entry.getValue().size() != 1) {
                    throw new RuntimeException("One object was accessed from multiple threads");
                }
            }
        }

        public synchronized void checkAllThreadsHaveAccessedTheSameNumberOfObjects(int numberOfObjects) {
            for (Map.Entry<Long, Set<Object>> entry : threadsToAccessedObjects.entrySet()) {
                if (entry.getValue().size() != numberOfObjects) {
                    throw new RuntimeException("The load was not balanced between the different threads");
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

    private class ThreadLoggingPredicate implements Predicate<FuturesLibrary.Book> {
        private MyExecutorService executor;
        private Predicate<FuturesLibrary.Book> predicate;

        ThreadLoggingPredicate(MyExecutorService executor,
                               Predicate<FuturesLibrary.Book> predicate) {
            this.executor = executor;
            this.predicate = predicate;
        }

        @Override
        public boolean test(FuturesLibrary.Book book) {
            executor.logAccess(book);
            return predicate.test(book);
        }
    }

    @Test
    @Grade(value = 5, cpuTimeout = 1000)
    public void testThreadingPolicy() {
        MyLibrary library = new MyLibrary();

        for (int i = 0; i < 100; i++) {
            MyBook book = new MyBook("author " + Integer.toString(i));
            for (int j = 0; j < 100; j++) {
                book.addWord("word " + Integer.toString(i + j));
            }
            library.addBook(book);
        }

        {
            MyExecutorService executor = new MyExecutorService(8);
            try {
                assertEquals(10, FuturesLibrary.countMatchingBooksWithThreads(library,
                        new ThreadLoggingPredicate(executor, (x) -> containsWord(x, "word 9")),
                        executor, 2));
                assertEquals(2 /* there were 2 threads */, executor.getCountSubmits());
                executor.checkAllObjectsWereAccessedByOneSingleThread();
                executor.checkAllThreadsHaveAccessedTheSameNumberOfObjects(50);
            } finally {
                executor.shutdown();
            }
        }

        {
            MyExecutorService executor = new MyExecutorService(8);
            try {
                assertEquals(1, FuturesLibrary.countMatchingBooksWithThreads(library,
                        new ThreadLoggingPredicate(executor, (x) -> containsWord(x, "word 198")),
                        executor, 4));
                assertEquals(4 /* there were 4 threads */, executor.getCountSubmits());
                executor.checkAllObjectsWereAccessedByOneSingleThread();
                executor.checkAllThreadsHaveAccessedTheSameNumberOfObjects(25);
            } finally {
                executor.shutdown();
            }
        }
    }

    // END STRIP
}
