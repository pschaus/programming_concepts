package basics;


import org.javagrader.Grade;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FourInARowTest {


    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void test1() {

        FourInARow game = new FourInARow();
        game.play(0, 'X');
        game.play(1, 'O');
        game.play(0, 'X');
        game.play(1, 'O');
        game.play(0, 'X');
        assertFalse(game.hasWon('X'));
        assertFalse(game.hasWon('O'));
        game.play(1, 'O');
        game.play(0, 'X');
        assertTrue(game.hasWon('X'));
        assertFalse(game.hasWon('O'));
    }


    @Grade(value = 1, cpuTimeout = 1000)
    public void test2() {

        FourInARow game = new FourInARow();
        game.play(0, 'X');
        game.play(1, 'O');
        game.play(0, 'X');
        game.play(1, 'O');
        game.play(0, 'X');
        game.play(1, 'O');
        game.play(0, 'X');
        game.play(1, 'O');
        game.play(0, 'X');
        game.play(1, 'O');
        game.play(0, 'X');
        game.play(1, 'O');

        assertThrows(IllegalArgumentException.class, () -> {
            game.play(0, 'X'); // row 0 is full, illegal argument exception
        });

    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void test3() { //up right  ↗ diagonal

        FourInARow game = new FourInARow();
        game.play(0, 'X');

        game.play(1, 'O');
        game.play(1, 'X');

        game.play(2, 'O');
        game.play(2, 'O');
        game.play(2, 'X');

        game.play(3, 'O');
        game.play(3, 'O');
        game.play(3, 'O');
        game.play(3, 'X');

        assertTrue(game.hasWon('X'));
        assertFalse(game.hasWon('O')); // if O won, then condition on 3 and not 4


    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void test4() { //down right  ↘ diagonal

        FourInARow game = new FourInARow();
        game.play(4, 'X');

        game.play(3, 'O');
        game.play(3, 'X');

        game.play(2, 'O');
        game.play(2, 'O');
        game.play(2, 'X');

        game.play(1, 'O');
        game.play(1, 'O');
        game.play(1, 'O');
        game.play(1, 'X');

        assertTrue(game.hasWon('X'));
        assertFalse(game.hasWon('O')); // if O won, then condition on 3 and not 4


    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void test5() {

        FourInARow game = new FourInARow();
        game.play(0, 'X');
        game.play(1, 'X');
        game.play(2, 'X');

        assertFalse(game.hasWon('X')); // if X won, then condition on 3 and not 4
        assertFalse(game.hasWon('O'));

        game.play(3, 'X');

        assertTrue(game.hasWon('X')); // if X won, then condition on 3 and not 4
        assertFalse(game.hasWon('O')); // if O won there but not previous Assert then good luck


    }


    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void test6() { //down right  ↘ diagonal

        FourInARow game = new FourInARow();
        game.play(6, 'X');
        game.play(5, 'X');
        game.play(4, 'X');
        game.play(3, 'X');



        assertTrue(game.hasWon('X'));
        assertFalse(game.hasWon('O')); // if O won, then condition on 3 and not 4


    }

}
