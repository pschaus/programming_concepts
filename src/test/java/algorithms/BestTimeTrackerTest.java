package algorithms;

import org.javagrader.Grade;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;


@Grade
class BestTimeTrackerTest {

    @Test
    @Grade(value = 1, cpuTimeout = 1)
    public void getBestTime() {
        BestTimeTracker tracker = new BestTimeTracker();
        tracker.addTime("Alice", 12.5);
        tracker.addTime("Bob", 10.3);
        tracker.addTime("Alice", 11.2);
        tracker.addTime("Charlie", 9.8);
        tracker.addTime("Bob", 10.0);
        tracker.addTime("Charlie", 12.8);


        assertEquals(tracker.getBestTime("Alice"), 11.2, 0.00001);
        assertEquals(tracker.getBestTime("Bob"), 10, 0.00001);
        assertEquals(tracker.getBestTime("Charlie"), 9.8, 0.00001);
        assertNull(tracker.getBestTime("Joe"));
    }


    @Test
    @Grade(value = 1, cpuTimeout = 1)
    public void iterator() {

        BestTimeTracker tracker = new BestTimeTracker();
        tracker.addTime("Alice", 12.5);
        tracker.addTime("Bob", 10.3);
        tracker.addTime("Alice", 11.2);
        tracker.addTime("Charlie", 9.8);
        tracker.addTime("Bob", 10.0);
        tracker.addTime("Charlie", 12.8);

        // Charlie 9.8, Bob 10.0, Alice 11.2,

        ArrayList<String> sortedParticipants = new ArrayList<>();
        for (String participant: tracker) { // implicitly call your iterator
            sortedParticipants.add(participant);
        }

        ArrayList<String> answer = new ArrayList<>();
        answer.add("Charlie");
        answer.add("Bob");
        answer.add("Alice");

        assertEquals(answer, sortedParticipants);


    }

    @Test
    @Grade(value = 1, cpuTimeout = 1)
    public void testFailSafe() {
        BestTimeTracker tracker = new BestTimeTracker();
        tracker.addTime("Alice", 12.5);
        tracker.addTime("Bob", 11.3);

        {
            Iterator<String> it = tracker.iterator();
            assertTrue(it.hasNext());
            it.next();
            assertTrue(it.hasNext());
            it.next();
            assertFalse(it.hasNext());
        }

        {
            Iterator<String> it = tracker.iterator();
            assertTrue(it.hasNext());
            it.next();
            assertTrue(it.hasNext());
            tracker.addTime("Bob", 10.2);
            assertThrows(ConcurrentModificationException.class, () -> it.next());
        }

        {
            Iterator<String> it = tracker.iterator();
            assertTrue(it.hasNext());
            it.next();
            assertTrue(it.hasNext());
            tracker.addTime("Bob", 10.1);
            assertThrows(ConcurrentModificationException.class, () -> it.hasNext());
        }
    }

    // BEGIN STRIP

    @Test
    @Grade(value = 3, cpuTimeout = 1)
    public void getBestTimeHidden() {
        BestTimeTracker tracker = new BestTimeTracker();
        tracker.addTime("Alice", 12.5);
        tracker.addTime("Bob", 10.3);
        tracker.addTime("Alice", 11.2);
        tracker.addTime("Alice", 11.1);
        tracker.addTime("Alice", 11.3);
        tracker.addTime("Charlie", 9.8);
        tracker.addTime("Bob", 10.0);
        tracker.addTime("Bob", 11.0);
        tracker.addTime("Charlie", 12.8);
        tracker.addTime("Bob", 12.0);
        tracker.addTime("Charlie", 12.9);

        assertNull(tracker.getBestTime("Zoe"));
        assertEquals(tracker.getBestTime("Alice"), 11.1, 0.00001);
        assertEquals(tracker.getBestTime("Bob"), 10, 0.00001);
        assertEquals(tracker.getBestTime("Charlie"), 9.8, 0.00001);
        assertNull(tracker.getBestTime("Joe"));

    }


    @Test
    @Grade(value = 1, cpuTimeout = 1)
    public void iteratorHidden() {

        BestTimeTracker tracker = new BestTimeTracker();
        tracker.addTime("Alice", 12.5);
        tracker.addTime("Bob", 10.3);
        tracker.addTime("Alice", 11.2);
        tracker.addTime("Charlie", 10.1);
        tracker.addTime("Bob", 10.0);
        tracker.addTime("Charlie", 12.8);
        tracker.addTime("Zoe", 6.0);

        ArrayList<String> sortedParticipants = new ArrayList<>();
        for (String participant: tracker) { // implicitly call your iterator
            sortedParticipants.add(participant);
        }

        ArrayList<String> answer = new ArrayList<>();
        answer.add("Zoe");
        answer.add("Bob");
        answer.add("Charlie");
        answer.add("Alice");

        assertEquals(answer, sortedParticipants);


    }


    @Test
    @Grade(value = 3, cpuTimeout = 1)
    public void iteratorOrderNotSensitive() {

        BestTimeTracker tracker = new BestTimeTracker();
        tracker.addTime("Alice", 12.5);
        tracker.addTime("Bob", 10.3);
        tracker.addTime("Alice", 11.2);
        tracker.addTime("Charlie", 10.1);
        tracker.addTime("Bob", 10.0);
        tracker.addTime("Charlie", 12.8);
        tracker.addTime("Zoe", 6.0);

        HashSet<String> participants = new HashSet<>();
        int nb = 0;
        for (String participant: tracker) { // implicitly call your iterator
            participants.add(participant);
            nb++;
        }

        assertEquals(4, nb);

        HashSet<String> answer = new HashSet<>();
        answer.add("Charlie");
        answer.add("Zoe");
        answer.add("Bob");
        answer.add("Alice");

        assertEquals(answer, participants);


    }

    // END STRIP
}
