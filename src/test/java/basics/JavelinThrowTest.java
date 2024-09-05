package basics;

import org.javagrader.Grade;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@Grade
public class JavelinThrowTest {
    /*
        A small field:
            ....xxx
            ..xx...
            xx.....
            .......
            xx.....
            ..xx...
            ....xxx
     */
    private static final char[][] emptyField = {
            { '.', '.', '.', '.', 'x', 'x', 'x'},
            { '.', '.', 'x', 'x', '.', '.', '.'},
            { 'x', 'x', '.', '.', '.', '.', '.'},
            { '.', '.', '.', '.', '.', '.', '.'},
            { 'x', 'x', '.', '.', '.', '.', '.'},
            { '.', '.', 'x', 'x', '.', '.', '.'},
            { '.', '.', '.', '.', 'x', 'x', 'x'},
    };

    // A larger field
    private static final char[][] emptyLargeField = {
            { '.', '.', '.', '.', '.', '.', '.', '.', 'x', 'x'},
            { '.', '.', '.', '.', 'x', 'x', 'x', 'x', '.', '.'},
            { '.', '.', '.', 'x', '.', '.', '.', '.', '.', '.'},
            { 'x', 'x', 'x', '.', '.', '.', '.', '.', '.', '.'},
            { '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'},
            { 'x', 'x', 'x', '.', '.', '.', '.', '.', '.', '.'},
            { '.', '.', '.', 'x', '.', '.', '.', '.', '.', '.'},
            { '.', '.', '.', '.', 'x', 'x', 'x', 'x', '.', '.'},
            { '.', '.', '.', '.', '.', '.', '.', '.', 'x', 'x'},
    };

    private char[][] makeCopyOfArray(char[][] a) {
        return Arrays.stream(a).map(char[]::clone).toArray(char[][]::new);
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1)
    public void testFindJavelin1() {
        // make a copy of the empty field
        char[][] field = makeCopyOfArray(emptyField);

        // put the javelin in the field
        field[2][5] = '#';

        // the field now looks like this:
        //   ....xxx
        //   ..xx...
        //   xx...#.     // the javelin is at x=5, y=2
        //   .......
        //   xx.....
        //   ..xx...
        //   ....xxx

        JavelinThrow.JavelinPosition jp = JavelinThrow.findJavelin(field);
        assertTrue(jp.x==5 && jp.y==2);
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1)
    public void testFindJavelin2() {
        // make a copy of the empty field
        char[][] field = makeCopyOfArray(emptyField);

        // put the javelin in the field
        field[5][0] = '#';

        // the field now looks like this:
        //   ....xxx
        //   ..xx...
        //   xx.....
        //   .......
        //   xx.....
        //   #.xx...     // the javelin is at x=0, y=5
        //   ....xxx

        JavelinThrow.JavelinPosition jp = JavelinThrow.findJavelin(field);
        assertTrue(jp.x==0 && jp.y==5);
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1)
    public void testFindJavelin3() {
        // make a copy of the empty field
        char[][] field = makeCopyOfArray(emptyField);

        // put the javelin in the field
        field[0][5] = '#';

        // the field now looks like this:
        //   ....x#x      // the javelin is at x=5, y=0
        //   ..xx...
        //   xx.....
        //   .......
        //   xx.....
        //   ..xx...
        //   ....xxx

        JavelinThrow.JavelinPosition jp = JavelinThrow.findJavelin(field);
        assertTrue(jp.x==5 && jp.y==0);
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1)
    public void testValidThrow() {
        // This is the same field as in the test testFindJavelin1.
        // The throw is valid because the javelin landed inside
        // the boundaries.
        char[][] field = makeCopyOfArray(emptyField);
        field[2][5] = '#';

        assertTrue(JavelinThrow.isThrowValid(field));
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1)
    public void testInvalidThrow() {
        // This is the same field as in the test testFindJavelin2.
        // The throw is invalid because the javelin landed outside
        // the boundary.
        char[][] field = makeCopyOfArray(emptyField);
        field[5][0] = '#';

        assertFalse(JavelinThrow.isThrowValid(field));
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1)
    public void testInvalidThrowOnBoundary() {
        // This is the same field as in the test testFindJavelin3.
        // The throw is invalid because the javelin landed on
        // the boundaries.
        char[][] field = makeCopyOfArray(emptyField);
        field[0][5] = '#';

        assertFalse(JavelinThrow.isThrowValid(field));
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1)
    public void testValidThrowInLargeField() {
        char[][] field = makeCopyOfArray(emptyLargeField);
        field[1][9] = '#';
        assertTrue(JavelinThrow.isThrowValid(field));
    }

    // Don't forget to make your own tests!!!


    // BEGIN STRIP
    // Internal tests

    private static final char[][] emptyFieldValid = {
            { 'x', 'x', 'x', 'x', 'x', 'x', 'x'},
            { 'x', 'x', 'x', 'x', '.', '.', '.'},
            { 'x', 'x', '.', '.', '.', '.', '.'},
            { '.', '.', '.', '.', '.', '.', '.'},
            { 'x', 'x', '.', '.', '.', '.', '.'},
            { 'x', 'x', 'x', 'x', '.', '.', '.'},
            { 'x', 'x', 'x', 'x', 'x', 'x', 'x'},
    };

    @Test
    @Grade(value = 1, cpuTimeout = 1)
    public void testFindJavelinInEntireField() {
        for (int y=0; y < emptyField.length; y++) {
            for (int x = 0; x< emptyField[0].length; x++) {
                char[][] field = makeCopyOfArray(emptyField);
                field[y][x] = '#';
                JavelinThrow.JavelinPosition jp = JavelinThrow.findJavelin(field);
                assertTrue(jp.x==x && jp.y==y);
            }
        }
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1)
    public void testEntireField() {
        for (int y = 0; y < emptyField.length; y++) {
            for (int x = 0; x < emptyField[0].length; x++) {
                char[][] field = makeCopyOfArray(emptyField);
                field[y][x] = '#';
                assertEquals(JavelinThrow.isThrowValid(field), emptyFieldValid[y][x] != 'x');
            }
        }
    }

    private static final char[][] emptyLargeFieldValid = {
            { 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x'},
            { 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', '.', '.'},
            { 'x', 'x', 'x', 'x', '.', '.', '.', '.', '.', '.'},
            { 'x', 'x', 'x', '.', '.', '.', '.', '.', '.', '.'},
            { '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'},
            { 'x', 'x', 'x', '.', '.', '.', '.', '.', '.', '.'},
            { 'x', 'x', 'x', 'x', '.', '.', '.', '.', '.', '.'},
            { 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', '.', '.'},
            { 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x'},
    };

    @Test
    @Grade(value = 1, cpuTimeout = 1)
    public void testEntireLargeField() {
        for (int y = 0; y < emptyLargeField.length; y++) {
            for (int x = 0; x < emptyLargeField[0].length; x++) {
                char[][] field = makeCopyOfArray(emptyLargeField);
                field[y][x] = '#';
                assertEquals(JavelinThrow.isThrowValid(field), emptyLargeFieldValid[y][x] != 'x');
            }
        }
    }

    // END STRIP
}
