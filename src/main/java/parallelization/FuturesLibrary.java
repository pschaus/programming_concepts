package parallelization;// BEGIN STRIP

/**

   Comments by Ramin (2023-07-10):

   "Looks good to me. Our traditional thread question in a different
   disguise :) Taking the role of a young student, I had first
   difficulties to understand what the parameter "countBooks" means,
   but if we provide a simple example in the tests, it should become
   clear. If we want to make this question more difficult, we could
   ask them to return the list of found books, not just the count."

 **/

// END STRIP
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.function.Predicate;

/**
 *  ***********
 *  * English *
 *  ***********
 *
 * You are an employee of a public library who must create a software to search the content of the books of the
 * library. Because the library contains many books, your task is to speed up this search thanks to
 * Java threads.
 *
 *  ************
 *  * Français *
 *  ************
 *
 * Vous êtes un employé d'une bibliothèque (library) publique qui doit créer un logiciel
 * pour faire des recherches dans le contenu des livres de la bibliothèque.
 * Comme la bibliothèque contient de nombreux livres,
 * votre tâche est d'accélérer cette recherche grâce à des threads Java.
 */
public class FuturesLibrary {

    /**
     * Interface that represents one book of the public library.
     * You do not have to implement this interface.
     */
    public interface Book {
        /**
         * Method that returns the author of the book.
         *
         * @return The author.
         */
        public String getAuthor();

        /**
         * Method that returns the number of words in the book.
         *
         * @return The number of words.
         */
        public int getNumberOfWords();

        /**
         * Method that returns one word from the book, using the index of the word.
         *
         * @param index The index of the word, which must be smaller than the value of "getNumberOfWords()".
         * @return The word of interest.
         */
        public String getWord(int index);
    }

    /**
     * Interface that represents the content of the public library.
     * You do not have to implement this interface.
     */
    public interface Library {
        /**
         * Method that returns the number of books stored in the library.
         *
         * @return The number of books.
         */
        public int getNumberOfBooks();

        /**
         * Method that returns one book stored in the library, using the index of the book.
         *
         * @param index The index of the book, which must be smaller than the value of "getNumberOfBooks()".
         * @return The book of interest.
         */
        public Book getBook(int index);
    }

    /**
     * Function that counts the number of books that match a given predicate, among a subset of the books stored in
     * the library. The subset of books is specified as a range of book indices in the library. These indices
     * correspond to the argument given to the "Library.getBook()" method. This function must be
     * sequential (i.e. it must **not** use threads).
     *
     * @param library        The library.
     * @param predicate      The predicate that specifies the criteria that the books must match.
     * @param startBookIndex The index of the first book in the range of interest.
     * @param countBooks     The number of books in the range of interest.
     * @return The number of books matching the predicate.
     * @throws IllegalArgumentException If "startBookIndex" or "countBooks" is negative, or if a book whose index is
     * greater or equal to the number of books in the library would have to be accessed.
     */
    public static int countMatchingBooks(Library library,
                                         Predicate<Book> predicate,
                                         int startBookIndex,
                                         int countBooks) {
        // TODO
        // STUDENT return 0;
        // BEGIN STRIP
        if (countBooks < 0 ||
                startBookIndex < 0 ||
                startBookIndex >= library.getNumberOfBooks() ||
                startBookIndex + countBooks > library.getNumberOfBooks()) {
            throw new IllegalArgumentException();
        }

        int count = 0;

        for (int i = 0; i < countBooks; i++) {
            if (predicate.test(library.getBook(i + startBookIndex))) {
                count++;
            }
        }

        return count;
        // END STRIP
    }

    /**
     * Function that counts the number of books that match a given predicate, among all the books stored in the
     * library. This function **must** use threads using the **executor given in argument**. The number of threads
     * to be used is given in the arguments.
     *
     * The individual threads should internally use the sequential method "countMatchingBooks()", and each thread
     * must access a number of books that is similar to the number of books that are accessed by the other threads
     * (i.e. the amount of work must be balanced between the different threads).
     *
     * You MUST catch all exceptions related to multithreading. If such an exception is thrown, you can return 0.
     *
     * @param library      The library.
     * @param predicate    The predicate that specifies the criteria that the books must match.
     * @param executor     The thread pool to be used. You must *not* call the method "Executors.newFixedThreadPool()"
     *                     to create the thread pool, neither the method "executor.shutdown()" (this is done for you
     *                     in the unit tests).
     * @param countThreads The number of threads to be used.
     * @return The number of books matching the predicate.
     */
    public static int countMatchingBooksWithThreads(Library library,
                                                    Predicate<Book> predicate,
                                                    ExecutorService executor,
                                                    int countThreads) {
        if (countThreads == 0) {
            throw new IllegalArgumentException();
        }

        // TODO
        // STUDENT return 0;
        // BEGIN STRIP
        ArrayList<Future<Integer>> futures = new ArrayList<Future<Integer>>();

        int finalCount = 0;

        if (true) {
            // Correct solution
            int countPerThread;
            if (library.getNumberOfBooks() % countThreads == 0) {
                countPerThread = library.getNumberOfBooks() / countThreads;
            } else {
                countPerThread = library.getNumberOfBooks() / countThreads + 1;
            }
            for (int i = 0; i < countThreads; i++) {
                final int start = i * countPerThread;
                final int count = Math.min(countPerThread, library.getNumberOfBooks() - start);
                futures.add(executor.submit(() -> countMatchingBooks(library, predicate, start, count)));
            }
        } else if (true) {
            // Incorrect solution: incorrect balance between threads
            for (int i = 1; i < countThreads; i++) {
                futures.add(executor.submit(() -> countMatchingBooks(library, predicate, 0, 0)));
            }

            futures.add(executor.submit(() -> countMatchingBooks(library, predicate, 0, library.getNumberOfBooks())));
        } else if (true) {
            // Incorrect solution: Everything is done in the main thread
            for (int i = 0; i < countThreads; i++) {
                futures.add(executor.submit(() -> countMatchingBooks(library, predicate, 0, 0)));
            }

            finalCount = countMatchingBooks(library, predicate, 0, library.getNumberOfBooks());
        }

        try {
            for (Future<Integer> future : futures) {
                finalCount += future.get();
            }

            return finalCount;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return 0;
        }
        // END STRIP
    }
}
