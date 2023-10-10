package correctness;


import org.javagrader.Grade;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;


public class SwapListTest {


    private static void run(int[] array) {
        LinkedList l = new LinkedList(array);
        SwapList.sort(l);
        int pops = l.getnPops();
        int swaps = l.getnSwaps();
        if (!l.isSorted())
            fail("Some test cases were incorrectly sorted.");
        if (pops > array.length * array.length)
            fail("Your code makes too many calls to `pop`. ");
        if (swaps > array.length * array.length)
            fail("Your code makes too many calls to `swaps`. ");
    }


    private static void runAll(int[][] arrays) {
        int statusAll = 0;
        int gradeAll = 0;
        for (int[] i : arrays) {
            run(i);
        }
    }

    @Test
    @Grade(value = 100, custom = true, cpuTimeout = 1000)
    public void alreadySorted() {
        int[][] arrays = new int[25][100];

        Random r = new Random(582);

        for (int i = 0; i < 25; i++) {
            arrays[i][0] = r.nextInt() % 10000;
            for (int j = 1; j < 100; j++)
                arrays[i][j] = arrays[i][j - 1] + Math.abs((r.nextInt() % 10000));
        }

        runAll(arrays);
    }

    @Test
    @Grade(value = 100, custom = true, cpuTimeout = 1000)
    public void reverseSorted() {
        int[][] arrays = new int[25][100];

        Random r = new Random(582);

        for (int i = 0; i < 25; i++) {
            arrays[i][99] = r.nextInt() % 10000;
            for (int j = 98; j >= 0; j--)
                arrays[i][j] = arrays[i][j + 1] + Math.abs((r.nextInt() % 10000));
        }

        runAll(arrays);
    }

    @Test
    @Grade(value = 100, custom = true, cpuTimeout = 1000)
    public void allSortedButOne() {
        int[][] arrays = new int[25][100];

        Random r = new Random(582);

        for (int i = 0; i < 25; i++) {
            arrays[i][0] = r.nextInt() % 10000;
            for (int j = 1; j < 100; j++)
                arrays[i][j] = arrays[i][j - 1] + Math.abs((r.nextInt() % 10000));

            int pos = r.nextInt(99);
            arrays[i][pos] = arrays[i][99] + Math.abs((r.nextInt() % 10000));
        }

        runAll(arrays);
    }

    @Test
    @Grade(value = 100, custom = true, cpuTimeout = 1000)
    public void twoDifferent() {
        int[][] arrays = new int[25][100];

        Random r = new Random(582);

        for (int i = 0; i < 25; i++) {
            int a = r.nextInt(1000000);
            int b = r.nextInt(1000000);
            for (int j = 0; j < 100; j++)
                arrays[i][j] = r.nextBoolean() ? a : b;
        }

        runAll(arrays);
    }

    @Test
    @Grade(value = 100, custom = true, cpuTimeout = 1000)
    public void bimonotonous() {
        int[][] arrays = new int[25][100];

        Random r = new Random(582);

        for (int i = 0; i < 25; i++) {
            int midPos = r.nextInt(98) + 1;
            boolean direction = r.nextBoolean();
            int multiplier = direction ? -1 : 1;

            arrays[i][midPos] = r.nextInt(10000);
            for (int j = midPos - 1; j >= 0; j--)
                arrays[i][j] = arrays[i][j + 1] + multiplier * r.nextInt(10000);
            for (int j = midPos + 1; j < 100; j++)
                arrays[i][j] = arrays[i][j - 1] + multiplier * r.nextInt(10000);
        }

        runAll(arrays);
    }

    @Test
    @Grade(value = 500, custom = true, cpuTimeout = 10000)
    public void random() {
        int[][] arrays = new int[125][100];

        Random r = new Random(582);

        for (int i = 0; i < 125; i++) {
            for (int j = 0; j < 100; j++)
                arrays[i][j] = r.nextInt();
        }

        runAll(arrays);
    }
}


// -------------------------


// Do not modify this class
class LinkedList implements SwapList {

    private Node first;
    private Node second;
    private Node tail;
    private int len;
    private int nSwaps;
    private int nPops;

    private class Node {

        private int value;
        private Node next;

        private Node(int v, Node n) {
            this.value = v;
            this.next = n;
        }

        private int getValue() {
            return value;
        }

        private void setNext(Node next) {
            this.next = next;
        }
    }

    /**
     * Create a new "linkedlist" with the content of the given array
     *
     * @param array
     */
    LinkedList(int[] array) {
        nPops = 0;
        nSwaps = 0;
        len = 0;
        for (int elem : array)
            add(elem);
    }

    /**
     * @return the first element of the list
     */
    public int getFirst() {
        return first.value;
    }

    /**
     * @return the second element of the list
     */
    public int getSecond() {
        return second.value;
    }

    /**
     * Swap the first and second element of the list
     */
    public void swap() {
        Node oldFirst = this.first;
        Node third = this.second.next;
        this.first = this.second;
        oldFirst.next = third;
        this.first.next = oldFirst;
        this.second = oldFirst;
        nSwaps++;
    }

    /**
     * Take the first element and put it at the end of the list
     */
    public void pop() {
        Node oldFirst = this.first;
        this.first = this.second;
        this.second = this.first.next;
        oldFirst.next = null;
        this.tail.next = oldFirst;
        this.tail = oldFirst;
        nPops++;
    }

    /**
     * Add an element to the list. You can make it public while debugging, if needed, but this will be private in
     * INGInious.
     *
     * @param v element to add
     */
    private void add(int v) {
        if (len == 0) {
            this.first = new Node(v, null);
            this.len++;
            this.tail = this.first;
        } else if (len == 1) {
            this.second = new Node(v, null);
            this.first.setNext(this.second);
            this.len++;
            this.tail = this.second;
        } else {
            Node n = new Node(v, null);
            this.tail.next = n;
            this.tail = n;
            this.len++;
        }
    }

    /**
     * @return True if the full list is sorted, False otherwise
     */
    public boolean isSorted() {
        nPops += getSize();
        Node current = this.first;
        if (current == null) return true;
        for (int i = 0; i < getSize() - 1 && current.next != null; i++) {
            if (current.getValue() > current.next.getValue()) return false;
            current = current.next;
        }
        return true;
    }

    public int getSize() {
        return len;
    }

    public String toString() {
        String s = "[";
        Node current = this.first;
        while (current != null) {
            s += current.value + " ";
            current = current.next;
        }

        s += " ]";
        return s;
    }

    public int getnSwaps() {
        return nSwaps;
    }

    public int getnPops() {
        return nPops;
    }
}