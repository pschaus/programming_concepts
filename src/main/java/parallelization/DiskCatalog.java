package parallelization;

import java.util.Iterator;
import java.util.Optional;
import java.util.Stack;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * In this exercise, you must count the number of music disk albums
 * among a catalog that match some criteria, depending on the name of
 * the band, on the name of the album, and on the year of release. You
 * have to use multithreading to speed up the search over the
 * collection of albums.
 **/
public class DiskCatalog {
    
    /**
     * Interface representing one disk in the catalog.
     **/
    public interface Disk {
        /**
         * Get the name of the band.
         **/
        public String getBandName();

        /**
         * Get the title of the disk.
         **/
        public String getDiskTitle();

        /**
         * Get the year of release of the disk.
         **/
        public int getYear();
    }


    /**
     * Callable that implements the counting over a catalog of albums.
     **/
    public static class CountMatchingDisksCallable implements Callable<Integer> {
        private Iterator<Disk> iterator;
        private Optional<String> bandName;
        private Optional<String> diskTitle;
        private Optional<Integer> year;
        private int skip;

        /**
         * Constructor of the callable. This constructor receives all
         * the information that is necessary to perform the search.
         *
         * The search criteria on the band name, on the disk title,
         * and on the release year are provided as Optional values: An
         * empty criterion has to match any album.
         *
         * The catalog is provided as an iterator, which allows the
         * caller to use any type of Java collection. This iterator
         * points to the first album to be considered by the callable.
         * The iterators provided to the different callables must
         * correspond to a different starting point in the collection,
         * so that different callables do not consider twice the same
         * album.
         *
         * For instance, let us consider a collection with 10 albums:
         * [ A, B, C, D, E, F, G, H, I, J ]. If this collection has to
         * be processed by 3 callables, each of these callables would
         * process the following albums:
         *
         * - Callable 1: [ A, D, G, J ]
         * - Callable 2: [ B, E, H ]
         * - Callable 3: [ C, F, I ]
         *
         * Finally, the "skip" argument is necessary to avoid multiple
         * threads to consider the same album in the collection. The
         * argument indicates how many albums must be skipped between
         * each iteration over the collection. In the example above
         * with 3 callables, "skip" should be equal to 2.
         **/
        CountMatchingDisksCallable(Iterator<Disk> iterator,
                                   Optional<String> bandName,
                                   Optional<String> diskTitle,
                                   Optional<Integer> year,
                                   int skip) {
            if (skip < 0) {
                throw new IllegalArgumentException();
            }
        
            this.iterator = iterator;
            this.bandName = bandName;
            this.diskTitle = diskTitle;
            this.year = year;
            this.skip = skip;
        }

        public Integer call() {
            // TODO
            // STUDENT return -1;
            // BEGIN STRIP
            int count = 0;

            while (iterator.hasNext()) {
                Disk disk = iterator.next();
            
                if ((!bandName.isPresent() ||
                     bandName.get().equals(disk.getBandName())) &&
                    (!diskTitle.isPresent() ||
                     diskTitle.get().equals(disk.getDiskTitle())) &&
                    (!year.isPresent() ||
                     year.get().equals(disk.getYear()))) {
                    count++;
                }

                for (int i = 0; i < skip && iterator.hasNext(); i++) {
                    iterator.next();
                }
            }

            return count;
            // END STRIP
        }
    }


    /**
     * Method that counts the number of disk albums in a collection
     * (provided as a Java Iterable) that match some criteria, using
     * multithreading. Check out the constructor of
     * "CountMatchingDisksCallable" for a description of the criteria.
     * 
     * You must use the thread pool that is provided in the argument
     * "threadPool", and you must create a number of callables that is
     * equal to argument "countCallables".
     *
     * NOTES:
     *   - The "threadPool" parameter corresponds to the thread pool to
     *     be used. You *don't have* to call the
     *     "Executors.newFixedThreadPool()" method by yourself to
     *     create the thread pool, nor the "executor.shutdown()"
     *     method (this is already done for you in the unit tests).
     *   - You do not have to catch any exception.
     *   - You must throw IllegalArgumentException if countCallables <= 0.
     **/
    public static int countMatchingDisks(Iterable<Disk> disks,
                                         Optional<String> bandName,
                                         Optional<String> diskTitle,
                                         Optional<Integer> year,
                                         ExecutorService threadPool,
                                         int countCallables) throws InterruptedException, ExecutionException {
        // TODO
        // STUDENT return -1;
        // BEGIN STRIP
        if (countCallables <= 0) {
            throw new IllegalArgumentException();
        }
        
        Stack<Future<Integer>> futures = new Stack<>();

        for (int i = 0; i < countCallables; i++) {
            Iterator<Disk> iterator = disks.iterator();
            for (int j = 0; j < i; j++) {
                if (iterator.hasNext()) {
                    iterator.next();
                }
            }
            futures.add(threadPool.submit(new CountMatchingDisksCallable(iterator, bandName, diskTitle, year, countCallables - 1)));
        }

        int result = 0;

        while (!futures.isEmpty()) {
            result += futures.pop().get();
        }

        return result;
        // END STRIP
    }
}
