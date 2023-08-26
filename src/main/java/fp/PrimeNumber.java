package fp;

import java.util.function.IntUnaryOperator;
import java.util.stream.IntStream;

/**
 *
 */
public class PrimeNumber {

    /**
     * Check that number is prime (can be divided by 1 and himself)
     *
     * @param number a non negative number
     * @return true if number is prime, false otherwise
     */
    public static boolean isPrime(int number) {
        // TODO
        // STUDENT return true;
        // BEGIN STRIP
        if (number == 0) return false;
        for (int i = 2; i <= number / 2; ++i) {
            // condition for non-prime number
            if (number % i == 0) {
                return false;
            }
        }
        return true;
        // END STRIP
    }

    /**
     * Generates an infinite stream of consecutive integers starting from a given value
     * @param from the start of the stream
     * @return an infinite stream from, from+1, from+2, ...
     */
    public static IntStream streamFrom(int from) {
        // TODO:
        // Hint: Consider using java.util.stream.IntStream.iterate method
        // STUDENT return null;
        // BEGIN STRIP
        return IntStream.iterate(from, i -> i + 1);
        // END STRIP
    }

    public static IntStream primeStreamFrom(int from) {
        // TODO
        // Hint: use filter
        // STUDENT return null;
        // BEGIN STRIP
        return streamFrom(from).filter(PrimeNumber::isPrime);
        // END STRIP
    }

    /**
     * Generate an infinite stream of prime gaps (difference between two successive prime numbers)
     * computed on the stream of prime numbers
     * starting at the first prime number larger or equal to from.
     * Example: from = 5 (5, 7, 11, 13, 17, 19, ...) , the stream of prime gaps is thus 2, 4, 2, 4, 2, ...
     *
     * @param from
     * @return an infinite stream of prime gaps
     */
    public static IntStream primeGapStreamFrom(int from) {
        // TODO
        // STUDENT return null;
        // BEGIN STRIP
        return streamFrom(from).filter(PrimeNumber::isPrime).map(new IntUnaryOperator() {
            int previous;
            @Override
            public int applyAsInt(int operand) {
                int diff = operand-previous;
                previous = operand;
                return diff;
            }
        }).skip(1);
        // END STRIP
    }

}
