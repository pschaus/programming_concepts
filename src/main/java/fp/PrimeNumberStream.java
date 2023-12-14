package fp;

import java.util.function.UnaryOperator;
import java.util.stream.Stream;

/**
 * Exercise about creating infinite streams. The goal is to model the
 * stream of prime numbers, as well as the stream of gap sizes between
 * successive prime numbers.
 */
public class PrimeNumberStream {

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
     * Generate the infinite stream of consecutive integers starting from a given value.
     * @param from The starting value (inclusive).
     * @return The infinite stream of integers: "from", "from + 1", "from + 2",...
     */
    public static Stream<Integer> streamFrom(int from) {
        // TODO:
        // Hint: Consider using Stream.iterate() method
        // STUDENT return null;
        // BEGIN STRIP
        return Stream.iterate(from, i -> i + 1);
        // END STRIP
    }


    /**
     * Generate the infinite stream of prime numbers that are greater or equal to a given value.
     * @param from The starting value (inclusive).
     * @return The infinite stream of prime numbers.
     */
    public static Stream<Integer> primeStreamFrom(int from) {
        // TODO
        // Hint: use filter
        // STUDENT return null;
        // BEGIN STRIP
        return streamFrom(from).filter(PrimeNumberStream::isPrime);
        // END STRIP
    }

    /**
     * Generate the infinite stream of prime gaps (difference between
     * two successive prime numbers), starting at the first prime
     * number that is greater or equal to a given value.
     *
     * Example: If "from" = 5, the stream of prime numbers is (5, 7,
     * 11, 13, 17, 19,...), consequently the stream of prime gaps is
     * (7-5, 11-7, 13-11, 17-13, 19-17,...), which is equal to (2, 4,
     * 2, 4, 2,...).
     *
     * @param from The starting value (inclusive).
     * @return The infinite stream of prime gaps.
     */
    public static Stream<Integer> primeGapStreamFrom(int from) {
        // TODO
        // STUDENT return null;
        // BEGIN STRIP
        return streamFrom(from).filter(PrimeNumberStream::isPrime).map(new UnaryOperator<Integer>() {
            int previous;
            @Override
            public Integer apply(Integer operand) {
                int diff = operand - previous;
                previous = operand;
                return diff;
            }
        }).skip(1);
        // END STRIP
    }
}
