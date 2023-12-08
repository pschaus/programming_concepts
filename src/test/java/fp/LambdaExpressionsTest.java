package fp;

import org.javagrader.Allow;
import org.javagrader.Grade;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.function.*;

@Grade
public class LambdaExpressionsTest {
    @Test
    @Grade(value = 1, cpuTimeout = 100)
    public void testSumOfIntegers() {
        BiFunction<Integer, Integer, Integer> f = (BiFunction) LambdaExpressions.sumOfIntegers();
        assertEquals(13, f.apply(7, 6));
        assertEquals(-20, f.apply(15, -35));
    }

    @Test
    @Grade(value = 1, cpuTimeout = 100)
    public void testIsEmptyString() {
        Predicate<String> f = (Predicate) LambdaExpressions.isEmptyString();
        assertTrue(f.test(""));
        assertFalse(f.test("A"));
        assertFalse(f.test("Hello"));
    }

    @Test
    @Grade(value = 1, cpuTimeout = 100)
    public void testIsOddNumber() {
        Predicate<Integer> f = (Predicate) LambdaExpressions.isOddNumber();
        assertFalse(f.test(0));
        assertTrue(f.test(1));
        assertTrue(f.test(3));
        assertFalse(f.test(4));
    }

    @Test
    @Grade(value = 1, cpuTimeout = 100)
    public void testRemove() {
        List<Integer> lst = new ArrayList<>();
        lst.addAll(Arrays.asList(1, 5, 2, 6, 7, 9, 0));
        LambdaExpressions.removeEvenNumbers(lst);
        assertEquals(4, lst.size());
        assertEquals(1, lst.get(0));
        assertEquals(5, lst.get(1));
        assertEquals(7, lst.get(2));
        assertEquals(9, lst.get(3));
    }

    @Test
    @Grade(value = 1, cpuTimeout = 100)
    public void testComputeFactorial() {
        Function<Integer, Integer> f = (Function) LambdaExpressions.computeFactorial();
        assertThrows(IllegalArgumentException.class, () -> f.apply(-1));
        assertEquals(1, f.apply(0));
        assertEquals(1, f.apply(1));
        assertEquals(2, f.apply(2));
        assertEquals(6, f.apply(3));
        assertEquals(24, f.apply(4));
        assertEquals(120, f.apply(5));
        assertEquals(3628800, f.apply(10));
    }

    @Test
    @Grade(value = 1, cpuTimeout = 100)
    public void testComputeMeanOfListOfDoubles() {
        Function<List<Double>, Double> f = (Function) LambdaExpressions.computeMeanOfListOfDoubles();
        assertThrows(IllegalArgumentException.class, () -> f.apply(new ArrayList<Double>()));
        assertEquals(10.0, f.apply(Arrays.asList(10.0)), 0.0001);
        assertEquals(15.0, f.apply(Arrays.asList(10.0, 20.0)), 0.0001);
        assertEquals(20.0, f.apply(Arrays.asList(10.0, 20.0, 30.0)), 0.0001);
    }

    @Test
    @Grade(value = 1, cpuTimeout = 100)
    public void testListOfStringsToLowerCase() {
        Function<List<String>, List<String>> f = (Function) LambdaExpressions.listOfStringsToLowerCase();
        List<String> a = Arrays.asList("FOO", "bar", "Hello", "WORld");
        List<String> b = f.apply(a);
        assertEquals(4, b.size());
        assertEquals("foo", b.get(0));
        assertEquals("bar", b.get(1));
        assertEquals("hello", b.get(2));
        assertEquals("world", b.get(3));
    }

    @Test
    @Grade(value = 1, cpuTimeout = 100)
    public void testConcatenateStrings() {
        BiFunction<String, String, String> f = (BiFunction) LambdaExpressions.concatenateStrings();
        assertEquals("", f.apply("", ""));
        assertEquals("Hello", f.apply("Hello", ""));
        assertEquals("World", f.apply("", "World"));
        assertEquals("HelloWorld", f.apply("Hello", "World"));
    }

    @Test
    @Grade(value = 1, cpuTimeout = 100)
    public void testComputeMinMax() {
        assertFalse(LambdaExpressions.computeMinMax().apply(new ArrayList<Integer>()).isPresent());

        List<Integer> a = Arrays.asList(-10, -20, 30, 15);
        Optional<LambdaExpressions.MinMaxResult> r = LambdaExpressions.computeMinMax().apply(a);
        assertTrue(r.isPresent());
        assertEquals(-20, r.get().getMinValue());
        assertEquals(30, r.get().getMaxValue());
    }

    @Test
    @Grade(value = 1, cpuTimeout = 100)
    public void testCountInstancesOfLetter() {
        BiFunction<String, Character, Integer> f = (BiFunction) LambdaExpressions.countInstancesOfLetter();
        assertEquals(0, f.apply("Hello", 'x'));
        assertEquals(1, f.apply("Hello", 'H'));
        assertEquals(2, f.apply("Hello", 'l'));
    }
}
