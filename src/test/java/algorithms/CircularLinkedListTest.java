package algorithms;

import org.javagrader.ConditionalOrderingExtension;
import org.javagrader.Grade;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

// BEGIN STRIP
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.Random;
// END STRIP

import java.util.*;


@ExtendWith(ConditionalOrderingExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Grade
public class CircularLinkedListTest {

    // BEGIN STRIP
    private final Random r = new Random(487752);
    static Stream<Integer> numberRepetitionParameterized() {
        return IntStream.rangeClosed(1, 50).mapToObj(i -> i);
    }

    // END STRIP

    @Grade(value=0.5)
    @Order(0)
    @Test
    public void simpleTestWithoutRemove(){
        // Build a circular linked list as follows
        // 0 -> 1 -> 2 -> 3 -> 4 -> 5 -> 6 -> 7 -> 8 -> 9
        CircularLinkedList<Integer> student = new CircularLinkedList<>();
        List<Integer> correct = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            student.enqueue(i);
            correct.add(i);
        }
        Iterator<Integer> aIter = student.iterator();
        Iterator<Integer> bIter = correct.iterator();
        assertEquals(correct.size(),student.size());
        while (bIter.hasNext()) {
            assertTrue(aIter.hasNext());
            assertEquals(bIter.next(),aIter.next());
        }
    }

    @Grade(value=0.5)
    @Order(0)
    @Test
    public void simpleTestWithRemoving(){
        // Build a circular linked list as follows
        // 0 -> 1 -> 2 -> 3 -> 4 -> 5 -> 6 -> 7 -> 8 -> 9 .. -> 49
        CircularLinkedList<Integer> student = new CircularLinkedList<>();
        List<Integer> correct = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            student.enqueue(i);
            correct.add(i);
        }

        // Remove 0, 10, [End], add 50, remove 25, 30
        student.remove(0);
        correct.remove(0);
        student.remove(10);
        correct.remove(10);
        student.remove(correct.size() - 1);
        correct.remove(correct.size() - 1);
        student.enqueue(50);
        correct.add(50);
        student.remove(25);
        correct.remove(25);
        student.remove(30);
        correct.remove(30);


        Iterator<Integer> aIter = student.iterator();
        Iterator<Integer> bIter = correct.iterator();
        assertEquals(correct.size(),student.size());
        while (bIter.hasNext()) {
            assertTrue(aIter.hasNext());
            assertEquals(bIter.next(),aIter.next());
        }
    }

    // BEGIN STRIP

    @Grade(value=0.5)
    @Order(1)
    @ParameterizedTest
    @MethodSource("numberRepetitionParameterized")
    public void runAsExpected(Integer i) {
        CircularLinkedList<Integer> student = new CircularLinkedList<>();
        List<Integer> correct = new ArrayList<>();
        for (int k = 0; k < 100; k++) {
            int v = r.nextInt();
            student.enqueue(v);
            correct.add(v);
        }
        if (i%2 == 0) {
            student.remove(10);
            correct.remove(10);
            student.remove(0);
            correct.remove(0);
            student.remove(student.size()-1);
            correct.remove(correct.size()-1);
        }
        Iterator<Integer> aIter = student.iterator();
        Iterator<Integer> bIter = correct.iterator();
        assertEquals(correct.size(),student.size());
        while (bIter.hasNext()) {
            assertTrue(aIter.hasNext());
            assertEquals(bIter.next(),aIter.next());
        }
        assertFalse(bIter.hasNext());
        assertFalse(aIter.hasNext());
    }

    @Test
    @Order(2)
    @Grade(value=15)
    public void testIteratorList() {
        for (int i = 0; i < 20; i++) {
            CircularLinkedList<Integer> a = new CircularLinkedList<>();
            assertEquals(0,a.size());
            a.enqueue(i);
            assertEquals(1,a.size());
            Iterator<Integer> itera = a.iterator();
            assertTrue(itera.hasNext());
            assertEquals(i,itera.next());

            CircularLinkedList<Integer> b = new CircularLinkedList<>();
            b.enqueue(i);
            b.remove(0);
            Iterator<Integer> iterb = b.iterator();
            assertFalse(iterb.hasNext());

        }
    }

    @Test
    @Order(3)
    @Grade(value=5)
    public void testOutOfBound() {
        CircularLinkedList<Integer> a = new CircularLinkedList<>();
        a.enqueue(3);
        assertThrows(IndexOutOfBoundsException.class, () -> {
            a.remove(1);
        });
    }

    @Test
    @Order(4)
    @Grade(value=5)
    public void testConcurrentModificationNext() {
        CircularLinkedList<Integer> a = new CircularLinkedList<>();
        Iterator<Integer> iter = a.iterator();
        a.enqueue(3);
        assertThrows(ConcurrentModificationException.class, () -> { iter.next(); });
    }

    static IntStream numberRepetitionComplexity() {
        return IntStream.rangeClosed(1, 5);
    }

    @Grade(value=10)
    @Order(5)
    @ParameterizedTest
    @MethodSource("numberRepetitionComplexity")
    public void testComplexityEnqueue(int i) {
        CircularLinkedList<Integer> a = new CircularLinkedList<>();
        for (int k = 0; k < 1_000_000; k++) {
            a.enqueue(k);
        }
    }
    // END STRIP
}
