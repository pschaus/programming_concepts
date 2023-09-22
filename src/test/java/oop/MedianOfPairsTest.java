package oop;

import org.javagrader.Grade;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.LinkedList;
import java.util.List;

@Grade
public class MedianOfPairsTest {

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void test1() {

        List<MedianOfPairs.Pair> pairs = new LinkedList<>();
        pairs.add(new MedianOfPairs.Pair(4, 1));
        pairs.add(new MedianOfPairs.Pair(3, 8));
        pairs.add(new MedianOfPairs.Pair(4, 3));
        pairs.add(new MedianOfPairs.Pair(9, 1));
        pairs.add(new MedianOfPairs.Pair(3, 6));

        MedianOfPairs.Pair res = MedianOfPairs.median(pairs);

        assertEquals(4, res.first);
        assertEquals(1, res.second);
    }

}
