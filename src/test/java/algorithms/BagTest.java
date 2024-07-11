package algorithms;

import org.javagrader.Grade;
import org.javagrader.GradeFeedback;
import org.javagrader.TestResultStatus;
import org.junit.jupiter.api.Test;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;


@Grade
class BagTest {

    @Test
    @Grade(value = 1, cpuTimeout = 1)
    public void testAddIsEmpty() {
        Bag bag = Bag.create();
        assertTrue(bag.isEmpty());
        bag.add(1);
        assertFalse(bag.isEmpty());
        bag.add(-5);
        assertFalse(bag.isEmpty());
    }


    @Test
    @Grade(value = 1, cpuTimeout = 1)
    public void testAddRemoveIsEmpty() {
        Bag bag = Bag.create();
        bag.add(1);
        bag.add(1);
        bag.add(-5);
        bag.remove(1);
        bag.remove(11); // does nothing since 11 is not in the bag
        bag.remove(1);
        assertFalse(bag.isEmpty());
        bag.remove(-5);
        assertTrue(bag.isEmpty());
    }


    @Test
    @Grade(value = 1, cpuTimeout = 1)
    public void testAddCount() {
        Bag bag = Bag.create();
        bag.add(1);
        bag.add(1);
        bag.add(2);
        assertEquals(2, bag.count(1));
        assertEquals(1, bag.count(2));
        assertEquals(0, bag.count(-7)); // -7 is not in the bag
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1)
    public void testAddRemoveCount() {
        Bag bag = Bag.create();
        bag.add(1);
        bag.add(1);
        bag.add(2);
        bag.add(-7);
        assertEquals(2, bag.count(1));
        assertEquals(1, bag.count(2));
        assertEquals(1, bag.count(-7));
        bag.remove(1);
        assertEquals(1, bag.count(1));
        bag.remove(88);
        assertEquals(1, bag.count(1));
        bag.remove(-7);
        bag.remove(1);
        assertEquals(0, bag.count(-7));
        assertEquals(0, bag.count(1));
    }



    @Test
    @Grade(value = 1, cpuTimeout = 1)
    public void testFilterAddRemoveIsEmpty() {
        Bag bag = Bag.create();
        bag.add(1);
        bag.add(2);
        bag.add(3);
        bag.add(4);
        bag.add(3);
        Bag filteredBag = bag.filter(x -> x % 2 != 0);
        // the filteredBag contains 1,3,3
        assertFalse(filteredBag.isEmpty());
        assertTrue(filteredBag.filter(x -> false).isEmpty()); // remove all the elements and test the result is empty
        filteredBag.remove(1);
        assertFalse(filteredBag.isEmpty());
        filteredBag.remove(3);
        assertFalse(filteredBag.isEmpty());
        filteredBag.remove(3);
        assertTrue(filteredBag.isEmpty());
    }



    @Test
    @Grade(value = 1, cpuTimeout = 1)
    public void testAddIterator() {
        Bag bag = Bag.create();
        bag.add(0);
        bag.add(1);
        bag.add(2);
        bag.add(0);
        bag.add(1);
        bag.add(2);
        int [] count = new int[3];
        for (int i : bag) {
            count[i]++;
        }
        for (int i = 0; i < 3; i++) {
            assertEquals(2, count[i]);
        }
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1)
    public void testAddFailFastIterator() {
        Bag bag = Bag.create();
        bag.add(0);
        bag.add(1);
        bag.add(2);
        bag.add(0);
        bag.add(1);
        bag.add(2);
        Iterator<Integer> ite = bag.iterator();
        ite.next();
        bag.add(3);
        assertThrows(ConcurrentModificationException.class, () -> {
            // an exception is thrown when the next() method is called because the bag has been modified
            // while the iterator was active
            ite.next();
        });
    }


    // BEGIN STRIP

    // ------------hidden tests----------------

    @Test
    @Grade(value = 1, cpuTimeout = 1)
    public void testAddRemoveIsEmptyHidden() {
        Bag bag = Bag.create();
        assertTrue(bag.isEmpty());
        bag.add(42);
        bag.add(42);
        bag.remove(42);
        assertFalse(bag.isEmpty());
        bag.remove(42);
        assertTrue(bag.isEmpty());
        bag.add(-42);
        bag.add(-42);
        bag.add(-5);
        bag.remove(-42);
        bag.remove(-5);
        assertFalse(bag.isEmpty());
        bag.remove(-42);
        assertTrue(bag.isEmpty());
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1)
    @GradeFeedback(message = "Problem with large size ?",on= TestResultStatus.FAIL)
    public void testAddRemoveIsEmptyCountHidden() {
        Bag bag = Bag.create();
        int n = 10000;
        int k = 1000;
        int [] count = new int[k];
        for (int i = 0; i < n; i++) {
            int v = i%k;
            count[v]++;
            bag.add(v);
            assertEquals(count[v], bag.count(v));
        }
        for (int i = 0; i < n; i++) {
            int v = i%k;
            count[v]--;
            bag.remove(v);
            assertEquals(count[v], bag.count(v));
        }
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1)
    public void testFilterCountHidden() {
        Bag bag = Bag.create();
        int n = 10000;
        int k = 1000;
        int [] count = new int[k];
        for (int i = 0; i < n; i++) {
            int v = i%k;
            count[v]++;
            bag.add(v);
            assertEquals(count[v], bag.count(v));
        }
        Bag filtered = bag.filter(x -> x % 2 == 0);

        for (int i = 0; i < k; i++) {
            int v = i%k;
            count[v]--;
            filtered.remove(v);
        }
        for (int i = 0; i < k; i++) {
            int v = i%k;
            if (v%2 == 0) {
                assertEquals(count[v], filtered.count(v));
            } else {
                assertEquals(0, filtered.count(v));
            }
        }


    }


    @Test
    @Grade(value = 1, cpuTimeout = 1)
    public void testIteratorHidden() {
        // all the same value
        Bag bag = Bag.create();
        int n = 10000;
        for (int i = 0; i < n; i++) {
            bag.add(10000);
        }
        int c = 0;
        for (int v : bag) {
            assertEquals(10000,v);
            c++;
        }
        assertEquals(n, c);

        bag = Bag.create();
        int k = 100;
        int [] count = new int[k];
        for (int i = 0; i < n; i++) {
            int v = i%k;
            count[v]++;
            bag.add(v);
            assertEquals(count[v], bag.count(v));
        }
        for (int v: bag) {
            count[v]--;
        }
        for (int i = 0; i < k; i++) {
            assertEquals(0, count[i]);
        }
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1)
    @GradeFeedback(message = "Problem with fail fast on remove, ConcurrentModificationException should be thrown with hasNext",on= TestResultStatus.FAIL)
    public void testFailFastHidden() {
        Bag bag = Bag.create();

        bag.add(0);
        bag.add(1);
        bag.add(2);
        bag.add(0);
        bag.add(1);
        bag.add(2);
        Iterator<Integer> ite = bag.iterator();
        ite.next();
        ite.next();
        bag.add(3);
        assertThrows(ConcurrentModificationException.class, () -> {
            // an exception is thrown when the next() method is called because the bag has been modified
            // while the iterator was active
            ite.next();
        });
    }



    // END STRIP




}