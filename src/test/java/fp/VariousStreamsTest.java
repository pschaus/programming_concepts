package fp;

/**
 * Exercises in this file are mostly inspired from:
 * https://www.w3resource.com/java-exercises/stream/index.php
 */

import org.javagrader.Grade;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@Grade
public class VariousStreamsTest {

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testAverage() {
        assertEquals(11.714285714285714, VariousStreams.computeAverage(Stream.of(1, 3, 6, 8, 10, 18, 36)), 0.00000001);
        assertEquals(0.0, VariousStreams.computeAverage(Stream.empty()), 0.00000001);
    }

    static private void checkStrings(Stream<String> expected,
                                     Stream<String> result) {
        List<String> a = expected.collect(Collectors.toList());
        List<String> b = result.collect(Collectors.toList());
        assertEquals(a.size(), b.size());
        for (int i = 0; i < a.size(); i++) {
            assertEquals(a.get(i), b.get(i));
        }
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testChangeCase() {
        checkStrings(Stream.of("RED", "GREEN", "WHITE", "ORANGE", "PINK"), VariousStreams.changeCase(Stream.of("RED", "grEEn", "white", "Orange", "pink"), true));
        checkStrings(Stream.of("red", "green", "white", "orange", "pink"), VariousStreams.changeCase(Stream.of("RED", "grEEn", "white", "Orange", "pink"), false));
        checkStrings(Stream.empty(), VariousStreams.changeCase(Stream.empty(), true));
        checkStrings(Stream.empty(), VariousStreams.changeCase(Stream.empty(), false));
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testSumEvenOrOdd() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        assertEquals(0, VariousStreams.getSumOfEvenOrOddNumbers(Stream.empty(), true));
        assertEquals(0, VariousStreams.getSumOfEvenOrOddNumbers(Stream.empty(), false));
        assertEquals(30, VariousStreams.getSumOfEvenOrOddNumbers(numbers.stream(), true));
        assertEquals(25, VariousStreams.getSumOfEvenOrOddNumbers(numbers.stream(), false));
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testRemoveDuplicates() {
        List<Integer> lst = VariousStreams.removeDuplicates(Stream.of(10, 23, 22, 23, 24, 24, 33, 15, 26, 15)).collect(Collectors.toList());
        assertEquals(7, lst.size());
        Set<Integer> s = new HashSet<>();
        for (Integer i : lst) {
            s.add(i);
        }
        assertTrue(s.contains(10));
        assertTrue(s.contains(23));
        assertTrue(s.contains(22));
        assertTrue(s.contains(24));
        assertTrue(s.contains(33));
        assertTrue(s.contains(15));
        assertTrue(s.contains(26));
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testCountStringsWithFirstLetter() {
        List<String> lst = Arrays.asList("Red", "Green", "Blue", "Pink", "Brown");
        assertEquals(0, VariousStreams.countStringsWithFirstLetter(Stream.empty(), 'R'));
        assertEquals(1, VariousStreams.countStringsWithFirstLetter(lst.stream(), 'R'));
        assertEquals(1, VariousStreams.countStringsWithFirstLetter(lst.stream(), 'P'));
        assertEquals(2, VariousStreams.countStringsWithFirstLetter(lst.stream(), 'B'));
        assertEquals(0, VariousStreams.countStringsWithFirstLetter(lst.stream(), 'Y'));
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testSortStrings() {
        List<String> lst = Arrays.asList("Red", "Green", "Blue", "Pink", "Brown");
        assertEquals(0, VariousStreams.sortAscendingOrDescending(Stream.empty(), true).count());
        assertEquals(0, VariousStreams.sortAscendingOrDescending(Stream.empty(), false).count());
        checkStrings(Stream.of("Blue", "Brown", "Green", "Pink", "Red"), VariousStreams.sortAscendingOrDescending(lst.stream(), true));
        checkStrings(Stream.of("Red", "Pink", "Green", "Brown", "Blue"), VariousStreams.sortAscendingOrDescending(lst.stream(), false));
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testMinMax() {
        Optional<VariousStreams.MinMaxValue> a = VariousStreams.computeMinMaxValue(Stream.empty());
        assertFalse(a.isPresent());

        a = VariousStreams.computeMinMaxValue(Stream.of(10));
        assertTrue(a.isPresent());
        assertEquals(10, a.get().getMinValue());
        assertEquals(10, a.get().getMaxValue());

        a = VariousStreams.computeMinMaxValue(Stream.of(1, 17, 54, 14, 14, 33, 45, -11));
        assertTrue(a.isPresent());
        assertEquals(-11, a.get().getMinValue());
        assertEquals(54, a.get().getMaxValue());
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testFibonacci() {
        List<Integer> a = VariousStreams.generateFibonacci().limit(5).collect(Collectors.toList());
        assertEquals(5, a.size());
        assertEquals(2, a.get(0));
        assertEquals(3, a.get(1));
        assertEquals(5, a.get(2));
        assertEquals(8, a.get(3));
        assertEquals(13, a.get(4));

        a = VariousStreams.generateFibonacci().skip(5).limit(5).collect(Collectors.toList());
        assertEquals(5, a.size());
        assertEquals(21, a.get(0));
        assertEquals(34, a.get(1));
        assertEquals(55, a.get(2));
        assertEquals(89, a.get(3));
        assertEquals(144, a.get(4));
    }
}
