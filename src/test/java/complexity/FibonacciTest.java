package complexity;

import org.javagrader.Grade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class FibonacciTest {

    @Test
    @Timeout(3)
    @Grade(value = 1, custom = true, cpuTimeout = 1)
    public void test50() {
        assertEquals(12586269025L, Fibonacci.fibonacci(50));
    }

    @Test
    @Timeout(3)
    @Grade(value = 1, custom = true, cpuTimeout = 1)
    public void test75() {
        assertEquals(2111485077978050L, Fibonacci.fibonacci(75));
    }

}