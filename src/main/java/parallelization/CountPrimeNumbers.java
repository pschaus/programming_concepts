package parallelization;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * In this exercise, you have to count the number of prime numbers in
 * an interval of integers using multiple threads. You have to
 * implement three versions of the multithreaded computation: Using a
 * Runnable, using a Callable, and using a shared variable.
 **/
public class CountPrimeNumbers {
    /**
     * Integer counter that is thread-safe thanks to the
     * "synchronized" keyword.
     **/
    public static class SharedCounter {
        private int counter = 0;

        /**
         * Set the counter to the given value.
         **/
        public synchronized void set(int value) {
            counter = value;
        }

        /**
         * Add a value to the counter.
         **/
        public synchronized void add(int value) {
            counter += value;
        }

        /**
         * Get the value of the counter.
         **/
        public synchronized int getValue() {
            return counter;
        }
    }
    

    /**
     * Check whether the given number is prime. A prime is an integer
     * that is not a product of two smaller natural numbers. By
     * convention, negative numbers, zero, and 1 are not considered as
     * prime numbers.
     *
     * @param number The number to be tested.
     * @return true if the number is prime, false otherwise.
     */
    public static boolean isPrime(int number) {
        // TODO
        // STUDENT return true;
        // BEGIN STRIP
        if (number <= 1) {
            return false;
        }
        
        for (int i = 2; i <= number / 2; i++) {
            // condition for non-prime number
            if (number % i == 0) {
                return false;
            }
        }
        return true;
        // END STRIP
    }


    /**
     * Count the number of primes inside the given interval of integers.
     *
     * @param start Start of the interval (inclusive).
     * @param end End of the interval (exclusive).
     * @return The number of integers "x" such that x is prime, "x >=
     * start", and "x < end".
     */
    public static int countPrimesInInterval(int start,
                                            int end) {
        // TODO
        // STUDENT return 0;
        // BEGIN STRIP
        int count = 0;
        for (int x = start; x < end; x++) {
            if (isPrime(x)) {
                count++;
            }
        }
        return count;
        // END STRIP
    }


    /**
     * Callable that counts the number of primes in an interval of integers.
     **/
    public static class CountPrimesCallable implements Callable<Integer> {
        private int start;
        private int end;

        /**
         * Constructor of the callable.
         * @param start Start of the interval (inclusive).
         * @param end End of the interval (exclusive).
         **/
        CountPrimesCallable(int start,
                            int end) {
            // TODO
            // BEGIN STRIP
            this.start = start;
            this.end = end;
            // END STRIP
        }

        public Integer call() {
            // TODO
            // STUDENT return 0;
            // BEGIN STRIP
            return countPrimesInInterval(start, end);
            // END STRIP
        }
    }


    /**
     * Runnable that counts the number of primes in an interval of integers.
     **/
    public static class CountPrimesRunnable implements Runnable {
        // BEGIN STRIP
        private int count;
        private int start;
        private int end;
        // END STRIP

        /**
         * Constructor of the runnable.
         * @param start Start of the interval (inclusive).
         * @param end End of the interval (exclusive).
         **/
        CountPrimesRunnable(int start,
                            int end) {
            // TODO
            // BEGIN STRIP
            this.start = start;
            this.end = end;
            // END STRIP
        }

        public void run() {
            // TODO
            // BEGIN STRIP
            count = countPrimesInInterval(start, end);
            // END STRIP
        }

        /**
         * Return the number of primes that were found in the interval
         * by the call to "run()".
         **/
        public int getResult() {
            // TODO
            // STUDENT return 0;
            // BEGIN STRIP
            return count;
            // END STRIP
        }
    }


    /**
     * Runnable that counts the number of primes in an interval of integers,
     * and that add this number to a shared counter.
     **/
    public static class CountPrimesSharedCounter implements Runnable {
        // BEGIN STRIP
        private SharedCounter target;
        private int start;
        private int end;
        // END STRIP

        /**
         * Constructor of the runnable.
         * @param target Shared counter to be incremented by the number
         * of primes in the interval.
         * @param start Start of the interval (inclusive).
         * @param end End of the interval (exclusive).
         **/
        CountPrimesSharedCounter(SharedCounter target,
                                 int start,
                                 int end) {
            // TODO
            // BEGIN STRIP
            this.target = target;
            this.start = start;
            this.end = end;
            // END STRIP
        }

