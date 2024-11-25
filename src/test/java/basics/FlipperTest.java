package quizz;

import org.javagrader.Grade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
// BEGIN STRIP
import java.util.concurrent.TimeUnit;
// END STRIP

@Grade(value = 100)
public class FlipperTest {

    @Test
    @Grade(value = 0.05, cpuTimeout = 1)
    public void test1() {
        char[][] map = new char[][]{
                {'.', '.', '.', '.', '.'},
                {'.', '.', '.', '.', '.'},
                {'.', '.', '.', '.', '.'},
                {'.', '.', '.', '.', '.'},
                {'.', '.', '.', '.', '.'},
        };

        // BEGIN STRIP
        // The beam trace is shown with * on the cells that are visited
        // '*', '*', '*', '*', '*'
        // '.', '.', '.', '.', '.'
        // '.', '.', '.', '.', '.'
        // '.', '.', '.', '.', '.'
        // '.', '.', '.', '.', '.'
        // END STRIP

        assertEquals(5, Flipper.run(map));
    }

    @Test
    @Grade(value = 0.05, cpuTimeout = 1)
    public void test2() {
        char [][] map = new char[][]{
                {'.', '.','\\', '.', '.'},
                {'.', '.', '.', '.', '.'},
                {'.', '.', '.', '.', '.'},
                {'.', '.', '.', '.', '.'},
                {'.', '.', '.', '.', '.'},
                {'.', '.','\\', '.', '.'},
                {'.', '.', '.', '.', '.'},
                {'.', '.', '.', '.', '.'},
        };
        // BEGIN STRIP
        // The beam trace is shown with * on the cells that are visited
        // '*', '*', '*', '.', '.'
        // '.', '.', '*', '.', '.'
        // '.', '.', '*', '.', '.'
        // '.', '.', '*', '.', '.'
        // '.', '.', '*', '.', '.'
        // '.', '.', '*', '*', '*'
        // '.', '.', '.', '.', '.'
        // '.', '.', '.', '.', '.'
        // END STRIP
        assertEquals(10, Flipper.run(map));
    }

    @Test
    @Grade(value = 0.1, cpuTimeout = 1)
    public void test3() {
        char [][] map = new char[][]{
                {'.', '.','\\', '.', '.'},
                {'.','\\', '/', '.', '.'},
                {'.', '.', '.', '.', '.'},
                {'.', '.', '.', '.', '.'},
                {'.', '.', '.', '.', '.'},
                {'.', '.','\\', '.', '.'},
                {'.', '.', '.', '.', '.'},
                {'.', '.', '.', '.', '.'},
        };
        // BEGIN STRIP
        // The beam trace is shown with * on the cells that are visited
        // '*', '*', '*', '.', '.'
        // '.', '*', '*', '.', '.'
        // '.', '.', '.', '.', '.'
        // '.', '.', '.', '.', '.'
        // '.', '.', '.', '.', '.'
        // '.', '.','\\', '.', '.'
        // '.', '.', '.', '.', '.'
        // '.', '.', '.', '.', '.'
        // END STRIP
        assertEquals(5, Flipper.run(map));
    }

    @Test
    @Grade(value = 0.05, cpuTimeout = 1)
    public void test4() {
        char[][] map = new char[][]{
                {'/'}
        };
        // BEGIN STRIP
        // The beam trace is shown with * on the cells that are enlighted
        // '*'
        // END STRIP
        assertEquals(1, Flipper.run(map));
    }

    @Test
    @Grade(value = 0.05, cpuTimeout = 1)
    public void test5() {
        // empty map
        char[][] map = new char[][]{
                {}
        };
        assertEquals(0, Flipper.run(map));
    }

    @Test
    @Grade(value = 0.15, cpuTimeout = 1)
    public void test6() {
        char [][] map = new char[][]{
                {'.', '.', '.', '.','\\'},
                {'/', '.', '.','\\', '.'},
                {'.', '/','\\', '.', '.'},
                {'.', '.', '.', '.', '.'},
                {'.', '.', '.', '.', '.'},
                {'.', '.', '.', '.', '.'},
                {'.','\\', '.', '/', '.'},
                {'\\', '.', '.', '.','/'},
        };
        // BEGIN STRIP
        // The beam trace is shown with * on the cells that are visited
        // '*', '*', '*', '*', '*'
        // '*', '*', '*', '*', '*'
        // '*', '*', '*', '*', '*'
        // '*', '*', '*', '*', '*'
        // '*', '*', '*', '*', '*'
        // '*', '*', '*', '*', '*'
        // '*', '*', '*', '*', '*'
        // '*', '*', '*', '*', '*'
        // END STRIP
        assertEquals(40, Flipper.run(map));
    }

    // BEGIN STRIP

    @Test
    @Grade(value = 0.15, cpuTimeout = 1)
    public void testOverlap() {
        char [][] map = new char[][]{
                {'.', '.','\\', '.', '.'},
                {'.', '.', '.', '.', '.'},
                {'.', '.', '.', '.', '.'},
                {'.', '.', '.', '.', '.'},
                {'.', '.', '.', '.', '.'},
                {'/', '.', '/', '.', '.'},
                {'.', '.', '.', '.', '.'},
                {'\\', '/','.', '.', '.'},
        };
        assertEquals(18, Flipper.run(map));
    }

    @Test
    @Grade(value = 0.15, cpuTimeout = 1)
    public void testAll() {
        char [][] map = new char[][]{
                {'.', '.', '.', '.', '\\'},
                {'/', '.', '.', '.', '/'},
                {'\\', '.', '.', '.', '\\'},
                {'/', '.', '.', '.', '/'},
                {'\\', '.', '.', '.', '\\'},
                {'/', '.', '.', '.', '/'},
                {'\\', '.', '.', '.', '\\'},
                {'.', '.', '.', '.', '/'},
        };
        assertEquals(40, Flipper.run(map));
    }

    private char[][] largeInstance() {
        int size = 1000;
        char [][] map = new char[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                map[i][j] = '.';
            }
        }
        map[0][20] = '\\';
        map[10][20] = '/';
        map[10][10] = '\\';
        return map;
    }

    private final char[][] largeMap = largeInstance();

    @Test
    //@Grade(value = 0.25, cpuTimeout = 1000, unit = TimeUnit.MILLISECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
    @Grade(value = 0.25, cpuTimeout = 10)  // More tolerance on cpuTimeout, as this led to undeterministic results during the quiz session on 2024-11-07
    public void testLargeMap() {
        assertEquals(50, Flipper.run(largeMap));
    }

    // END STRIP
}
