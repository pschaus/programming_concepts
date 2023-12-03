package fp;

import org.javagrader.Grade;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.IntStream;

@Grade
public class PrimeNumberStreamTest {

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testisPrime() {
        assertTrue(PrimeNumberStream.isPrime(19));
        assertTrue(PrimeNumberStream.isPrime(17));
        assertFalse(PrimeNumberStream.isPrime(0));
        assertFalse(PrimeNumberStream.isPrime(27));
        assertTrue(PrimeNumberStream.isPrime(8191));
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testInfiniteStream1() {
        IntStream streamInf = PrimeNumberStream.streamFrom(0);
        assertArrayEquals(streamInf.limit(10).toArray(), new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testInfiniteStream2() {
        IntStream streamInf = PrimeNumberStream.streamFrom(1000);
        assertArrayEquals(streamInf.limit(4).toArray(), new int[]{1000,1001,1002,1003});
    }


    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testPrime1() {
        IntStream streamInf = PrimeNumberStream.primeStreamFrom(0);
        assertArrayEquals(streamInf.limit(9).toArray(), new int[]{1, 2, 3, 5, 7, 11, 13, 17, 19});
    }


    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testPrime2() {
        IntStream streamInf = PrimeNumberStream.primeStreamFrom(8191);
        assertArrayEquals(streamInf.limit(20).toArray(), new int[]{8191, 8209, 8219, 8221, 8231, 8233, 8237, 8243, 8263, 8269, 8273, 8287, 8291, 8293, 8297, 8311, 8317, 8329, 8353, 8363});
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testInfiniteStreamOfPrimeGaps2() {
        IntStream streamInf = PrimeNumberStream.primeGapStreamFrom(8191);
        assertArrayEquals(streamInf.limit(20).toArray(), new int[]{18, 10, 2, 10, 2, 4, 6, 20, 6, 4, 14, 4, 2, 4, 14, 6, 12, 24, 10, 6});
    }

}