        public void run() {
            // TODO
            // BEGIN STRIP
            target.add(countPrimesInInterval(start, end));
            // END STRIP
        }
    }


    /**
     * Count the number of primes up to a given number "end" by using
     * a thread pool and the CountPrimesCallable class defined above.
     *
     * You must divide the range between "0" and "end" into
     * "countIntervals" intervals of roughly equal length (note that a
     * more realistic implementation could consider a sequence of
     * intervals with reducing length, to account for the linear
     * complexity of "countPrimesInInterval()").
     *
     * @param threadPool The thread pool to be used.
     * @param end Largest number to be considered (exclusive, i.e.
     * you have to count the numbers "x" such that x is prime, and "x
     * < end").
     * @param countIntervals The number of intervals to be processed
     * by Future. 
     * @return The number of prime numbers below "end".
     *
     * NOTES:
     *   - The "threadPool" parameter corresponds to the thread pool to
     *     be used. You *don't have* to call the
     *     "Executors.newFixedThreadPool()" method by yourself to
     *     create the thread pool, nor the "executor.shutdown()"
     *     method (this is already done for you in the unit tests).
     *   - You do not have to catch any exception.
     *   - You must throw IllegalArgumentException if countIntervals <= 0.
     **/
    public static int countPrimesWithCallable(ExecutorService threadPool,
                                              int end,
                                              int countIntervals) throws InterruptedException, ExecutionException {
        // TODO
        // STUDENT return 0;
        // BEGIN STRIP
        if (countIntervals <= 0) {
            throw new IllegalArgumentException();
        }
        
        Stack<Future<Integer>> futures = new Stack<>();

        int intervalsize = end / countIntervals;

        for (int i = 0; i < countIntervals; i++) {
            int a = i * intervalsize;
            int b = (i == countIntervals - 1 ? end : (i + 1) * intervalsize);
            futures.add(threadPool.submit(new CountPrimesCallable(a, b)));
        }

        int count = 0;
        while (!futures.empty()) {
            count += futures.pop().get();
        }

        return count;
        // END STRIP
    }


    /**
     * Method with the same specification as
     * "countPrimesWithCallable()", but using CountPrimesRunnable.
     **/
    public static int countPrimesWithRunnable(ExecutorService threadPool,
                                              int end,
                                              int countIntervals) throws InterruptedException, ExecutionException {
        // TODO
        // STUDENT return 0;
        // BEGIN STRIP
        if (countIntervals <= 0) {
            throw new IllegalArgumentException();
        }
        
        List<CountPrimesRunnable> runnables = new ArrayList<>();
        Stack<Future> futures = new Stack<>();

        int intervalsize = end / countIntervals;

        for (int i = 0; i < countIntervals; i++) {
            int a = i * intervalsize;
            int b = (i == countIntervals - 1 ? end : (i + 1) * intervalsize);
            CountPrimesRunnable r = new CountPrimesRunnable(a, b);
            runnables.add(r);
            futures.add(threadPool.submit(r));
        }

        while (!futures.empty()) {
            futures.pop().get();
        }
        
        int count = 0;
        for (CountPrimesRunnable runnable : runnables) {
            count += runnable.getResult();
        }

        return count;
        // END STRIP
    }


    /**
     * Method with the same specification as
     * "countPrimesWithCallable()", but using CountPrimesSharedCounter.
     * Also, the result must be stored inside the "target" shared
     * variable, instead of being returned by the method.
     **/
    public static void countPrimesWithSharedCounter(SharedCounter target,
                                                    ExecutorService threadPool,
                                                    int end,
                                                    int countIntervals) throws InterruptedException, ExecutionException {
        // TODO
        // BEGIN STRIP
        if (countIntervals <= 0) {
            throw new IllegalArgumentException();
        }
        
        target.set(0);
        Stack<Future> futures = new Stack<>();

        int intervalsize = end / countIntervals;

        for (int i = 0; i < countIntervals; i++) {
            int a = i * intervalsize;
            int b = (i == countIntervals - 1 ? end : (i + 1) * intervalsize);
            futures.add(threadPool.submit(new CountPrimesSharedCounter(target, a, b)));
        }

        while (!futures.empty()) {
            futures.pop().get();
        }
        // END STRIP
    }
}
