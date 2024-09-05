package parallelization;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * You are part of the organizing committee of the 2024 Summer Olympics in Paris.
 * Your task is to create a software that computes the age distribution of the supporters attending
 * the events in the Stade de France, taking into account the nationality of the supporters.
 * Because the stadium can gather a lot of people, your computation must use multithreading.
 */
public class StadiumStatistics {

    /**
     * Interface defining one supporter attending the event in the stadium.
     */
    public interface Supporter {
        /**
         * Get the nationality of the supporter.
         *
         * @return The nationality.
         */
        public String getNationality();

        /**
         * Get the age of the supporter.
         *
         * @return The age (in number of years).
         */
        public int getAge();
    }

    /**
     * The different categories of ages you have to consider to describe the age distribution of the supporters.
     */
    public enum AgeCategory {
        BETWEEN_0_AND_9,      // Between 0 and 9 years old (inclusive)
        BETWEEN_10_AND_19,    // Between 10 and 19 years old (inclusive)
        BETWEEN_20_AND_29,    // Between 20 and 29 years old (inclusive)
        BETWEEN_30_AND_39,    // Between 30 and 39 years old (inclusive)
        BETWEEN_40_AND_49,    // Between 40 and 49 years old (inclusive)
        BETWEEN_50_AND_59,    // Between 50 and 59 years old (inclusive)
        BETWEEN_60_AND_69,    // Between 60 and 69 years old (inclusive)
        BETWEEN_70_AND_79,    // Between 70 and 79 years old (inclusive)
        ABOVE_80              // Above 80 years old (inclusive)
    }

    /**
     * Histogram encoding the age distribution of the supporters. This histogram must associate each
     * category of ages (cf. the "AgeCategory" enumeration) with the number of supporters of this age.
     */
    static public class AgeHistogram {
        // STUDENT // TODO - Add the private members of your data structure at this point
        // BEGIN STRIP
        private int counter[] = new int[9];
        // END STRIP

        /**
         * Your constructor. Initially, the number of supporters must be zero for each age category.
         */
        AgeHistogram() {
            // STUDENT // TODO (only if your data structure requires an initialization)
            // BEGIN STRIP
            // END STRIP
        }

        /**
         * Get the number of supporters in the given age category.
         *
         * @param category The age category of interest.
         * @return The number of supporters.
         * @throws IllegalArgumentException This exception must be thrown is the
         * requested age category does not exist in the "AgeCategory" enumeration.
         */
        public int getNumberOfSupporters(AgeCategory category) {
            // STUDENT return -1;  // TODO
            // BEGIN STRIP
            switch (category) {
                case BETWEEN_0_AND_9:
                    return counter[0];
                case BETWEEN_10_AND_19:
                    return counter[1];
                case BETWEEN_20_AND_29:
                    return counter[2];
                case BETWEEN_30_AND_39:
                    return counter[3];
                case BETWEEN_40_AND_49:
                    return counter[4];
                case BETWEEN_50_AND_59:
                    return counter[5];
                case BETWEEN_60_AND_69:
                    return counter[6];
                case BETWEEN_70_AND_79:
                    return counter[7];
                case ABOVE_80:
                    return counter[8];
                default:
                    throw new IllegalArgumentException();
            }
            // END STRIP
        }

        /**
         * Add one new supporter to the age distribution.
         *
         * @param age The age of the supporter.
         * @throws IllegalArgumentException This exception must be thrown is the age is invalid
         * (i.e., if the age is a negative number).
         */
        public void addSupporter(int age) {
            // STUDENT // TODO
            // BEGIN STRIP
            if (age < 0) {
                throw new IllegalArgumentException();
            }

            int index = Math.min(8, age / 10);
            counter[index]++;
            // END STRIP
        }

        /**
         * Combine two distributions of ages. This consists in summing the number of
         * supporters in both age distributions, for each age category.
         *
         * @param histogram1 The first age distribution.
         * @param histogram2 The second age distribution.
         * @return The combined age distribution.
         */
        static public AgeHistogram combine(AgeHistogram histogram1,
                                           AgeHistogram histogram2) {
            // STUDENT return null;  // TODO
            // BEGIN STRIP
            AgeHistogram result = new AgeHistogram();
            for (int i = 0; i < result.counter.length; i++) {
                result.counter[i] = histogram1.counter[i] + histogram2.counter[i];
            }
            return result;
            // END STRIP
        }
    }

