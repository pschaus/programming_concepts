package oop;

import org.javagrader.Grade;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.Random;


public class RecursiveStackTest {




    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void test1() {
        RecursiveStack<Integer> e = new RecursiveStack<>();

        assertEquals(0, e.size());

        e = e.add(2);
        e = e.add(3);
        e = e.add(4);



        assertEquals(3, e.size());
        assertEquals(Integer.valueOf(4), e.top());
        e = e.removeTop(); // remove 4
        assertEquals(2, e.size());
        assertEquals(Integer.valueOf(3), e.top());
        e = e.removeTop(); // remove 3
        assertEquals(1, e.size());
        assertEquals(Integer.valueOf(2), e.top());
        e = e.removeTop(); // remove 2
        assertEquals(0, e.size());

    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testReverseAlone() {
        RecursiveStack<Integer> e = new RecursiveStack<>();

        e = e.add(2);
        e = e.add(3);
        e = e.add(4);

        RecursiveStack re = e.reverse();



        assertEquals(Integer.valueOf(2),re.top());
        re = re.removeTop();
        assertEquals(Integer.valueOf(3),re.top());
        re = re.removeTop();
        assertEquals(Integer.valueOf(4),re.top());
        re = re.removeTop();

        e = e.reverse().reverse();

        assertEquals(Integer.valueOf(4),e.top());
        e = e.removeTop();
        assertEquals(Integer.valueOf(3),e.top());
        e = e.removeTop();
        assertEquals(Integer.valueOf(2),e.top());
        e = e.removeTop();


    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testReverse() {
        RecursiveStack<Integer> e = new RecursiveStack<>();

        e = e.add(2);
        e = e.add(3);
        e = e.add(4);

        RecursiveStack re = e.reverse();

        Iterator<Integer> ite = re.iterator();
        assertTrue(ite.hasNext());
        assertEquals(Integer.valueOf(2),ite.next());
        assertTrue(ite.hasNext());
        assertEquals(Integer.valueOf(3),ite.next());
        assertTrue(ite.hasNext());
        assertEquals(Integer.valueOf(4),ite.next());
        assertFalse(ite.hasNext());
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testEmptyIterator() {
        RecursiveStack<Integer> e = new RecursiveStack<>();

        Iterator<Integer> ite = e.iterator();
        assertFalse(ite.hasNext());
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testIteratorTopDown() {
        RecursiveStack<Integer> e = new RecursiveStack<>();

        e = e.add(2);
        e = e.add(3);
        e = e.add(4);

        Iterator<Integer> ite = e.iterator();
        assertTrue(ite.hasNext());
        assertEquals(Integer.valueOf(4),ite.next());
        assertTrue(ite.hasNext());
        assertEquals(Integer.valueOf(3),ite.next());
        assertTrue(ite.hasNext());
        assertEquals(Integer.valueOf(2),ite.next());
        assertFalse(ite.hasNext());
    }

    /*
    @Test(expected = EmptyStackException.class)
    @Grade(value = 1, cpuTimeout = 1000)
    public void testEmptyTop() {
        RecursiveStack<Integer> e = new RecursiveStack<>();
        int v = e.top();
    }


    @Test(expected = EmptyStackException.class)
    @Grade(value = 1, cpuTimeout = 1000)
    public void testEmpty() {
        RecursiveStack<Integer> e = new RecursiveStack<>();

        e = e.add(2);
        e = e.add(3);

        e = e.removeTop();
        e = e.removeTop();
        e = e.removeTop();
    }
     */

    // BEGIN STRIP

    // ---------------------- inginious only -----------------

    public static int[] randomTable(int n) {
        Random r = new Random(n);

        int[] values = new int[n];
        for (int i = 0; i < values.length; i++) {
            values[i] = r.nextInt();
        }
        return  values;
    }



    @Grade(value = 1, cpuTimeout = 10)
    public void testLargeSizeInternalState() {
        RecursiveStack<Integer> stack = new RecursiveStack<>();

        int [] values = randomTable(100);
        int size = 0;

        for (int i = 0; i < values.length; i++) {
            stack = stack.add(values[i]);
            size++;
        }

        // check internal state
        RecursiveStack current = stack;
        for (int i = size-1; i >= 0; i--) {
            assertEquals(values[i],current.e);
            current = current.next;
        }
        assertTrue(current.next == null);



        for (int i = 0; i < values.length/2; i++) {
            stack = stack.removeTop();
            size--;
        }

        current = stack;
        for (int i = size-1; i >= 0; i--) {
            assertEquals(values[i],current.e);
            current = current.next;
        }
        assertTrue(current.next == null);
    }


    @Test
    @Grade(value = 1, cpuTimeout = 10)
    public void stackUnChanged() {
        RecursiveStack<Integer> stack = new RecursiveStack<>();

        int [] values = randomTable(120);

        int size = 1;
        stack = stack.add(values[0]);
        for (int i = 1; i < values.length; i++) {
            Integer topBefore = stack.top();
            RecursiveStack<Integer> newStack = stack.add(values[i]);
            assertEquals(topBefore,stack.top());
            stack = newStack;
            assertEquals(Integer.valueOf(values[i]),stack.e);
            size++;
        }

        stack.reverse();
        stack.iterator();
        stack.removeTop();
        stack.top();
        stack.size();
        stack.add(0); // add one more and verify that the stack is unchanged

        // check that the stack is not changed
        assertEquals(stack.size(),values.length);
        RecursiveStack current = stack;
        for (int i = size-1; i >= 0; i--) {
            assertEquals(Integer.valueOf(values[i]),current.e);
            current = current.next;
        }
        assertTrue(current.next == null);

        for (int i = 0; i < values.length/2; i++) {
            Integer topBefore = stack.top();
            RecursiveStack<Integer> newStack = stack.removeTop();
            assertEquals(topBefore,stack.top());
            stack = newStack;
            size++;
        }
    }

    @Test
    @Grade(value = 1, cpuTimeout = 10)
    public void largeIterator() {
        RecursiveStack<Integer> stack = new RecursiveStack<>();

        int [] values = randomTable(120);

        for (int v: values) {
            stack = stack.add(v);
        }

        int i = values.length-1;
        for (int v: stack) {
            assertEquals(values[i],v);
            i--;
        }
    }

    @Test
    @Grade(value = 1, cpuTimeout = 10)
    public void largeReverse() {
        RecursiveStack<Integer> stack = new RecursiveStack<>();

        int [] values = randomTable(120);

        for (int v: values) {
            stack = stack.add(v);
        }

        stack = stack.reverse();

        int i = 0;
        for (int v: stack) {
            assertEquals(values[i],v);
            i++;
        }

        stack = stack.reverse();

        i = values.length-1;
        for (int v: stack) {
            assertEquals(values[i],v);
            i--;
        }
    }

    // END STRIP


}