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

}
