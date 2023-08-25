package oop;// This file must *not* be modified!

import org.javagrader.Grade;
import org.junit.jupiter.api.Test;


import java.util.stream.DoubleStream;

import static org.junit.jupiter.api.Assertions.*;


public class DotProductIteratorTest {
    static final float THRESHOLD = 0.000001f;

    private static double computeDotProduct(double[] a,
                                            double[] b) {
        return DotProductIterator.computeDotProduct(
                DoubleStream.of(a).iterator(), DoubleStream.of(b).iterator());
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testBasicProduct() {
        double[] a = {4, 5};
        double[] b = {6, 7};
        assertEquals(4 * 6 + 5 * 7, computeDotProduct(a, b), THRESHOLD);
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testBasicIterator() {
        double[] a = {2, 7};
        DotProductIterator.VectorIterator it = new DotProductIterator.VectorIterator(a);
        assertTrue(it.hasNext());
        assertEquals(2, it.next(), THRESHOLD);
        assertTrue(it.hasNext());
        assertEquals(7, it.next(), THRESHOLD);
        assertFalse(it.hasNext());
    }

    // BEGIN STRIP
    private double[] createRandomArray(int size) {
        double[] v = new double[size];
        for (int i = 0; i < size; i++) {
            v[i] = Math.random();
        }
        return v;
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testProduct2() {
        for (int i = 0; i < 10; i++) {
            double[] a = createRandomArray(i);
            double[] b = createRandomArray(i);

            double accumulator = 0;
            for (int j = 0; j < a.length; j++) {
                accumulator += a[j] * b[j];
            }

            assertEquals(computeDotProduct(a, b), accumulator, THRESHOLD);
        }
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testIterator2() {
        for (int i = 0; i < 10; i++) {
            double[] a = createRandomArray(i);
            DotProductIterator.VectorIterator it = new DotProductIterator.VectorIterator(a);
            for (int j = 0; j < i; j++) {
                assertTrue(it.hasNext());
                assertEquals(a[j], it.next(), THRESHOLD);
            }
            assertFalse(it.hasNext());
        }
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testIncompatibleSizes() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                double[] a = createRandomArray(i);
                double[] b = createRandomArray(j);
                if (i == j) {
                    computeDotProduct(a, b);
                } else {
                    try {
                        computeDotProduct(a, b);
                        fail("should have thrown an exception");
                    } catch (IllegalArgumentException e) {
                    }
                }
            }
        }
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testOutOfBoundsIterator() {
        for (int i = 0; i < 10; i++) {
            double[] a = createRandomArray(i);
            DotProductIterator.VectorIterator it = new DotProductIterator.VectorIterator(a);
            for (int j = 0; j < i; j++) {
                assertTrue(it.hasNext());
                assertEquals(a[j], it.next(), THRESHOLD);
            }
            assertFalse(it.hasNext());
            try {
                it.next();
                fail();
            } catch (IllegalStateException e) {
            }
        }
    }
    // END STRIP
}
