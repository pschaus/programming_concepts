package oop;

import org.javagrader.Grade;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


import java.util.Arrays;
import java.util.List;

@Grade
public class BinarySearchTreeTest {

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void test1() {

        BinarySearchTree t = new BinarySearchTree();
        t.insert(2);
        t.insert(9);
        t.insert(5);
        t.insert(2);
        t.insert(6);
        t.insert(0);
        t.insert(9);
        t.insert(10);

        // 10 - 9 - 6 - 5 - 2 - 0

        List<Integer> result = t.decreasing();

        List<Integer> answer = Arrays.asList(10,9,6,5,2,0);

        assertEquals(answer,result);


    }

}
