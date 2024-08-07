package algorithms;

import algorithms.Fibonacci;
import org.javagrader.Grade;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Grade
class FibonacciTest {

    @Test
    public void test50() {
        assertEquals(12586269025L, Fibonacci.fibonacci(50));
    }

    @Test
    public void test75() {
        assertEquals(2111485077978050L, Fibonacci.fibonacci(75));
    }
}