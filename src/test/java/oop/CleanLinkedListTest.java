package oop;

import org.javagrader.Grade;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Grade
public class CleanLinkedListTest {

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void test1() {

        CleanLinkedList input = new CleanLinkedList();
        input.add(3,3,3,4,5,5,6,6,6,7,9,9,9,9,10,10);

        List expected = Arrays.asList(3,4,5,6,7,9,10);


        List answer = new ArrayList();

        CleanLinkedList.Node n = input.clean().first;
        while (n != null) {
            answer.add(n.v);
            n = n.next;
        }

        assertEquals(expected,answer);
    }

}
