package algorithms;

import java.util.Optional;

import org.javagrader.Grade;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Grade
public class CircularLinkedListTest {

    // BEGIN STRIP
    private void assertLinkedListState(CircularLinkedList list, int[] expectedValues) {
        assertEquals(expectedValues.length, list.size);
        if (expectedValues.length == 0) {
            assertFalse(list.getFirst().isPresent());
            assertFalse(list.getLast().isPresent());
            return;
        }
        
        Optional<CircularLinkedList.Node> currentOpt = list.getFirst();
        for (int expectedValue : expectedValues) {
            assertTrue(currentOpt.isPresent());
            CircularLinkedList.Node current = currentOpt.get();
            assertEquals(expectedValue, current.value);
            currentOpt = current.next;
        }
        
        // Check circularity
        if (list.getLast().isPresent()) {
            CircularLinkedList.Node lastNode = list.getLast().get();
            assertTrue(lastNode.next.isPresent());
            assertEquals(list.getFirst().get(), lastNode.next.get());
        }
    }
    // END STRIP
    
    @Test
    @Grade(value = 1)
    public void testSimple() {
        CircularLinkedList list = new CircularLinkedList();
        assertTrue(list.isEmpty());

        list.enqueue(0);

        assertFalse(list.isEmpty());
        Optional<CircularLinkedList.Node> first = list.getFirst();
        assertTrue(first.isPresent());
        assertEquals(0, first.get().value);
        
        Optional<CircularLinkedList.Node> last = list.getLast();
        // Here we compare the references since last and first must be the same object
        assertTrue(last.isPresent());
        assertTrue(first.get() == last.get());

        list.enqueue(1);
        list.enqueue(2);

        first = list.getFirst();
        assertTrue(first.isPresent());
        assertEquals(0, first.get().value);

        assertEquals(3, list.size);
        int[] array = new int[]{0, 1, 2};
        // STUDENT CircularLinkedList.Node current = list.first.get();
        // STUDENT for (int i = 0; i < array.length; i++) {
        // STUDENT     assertEquals(array[i], current.value);
        // STUDENT     current = current.next.get();
        // STUDENT }            
        // BEGIN STRIP
        assertLinkedListState(list, array);
        // END STRIP
    }

    // BEGIN STRIP
    @Test
    @Grade(value = 1)
    public void testLength0() {
        CircularLinkedList list = new CircularLinkedList();
        assertTrue(list.isEmpty());
        int[] array = new int[]{};
        assertLinkedListState(list, array);
    }

    @Test
    @Grade(value = 1)
    public void testLength1() {
        CircularLinkedList list = new CircularLinkedList();
        list.enqueue(0);
        assertFalse(list.isEmpty());
        int[] array = new int[]{0};
        assertLinkedListState(list, array);
    }

    @Test
    @Grade(value = 1)
    public void testRemove() {
        CircularLinkedList list = new CircularLinkedList();
        list.enqueue(0);
        list.enqueue(1);
        list.enqueue(2);
        list.enqueue(3);

        assertEquals(4, list.size);
        int[] array = new int[]{0, 1, 2, 3};
        assertLinkedListState(list, array);

        int result = list.remove(2);
        assertEquals(2, result);
        array = new int[]{0, 1, 3};
        assertLinkedListState(list, array);

        result = list.remove(2);
        assertEquals(3, result);
        array = new int[]{0, 1};
        assertLinkedListState(list, array);
    }

    @Test
    @Grade(value = 1)
    public void testRemoveToEmpty() {
        CircularLinkedList list = new CircularLinkedList();
        list.enqueue(0);

        assertEquals(1, list.size);
        int[] array = new int[]{0};
        assertLinkedListState(list, array);

        int result = list.remove(0);
        assertEquals(0, result);
        array = new int[]{};
        assertLinkedListState(list, array);
    }

    @Test
    @Grade(value = 1)
    public void testRemoveOutOfBounds() {
        CircularLinkedList list = new CircularLinkedList();

        int result = list.remove(0); // Should not crash, should return -1
        assertEquals(-1, result);
    }
    // END STRIP
    
}