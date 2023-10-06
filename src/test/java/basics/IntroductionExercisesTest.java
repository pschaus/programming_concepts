package basics;

import org.javagrader.Grade;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Grade
public class IntroductionExercisesTest {
    
    @Test
    @Grade(value = 1)
    public void testAttribute() {
        IntroductionExercises.attribute(42);
        assertEquals(42, IntroductionExercises.variable);
    }

    @Test
    @Grade(value = 1)
    public void testAdd() {
        assertEquals(42, IntroductionExercises.add(40, 2));
    }

    @Test
    @Grade(value = 1)
    public void testEqualsIntegers() {
        assertTrue(IntroductionExercises.equalsIntegers(42, 42));
        assertFalse(IntroductionExercises.equalsIntegers(42, 43));
    }

    @Test
    @Grade(value = 1)
    public void testMax() {
        assertEquals(42, IntroductionExercises.max(40, 42));
        assertEquals(42, IntroductionExercises.max(42, 40));
    }

    @Test
    @Grade(value = 1)
    public void testMiddleValue() {
        assertEquals(42, IntroductionExercises.middleValue(40, 42, 44));
        assertEquals(42, IntroductionExercises.middleValue(44, 42, 40));
        assertEquals(42, IntroductionExercises.middleValue(42, 40, 44));
        assertEquals(42, IntroductionExercises.middleValue(44, 40, 42));
        assertEquals(42, IntroductionExercises.middleValue(40, 44, 42));
        assertEquals(42, IntroductionExercises.middleValue(42, 44, 40));
    }

    @Test
    @Grade(value = 1)
    public void testGreetings() {
        assertEquals("Good morning, sir!", IntroductionExercises.greetings("Morning"));
        assertEquals("Hello, sir!", IntroductionExercises.greetings("Afternoon"));
        assertEquals("Good evening, sir!", IntroductionExercises.greetings("Evening"));
    }

    @Test
    @Grade(value = 1)
    public void testLastFirstMiddle(){
        int[] a = {1,2,3,4,5,6,7,8,9,10};
        int[] res = IntroductionExercises.lastFirstMiddle(a);
        assertArrayEquals(new int[]{10,1,6}, res);
        a = new int[]{2,4,6,8,10};
        res = IntroductionExercises.lastFirstMiddle(a);
        assertArrayEquals(new int[]{10,2,6}, res);
    }

    @Test
    @Grade(value = 1)
    public void testSum(){
        int[] a = {1,2,3,4,5,6,7,8,9,10};
        assertEquals(55, IntroductionExercises.sum(a));
        a = new int[]{2,4,6,8,10};
        assertEquals(30, IntroductionExercises.sum(a));
    }

    @Test
    @Grade(value = 1)
    public void testMaxArray(){
        int[] a = {1,2,3,4,5,6,7,8,9,10};
        assertEquals(10, IntroductionExercises.maxArray(a));
        a = new int[]{20,4,6,8,10};
        assertEquals(20, IntroductionExercises.maxArray(a));
    }

    @Test
    @Grade(value = 1)
    public void testMain(){
        IntroductionExercises.main(new String[]{"2", "5", "2.5"});
        assertArrayEquals(new int[]{4, 25, 0}, IntroductionExercises.squares);
    }
}