    /**
     * Sequential algorithm to compute the age distribution of the supporters in the stadium,
     * for one nationality of interest. This method must *not* use threads.
     * <p>
     * In order to enable subsequent multithreading, this algorithm must consider only a subarray of the
     * provided array of supporters. More precisely, you are given two parameters "startIndex" and "endIndex",
     * and you must only consider the supporters in the range from "supporters[startIndex]" to
     * "supporters[endIndex - 1]". In the case where "startIndex == endIndex", there is no supporter to consider
     * and the resulting histogram contains zero for each age category.
     *
     * @param supporters  Information about all the supporters in the stadium.
     * @param nationality The nationality of interest. When computing the age distribution, you must
     *                    only include the supporters whose nationality corresponds to this argument.
     * @param startIndex  Start index of the subarray of supporters (inclusive).
     * @param endIndex    End index of the subarray of supporters (exclusive).
     * @return The age distribution.
     * @throws IllegalArgumentException If the value of "startIndex" or "endIndex" would lead to the reading
     * of an item that is out of the bounds of the "supporters" array. This exception is already implemented
     * for you before the "TODO": You do *not* have to raise this exception by yourself.
     */
    public static AgeHistogram computeAgeHistogramSequential(Supporter[] supporters,
                                                             String nationality,
                                                             int startIndex,
                                                             int endIndex) {
        if (startIndex < 0 || startIndex > endIndex || endIndex > supporters.length) {
            throw new IllegalArgumentException();
        }

        // STUDENT return null;  // TODO
        // BEGIN STRIP
        AgeHistogram histogram = new AgeHistogram();

        for (int i = startIndex; i < endIndex; i++) {
            if (supporters[i].getNationality().equals(nationality)) {
                histogram.addSupporter(supporters[i].getAge());
            }
        }

        return histogram;
        // END STRIP
    }

    /**
     * Parallel algorithm to compute the age distribution of the supporters in the stadium,
     * for one nationality of interest.
     * <p>
     * You *must* use two threads to carry on this computation, and you *must* use the provided thread pool.
     * You are encouraged to use the method "AgeHistogram.combine()" to combine the partial results computed
     * by the two threads.
     * <p>
     * You MUST catch all exceptions related to multithreading. If such an exception is thrown, you must return "null".
     *
     * @param executorService The thread pool to be used. You must *not* call the method "Executors.newFixedThreadPool()"
     *                        to create the thread pool, neither the method "executor.shutdown()" (this is done for you
     *                        in the unit tests).
     * @param supporters      Information about all the supporters in the stadium.
     * @param nationality     The nationality of interest. When computing the age distribution, you must only
     *                        include the supporters whose nationality corresponds to this argument.
     * @return The age distribution.
     */
    public static AgeHistogram computeAgeHistogramParallel(ExecutorService executorService,
                                                           Supporter[] supporters,
                                                           String nationality) {
        // STUDENT return null;  // TODO
        // BEGIN STRIP
        if (true) {
            // CORRECT SOLUTION using Future
            Future<AgeHistogram> future1 = executorService.submit(() -> computeAgeHistogramSequential(supporters, nationality, 0, supporters.length / 2));
            Future<AgeHistogram> future2 = executorService.submit(() -> computeAgeHistogramSequential(supporters, nationality, supporters.length / 2, supporters.length));

            try {
                return AgeHistogram.combine(future1.get(), future2.get());
            } catch (InterruptedException | ExecutionException e) {
                return null;
            }
        } else if (false) {
            // CORRECT SOLUTION using Runnable
            class R implements Runnable {
                private int startIndex;
                private int endIndex;
                private AgeHistogram histogram;

                R(int startIndex,
                  int endIndex) {
                    this.startIndex = startIndex;
                    this.endIndex = endIndex;
                }

                @Override
                public void run() {
                    histogram = computeAgeHistogramSequential(supporters, nationality, startIndex, endIndex);
                }

                public AgeHistogram getHistogram() {
                    return histogram;
                }
            }

            R r1 = new R(0, supporters.length / 2);
            R r2 = new R(supporters.length / 2, supporters.length);
            Future f1 = executorService.submit(r1);
            Future f2 = executorService.submit(r2);
            try {
                f1.get();
                f2.get();
                return AgeHistogram.combine(r1.getHistogram(), r2.getHistogram());
            } catch (InterruptedException | ExecutionException e) {
                return null;
            }
        } else if (false) {
            // INCORRECT SOLUTION - Futures are doing nothing
            final Future<Void> futureA = executorService.submit(() -> null);
            final Future<Void> futureB = executorService.submit(() -> null);
            try {
                futureA.get();
                futureB.get();
                return computeAgeHistogramSequential(supporters, nationality, 0, supporters.length);
            } catch (InterruptedException | ExecutionException e) {
                return null;
            }
        } else if (false) {
            // INCORRECT SOLUTION - One future does all the job, the second does nothing
            final Future<AgeHistogram> futureA = executorService.submit(() -> computeAgeHistogramSequential(supporters, nationality, 0, supporters.length));
            final Future<AgeHistogram> futureB = executorService.submit(() -> new AgeHistogram());
            try {
                return AgeHistogram.combine(futureA.get(), futureB.get());
            } catch (InterruptedException | ExecutionException e) {
                return null;
            }
        } else {
            return null;
        }
        // END STRIP
    }
}
