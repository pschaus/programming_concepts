package complexity;

import org.javagrader.Grade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class FibonacciTest {

    @Test
    @Grade(value = 1, cpuTimeout = 1, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
    public void test50() {
        assertEquals(12586269025L, Fibonacci.fibonacci(50));
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
    public void test75() {
        assertEquals(2111485077978050L, Fibonacci.fibonacci(75));
    }
}