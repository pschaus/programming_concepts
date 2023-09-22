package oop;

import org.javagrader.Grade;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Grade
public class StringIteratorTest {

    @Test
    @Grade
    public void test1() {

        StringIterator.IterableString myString = StringIterator.makeIterableString("Hello World");

        String s1 = "";
        for (char c1: myString) {
            String s2 = "";
            for (char c2: myString) {
                s2 += c2;
            }
            assertEquals(s2,"Hello World");
            s1 += c1;
        }
        assertEquals(s1,"Hello World");

    }


}
