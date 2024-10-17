package algorithms;

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

    // BEGIN STRIP
    
    private BinarySearchTree makeTree(int ... elements) {
        BinarySearchTree tree = new BinarySearchTree();
        for (int element : elements) {
            tree.insert(element);
        }
        return tree;
    }
    
    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testLinearLeft() {
        BinarySearchTree tree = makeTree(30, 20, 19, 3, 2, 1);
        List<Integer> result = tree.decreasing();
        List<Integer> answer = Arrays.asList(30, 20, 19, 3, 2, 1);
        assertEquals(answer, result);
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testLinearRight() {
        BinarySearchTree tree = makeTree(1, 20, 32, 40, 51);
        List<Integer> result = tree.decreasing();
        List<Integer> answer = Arrays.asList(51, 40, 32, 20, 1);
        assertEquals(answer, result);
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testBalanced() {
        BinarySearchTree tree = makeTree(40, 20, 30, 12, 60, 70, 50);
        List<Integer> result = tree.decreasing();
        List<Integer> answer = Arrays.asList(70, 60, 50, 40, 30, 20, 12);
        assertEquals(answer, result);
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testSingleNode() {
        BinarySearchTree tree = makeTree(20);
        List<Integer> result = tree.decreasing();
        List<Integer> answer = Arrays.asList(20);
        assertEquals(answer, result);
    }

    // END STRIP

}
