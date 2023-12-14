package fp;

import org.javagrader.Grade;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;

@Grade
public class PrimeNumberStreamTest {

    @Test
    @Grade(value = 1, cpuTimeout = 2000)
    public void testIsPrime() {
        assertFalse(PrimeNumberStream.isPrime(-10));
        assertFalse(PrimeNumberStream.isPrime(-1));
        assertFalse(PrimeNumberStream.isPrime(0));
        assertFalse(PrimeNumberStream.isPrime(1));
        assertTrue(PrimeNumberStream.isPrime(2));
        assertTrue(PrimeNumberStream.isPrime(3));
        assertFalse(PrimeNumberStream.isPrime(4));
        assertTrue(PrimeNumberStream.isPrime(5));
        assertFalse(PrimeNumberStream.isPrime(6));
        assertTrue(PrimeNumberStream.isPrime(7));
        assertFalse(PrimeNumberStream.isPrime(8));
        assertFalse(PrimeNumberStream.isPrime(9));
        assertFalse(PrimeNumberStream.isPrime(10));
        assertTrue(PrimeNumberStream.isPrime(11));
        assertTrue(PrimeNumberStream.isPrime(19));
        assertTrue(PrimeNumberStream.isPrime(17));
        assertFalse(PrimeNumberStream.isPrime(0));
        assertFalse(PrimeNumberStream.isPrime(27));
        assertTrue(PrimeNumberStream.isPrime(8191));
    }

    private static void check(Stream<Integer> stream,
                              int[] expected) {
        Object[] actual = stream.toArray();
        assertEquals(actual.length, expected.length);
        for (int i = 0; i < actual.length; i++) {
            assertEquals((Integer) actual[i], expected[i]);
        }
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testInfiniteStream1() {
        Stream<Integer> streamInf = PrimeNumberStream.streamFrom(0);
        check(streamInf.limit(10), new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testInfiniteStream2() {
        Stream<Integer> streamInf = PrimeNumberStream.streamFrom(1000);
        check(streamInf.limit(4), new int[]{1000,1001,1002,1003});
    }


    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testPrime1() {
        Stream<Integer> streamInf = PrimeNumberStream.primeStreamFrom(0);
        check(streamInf.limit(13), new int[]{2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41});
    }


    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testPrime2() {
        Stream<Integer> streamInf = PrimeNumberStream.primeStreamFrom(8191);
        check(streamInf.limit(20), new int[]{8191, 8209, 8219, 8221, 8231, 8233, 8237, 8243, 8263, 8269, 8273, 8287, 8291, 8293, 8297, 8311, 8317, 8329, 8353, 8363});
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testInfiniteStreamOfPrimeGaps1() {
        Stream<Integer> streamInf = PrimeNumberStream.primeGapStreamFrom(0);
        check(streamInf.limit(12), new int[]{1, 2, 2, 4, 2, 4, 2, 4, 6, 2, 6, 4});
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testInfiniteStreamOfPrimeGaps2() {
        Stream<Integer> streamInf = PrimeNumberStream.primeGapStreamFrom(8191);
        check(streamInf.limit(20), new int[]{18, 10, 2, 10, 2, 4, 6, 20, 6, 4, 14, 4, 2, 4, 14, 6, 12, 24, 10, 6});
    }

}
