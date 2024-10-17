package algorithms;

import org.javagrader.Grade;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

@Grade
public class TreeHashtableTest {

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void test() {

        TreeHashtable map = new TreeHashtable();
        map.put("Apple", 5);
        map.put("Orange", 10);
        map.put("Banana", 1);
        map.put("Grape", 20);

        assertEquals(1, map.delete("Banana"));
        assertNull(map.delete("Peach"));
    }

    // BEGIN STRIP

    private TreeHashtable makeMap(int n) {
        TreeHashtable map = new TreeHashtable();
        for (int i = 0; i < n; i++) {
            map.put(String.valueOf(i), i);
        }
        return map;
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testCorrectlyRemove() {
        TreeHashtable map = makeMap(10);
        assertEquals(4, map.delete("4"));
        assertNull(map.delete("4"));
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testDeleteWithCollisions() {
        TreeHashtable map = makeMap(100);
        List<Integer> elements = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            elements.add(i);
        }
        Collections.shuffle(elements);
        for (Integer element : elements) {
            assertEquals(element, map.delete(String.valueOf(element)));
            assertEquals(null, map.delete(String.valueOf(element)));
        }
    }

    // END STRIP

}
