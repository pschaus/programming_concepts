package oop;

import oop.Pentago;
import org.javagrader.Grade;
import org.javagrader.GradeFeedback;
import org.javagrader.TestResultStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Grade
public class PentagoTest {
    @Test
    @Grade(value = 1, cpuTimeout = 3)
    @GradeFeedback(message = "Congrats!", on= TestResultStatus.SUCCESS)
    @GradeFeedback(message = "Something is wrong in the rotation of a square matrix", on=TestResultStatus.FAIL)
    @GradeFeedback(message = "Too slow, infinite loop ?", on=TestResultStatus.TIMEOUT)
    public void testRotateSquare(){
        Pentago game = new Pentago();
        Pentago.Player[][] matrix = {{Pentago.Player.A, Pentago.Player.A, Pentago.Player.B, null},
                {null, Pentago.Player.B, Pentago.Player.A, Pentago.Player.A},
                {null, Pentago.Player.B, null, null},
                {Pentago.Player.A, Pentago.Player.B, Pentago.Player.A, Pentago.Player.B}};
        Pentago.Player[][] rotated = game.rotateMatrix(matrix);
        Pentago.Player[][] expected = {{Pentago.Player.A, null, null, Pentago.Player.A},
                {Pentago.Player.B, Pentago.Player.B, Pentago.Player.B, Pentago.Player.A},
                {Pentago.Player.A, null, Pentago.Player.A, Pentago.Player.B},
                {Pentago.Player.B, null, Pentago.Player.A, null}};
        for(int i=0; i<expected.length; i++)
            assertArrayEquals(expected[i], rotated[i]);
    }


    // BEGIN STRIP
    @Test
    @Grade(value = 2, cpuTimeout = 3)
    @GradeFeedback(message = "Congrats!", on= TestResultStatus.SUCCESS)
    @GradeFeedback(message = "Something is wrong in the rotation of a non-square matrix", on=TestResultStatus.FAIL)
    @GradeFeedback(message = "Too slow, infinite loop ?", on=TestResultStatus.TIMEOUT)
    public void testRotateRectangular(){
        Pentago game = new Pentago();
        Pentago.Player[][] matrix = {{Pentago.Player.A, Pentago.Player.A, Pentago.Player.B},
                                    {null, Pentago.Player.B, Pentago.Player.A}};
        Pentago.Player[][] rotated = game.rotateMatrix(matrix);
        Pentago.Player[][] expected = {{null, Pentago.Player.A},{Pentago.Player.B, Pentago.Player.A},
                                        {Pentago.Player.A, Pentago.Player.B}};
        for(int i=0; i<expected.length; i++)
            assertArrayEquals(expected[i], rotated[i]);
    }

    @Test
    @Grade(value = 2, cpuTimeout = 3)
    @GradeFeedback(message = "Congrats!", on= TestResultStatus.SUCCESS)
    @GradeFeedback(message = "Something is wrong when rotating three times a matrix", on=TestResultStatus.FAIL)
    @GradeFeedback(message = "Too slow, infinite loop ?", on=TestResultStatus.TIMEOUT)
    public void testRotate3Times(){
        Pentago game = new Pentago();
        Pentago.Player[][] matrix = {{Pentago.Player.A, Pentago.Player.B, Pentago.Player.B, Pentago.Player.A, null},
                                    {null, Pentago.Player.B, null, null, Pentago.Player.A}};
        Pentago.Player[][] rotated = game.rotateMatrix(game.rotateMatrix(game.rotateMatrix(matrix)));
        Pentago.Player[][] expected = {{null, Pentago.Player.A},
                                    {Pentago.Player.A, null},
                                    {Pentago.Player.B, null},
                                    {Pentago.Player.B, Pentago.Player.B},
                                    {Pentago.Player.A, null}};
        for(int i=0; i<expected.length; i++)
            assertArrayEquals(expected[i], rotated[i]);
    }
    // END STRIP

    @Test
    @Grade(value = 3, cpuTimeout = 3)
    @GradeFeedback(message = "Congrats!", on= TestResultStatus.SUCCESS)
    @GradeFeedback(message = "Something is wrong in the detection of a winner with checkWinPlayer method",
                    on=TestResultStatus.FAIL)
    @GradeFeedback(message = "Too slow, infinite loop ?", on=TestResultStatus.TIMEOUT)
    public void testWinVector(){
        Pentago game = new Pentago();

        Pentago.Player[] vector;

        // size six with all empty positions
        vector = new Pentago.Player[]{null, null, null, null, null, null};
        assertFalse(game.checkWinPlayer(vector, Pentago.Player.B), "No player should have won in an empty row");
        assertFalse(game.checkWinPlayer(vector, Pentago.Player.A), "No player should have won in an empty row");

        // size six
        vector = new Pentago.Player[]{Pentago.Player.B, Pentago.Player.B, Pentago.Player.B, Pentago.Player.B, Pentago.Player.A, Pentago.Player.A};
        assertFalse(game.checkWinPlayer(vector, Pentago.Player.B), "This player should not be winning");
        assertFalse(game.checkWinPlayer(vector, Pentago.Player.A), "This player should not be winning");

        // size six
        vector = new Pentago.Player[]{Pentago.Player.B, Pentago.Player.B, Pentago.Player.B, Pentago.Player.B, Pentago.Player.B, Pentago.Player.A};
        assertTrue(game.checkWinPlayer(vector, Pentago.Player.B), "This player should be winning");
        assertFalse(game.checkWinPlayer(vector, Pentago.Player.A), "This player should not be winning");

        // size five
        vector = new Pentago.Player[]{Pentago.Player.B, Pentago.Player.B, Pentago.Player.B, Pentago.Player.B, Pentago.Player.B};
        assertTrue(game.checkWinPlayer(vector, Pentago.Player.B), "This player should be winning");
        assertFalse(game.checkWinPlayer(vector, Pentago.Player.A), "This player should not be winning");

        // size four
        vector = new Pentago.Player[]{Pentago.Player.B, Pentago.Player.B, Pentago.Player.B, Pentago.Player.B};
        assertFalse(game.checkWinPlayer(vector, Pentago.Player.B), "This player should be winning");
        assertFalse(game.checkWinPlayer(vector, Pentago.Player.A), "This player should not be winning");

        vector = new Pentago.Player[]{Pentago.Player.B, Pentago.Player.A, Pentago.Player.A, Pentago.Player.A, Pentago.Player.A, Pentago.Player.A};
        assertFalse(game.checkWinPlayer(vector, Pentago.Player.B), "This player should not be winning");
        assertTrue(game.checkWinPlayer(vector, Pentago.Player.A), "This player should be winning");

        vector = new Pentago.Player[]{Pentago.Player.A, Pentago.Player.A, Pentago.Player.A, null, Pentago.Player.A, Pentago.Player.A};
        assertFalse(game.checkWinPlayer(vector, Pentago.Player.B), "Hint: Winning sequence must be contiguous (no null)");
        assertFalse(game.checkWinPlayer(vector, Pentago.Player.A), "Hint: Winning sequence must be contiguous (no null)");
    }

    // Test to check that an exception is thrown when trying to play on an already used position
    @Test
    @Grade(value = 1, cpuTimeout = 10)
    @GradeFeedback(message = "Congrats!", on= TestResultStatus.SUCCESS)
    @GradeFeedback(message = "Something is wrong when trying to play on an already used position", on=TestResultStatus.FAIL)
    @GradeFeedback(message = "Too slow, infinite loop ?", on=TestResultStatus.TIMEOUT)
    public void testExceptionNonEmptyCell(){
        Pentago game = new Pentago();
        game.play(2, 4, Pentago.Player.B, Pentago.BoardSubpart.TOP_RIGHT, Pentago.RotationDirection.LEFT);
        game.play(5, 0, Pentago.Player.A, Pentago.BoardSubpart.BOTTOM_LEFT, Pentago.RotationDirection.RIGHT);
        assertThrows(IllegalArgumentException.class, () -> {
            game.play(1, 5, Pentago.Player.B, Pentago.BoardSubpart.TOP_LEFT, Pentago.RotationDirection.LEFT);
        }, "The first move of player B followed by the rotation is already using the position 1,5 in the bottom LEFT subpart");
    }

    // Simple test to check that the right winner is found and that it is not found too soon
    @Test
    @Grade(value = 1, cpuTimeout = 10)
    @GradeFeedback(message = "Congrats!", on= TestResultStatus.SUCCESS)
    @GradeFeedback(message = "Something is wrong when the rotations do not change the board's state (i.e. rotating an empty subpart)", on=TestResultStatus.FAIL)
    @GradeFeedback(message = "Too slow, infinite loop ?", on=TestResultStatus.TIMEOUT)
    public void testPlay1(){
        Pentago game = new Pentago();
        Pentago.Winner winner;
        winner = game.play(0, 0, Pentago.Player.A, Pentago.BoardSubpart.BOTTOM_LEFT, Pentago.RotationDirection.LEFT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(5, 5, Pentago.Player.B, Pentago.BoardSubpart.TOP_RIGHT, Pentago.RotationDirection.RIGHT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(1, 0, Pentago.Player.A, Pentago.BoardSubpart.BOTTOM_RIGHT, Pentago.RotationDirection.LEFT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(3, 3, Pentago.Player.B, Pentago.BoardSubpart.TOP_RIGHT, Pentago.RotationDirection.LEFT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(2, 0, Pentago.Player.A, Pentago.BoardSubpart.BOTTOM_LEFT, Pentago.RotationDirection.RIGHT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(2, 2, Pentago.Player.B, Pentago.BoardSubpart.TOP_LEFT, Pentago.RotationDirection.RIGHT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(0, 3, Pentago.Player.A, Pentago.BoardSubpart.BOTTOM_RIGHT, Pentago.RotationDirection.LEFT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(4, 3, Pentago.Player.B, Pentago.BoardSubpart.BOTTOM_LEFT, Pentago.RotationDirection.RIGHT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(0, 4, Pentago.Player.A, Pentago.BoardSubpart.BOTTOM_LEFT, Pentago.RotationDirection.LEFT);
        assertEquals(Pentago.Winner.A_WINS, winner, "Player A should have won");
    }

    @Test
    @Grade(value = 0.4, cpuTimeout = 3)
    @GradeFeedback(message = "Congrats!", on= TestResultStatus.SUCCESS)
    @GradeFeedback(message = "Something is wrong in the finding of a diagonal win", on=TestResultStatus.FAIL)
    @GradeFeedback(message = "Too slow, infinite loop ?", on=TestResultStatus.TIMEOUT)
    public void testSimpleDiagonal(){
        /*
         * This is the state of the game after the different moves of the test.
         * The player B wins because he has 5 tokens in a diagonal.
         * ------------------------------
         * |  x   B   x  |  x   x   x   |
         * |  x   x   B  |  x   x   x   |
         * |  x   x   x  |  B   x   x   |
         * ------------------------------
         * |  x   x   x  |  x   B   x   |
         * |  x   x   x  |  x   x   B   |
         * |  x   x   x  |  x   x   x   |
         * ------------------------------
         */
        Pentago game = new Pentago();
        Pentago.Winner winner;
        winner = game.play(0, 1, Pentago.Player.B, Pentago.BoardSubpart.BOTTOM_RIGHT, Pentago.RotationDirection.LEFT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(1, 2, Pentago.Player.B, Pentago.BoardSubpart.BOTTOM_LEFT, Pentago.RotationDirection.RIGHT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(2, 3, Pentago.Player.B, Pentago.BoardSubpart.BOTTOM_RIGHT, Pentago.RotationDirection.LEFT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(3, 4, Pentago.Player.B, Pentago.BoardSubpart.BOTTOM_LEFT, Pentago.RotationDirection.LEFT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(4, 5, Pentago.Player.B, Pentago.BoardSubpart.BOTTOM_LEFT, Pentago.RotationDirection.RIGHT);
        assertEquals(Pentago.Winner.B_WINS, winner, "B should have won");
    }

    // BEGIN STRIP
    // Test to check that an exception is thrown when trying to play on a position that is not on the board
    @Test
    @Grade(value = 1, cpuTimeout = 10)
    @GradeFeedback(message = "Congrats!", on= TestResultStatus.SUCCESS)
    @GradeFeedback(message = "You do not launch an exception when a player tries to play out of the board", on=TestResultStatus.FAIL)
    @GradeFeedback(message = "Too slow, infinite loop ?", on=TestResultStatus.TIMEOUT)
    public void testExceptionOutOfBoard(){
        Pentago game = new Pentago();
        game.play(2, 4, Pentago.Player.B, Pentago.BoardSubpart.TOP_RIGHT, Pentago.RotationDirection.LEFT);
        game.play(5, 0, Pentago.Player.A, Pentago.BoardSubpart.BOTTOM_LEFT, Pentago.RotationDirection.RIGHT);
        assertThrows(IllegalArgumentException.class, () -> {
            game.play(6, 2, Pentago.Player.B, Pentago.BoardSubpart.TOP_LEFT, Pentago.RotationDirection.RIGHT);
        }, "The position 6,2 should not be valid");
    }

    // Test to check that no winner is found when we have 5 pieces on one line, but they are not adjacent to each other
    @Test
    @Grade(value = 1, cpuTimeout = 10)
    @GradeFeedback(message = "Congrats!", on= TestResultStatus.SUCCESS)
    @GradeFeedback(message = "Something is wrong in the winning detection, the pieces have to be adjacent", on=TestResultStatus.FAIL)
    @GradeFeedback(message = "Too slow, infinite loop ?", on=TestResultStatus.TIMEOUT)
    public void testPlay4(){
        Pentago game = new Pentago();
        Pentago.Winner winner;
        winner = game.play(0,1, Pentago.Player.A, Pentago.BoardSubpart.TOP_RIGHT, Pentago.RotationDirection.LEFT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(1, 4, Pentago.Player.B, Pentago.BoardSubpart.TOP_RIGHT, Pentago.RotationDirection.LEFT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(2,1, Pentago.Player.A, Pentago.BoardSubpart.TOP_RIGHT, Pentago.RotationDirection.RIGHT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(1,3, Pentago.Player.B, Pentago.BoardSubpart.TOP_RIGHT, Pentago.RotationDirection.LEFT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(3, 1, Pentago.Player.A, Pentago.BoardSubpart.TOP_RIGHT, Pentago.RotationDirection.RIGHT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(2,3, Pentago.Player.B, Pentago.BoardSubpart.TOP_RIGHT, Pentago.RotationDirection.RIGHT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(4,1, Pentago.Player.A, Pentago.BoardSubpart.BOTTOM_RIGHT, Pentago.RotationDirection.LEFT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(4,4, Pentago.Player.B, Pentago.BoardSubpart.BOTTOM_RIGHT, Pentago.RotationDirection.LEFT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(5,1, Pentago.Player.A, Pentago.BoardSubpart.BOTTOM_RIGHT, Pentago.RotationDirection.RIGHT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "Player A should not win as it has 5 token in the same column but these are not all next to each other");
    }

    // Test to check that a DRAW is found when both players win at the same time
    @Test
    @Grade(value = 2, cpuTimeout = 10)
    @GradeFeedback(message = "Congrats!", on= TestResultStatus.SUCCESS)
    @GradeFeedback(message = "Something is wrong in the NO_WINNER detection when both players win at the same time", on=TestResultStatus.FAIL)
    @GradeFeedback(message = "Too slow, infinite loop ?", on=TestResultStatus.TIMEOUT)
    public void testPlay5(){
        Pentago game = new Pentago();
        Pentago.Winner winner;
        winner = game.play(1,0, Pentago.Player.A, Pentago.BoardSubpart.BOTTOM_LEFT, Pentago.RotationDirection.RIGHT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(4, 1, Pentago.Player.B, Pentago.BoardSubpart.BOTTOM_LEFT, Pentago.RotationDirection.RIGHT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(0,1, Pentago.Player.A, Pentago.BoardSubpart.TOP_RIGHT, Pentago.RotationDirection.LEFT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(3, 1, Pentago.Player.B, Pentago.BoardSubpart.BOTTOM_RIGHT, Pentago.RotationDirection.RIGHT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(2,3, Pentago.Player.A, Pentago.BoardSubpart.BOTTOM_RIGHT, Pentago.RotationDirection.RIGHT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(5, 1, Pentago.Player.B, Pentago.BoardSubpart.TOP_RIGHT, Pentago.RotationDirection.LEFT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(3,4, Pentago.Player.A, Pentago.BoardSubpart.TOP_RIGHT, Pentago.RotationDirection.LEFT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(1, 2, Pentago.Player.B, Pentago.BoardSubpart.TOP_RIGHT, Pentago.RotationDirection.RIGHT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(4,5, Pentago.Player.A, Pentago.BoardSubpart.TOP_RIGHT, Pentago.RotationDirection.RIGHT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(1, 1, Pentago.Player.B, Pentago.BoardSubpart.TOP_LEFT, Pentago.RotationDirection.RIGHT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "The result of this game should be a NO_WINNER");
    }

    // Test to check that the winner is found if the winning pieces are on the second main diagonal
    @Test
    @Grade(value = 1, cpuTimeout = 10)    
    @GradeFeedback(message = "Congrats!", on= TestResultStatus.SUCCESS)
    @GradeFeedback(message = "Something is wrong in the victory detection of player A when winning on a diagonal", on=TestResultStatus.FAIL)
    @GradeFeedback(message = "Too slow, infinite loop ?", on=TestResultStatus.TIMEOUT)
    public void testPlay6(){
        Pentago game = new Pentago();
        Pentago.Winner winner;
        winner = game.play(5,0, Pentago.Player.A, Pentago.BoardSubpart.TOP_RIGHT, Pentago.RotationDirection.LEFT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(0, 1, Pentago.Player.B, Pentago.BoardSubpart.TOP_RIGHT, Pentago.RotationDirection.RIGHT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(1,4, Pentago.Player.A, Pentago.BoardSubpart.TOP_LEFT, Pentago.RotationDirection.RIGHT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(2, 0, Pentago.Player.B, Pentago.BoardSubpart.BOTTOM_RIGHT, Pentago.RotationDirection.RIGHT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(3,2, Pentago.Player.A, Pentago.BoardSubpart.BOTTOM_RIGHT, Pentago.RotationDirection.RIGHT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(0, 1, Pentago.Player.B, Pentago.BoardSubpart.TOP_LEFT, Pentago.RotationDirection.LEFT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(4,1, Pentago.Player.A, Pentago.BoardSubpart.TOP_LEFT, Pentago.RotationDirection.LEFT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(1, 1, Pentago.Player.B, Pentago.BoardSubpart.BOTTOM_RIGHT, Pentago.RotationDirection.LEFT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(2,5, Pentago.Player.A, Pentago.BoardSubpart.TOP_RIGHT, Pentago.RotationDirection.RIGHT);
        assertEquals(Pentago.Winner.A_WINS, winner, "A should have won");

    }

    // Test to check that the winner is found if the winning pieces are on the upper diagonal
    @Test
    @Grade(value = 1, cpuTimeout = 10)
    @GradeFeedback(message = "Congrats!", on= TestResultStatus.SUCCESS)
    @GradeFeedback(message = "Something is wrong in the victory detection of player B when winning on a diagonal", on=TestResultStatus.FAIL)
    @GradeFeedback(message = "Too slow, infinite loop ?", on=TestResultStatus.TIMEOUT)
    public void testPlay7(){
        Pentago game = new Pentago();
        Pentago.Winner winner;
        winner = game.play(4,1, Pentago.Player.A, Pentago.BoardSubpart.BOTTOM_LEFT, Pentago.RotationDirection.RIGHT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(4,5, Pentago.Player.B, Pentago.BoardSubpart.TOP_LEFT, Pentago.RotationDirection.LEFT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(5,0, Pentago.Player.A, Pentago.BoardSubpart.BOTTOM_RIGHT, Pentago.RotationDirection.LEFT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(4, 5, Pentago.Player.B, Pentago.BoardSubpart.BOTTOM_LEFT, Pentago.RotationDirection.LEFT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(5,0, Pentago.Player.A, Pentago.BoardSubpart.BOTTOM_LEFT, Pentago.RotationDirection.LEFT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(0,1, Pentago.Player.B, Pentago.BoardSubpart.BOTTOM_LEFT, Pentago.RotationDirection.LEFT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(5,1, Pentago.Player.A, Pentago.BoardSubpart.BOTTOM_LEFT, Pentago.RotationDirection.RIGHT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(1, 0, Pentago.Player.B, Pentago.BoardSubpart.BOTTOM_LEFT, Pentago.RotationDirection.LEFT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(3,1, Pentago.Player.A, Pentago.BoardSubpart.BOTTOM_LEFT, Pentago.RotationDirection.LEFT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(2,3, Pentago.Player.B, Pentago.BoardSubpart.TOP_LEFT, Pentago.RotationDirection.RIGHT);
        assertEquals(Pentago.Winner.B_WINS, winner, "B should have won");
    }

    // Test to check that no one wins if the five pieces in a row are not all from the same player
    @Test
    @Grade(value = 1, cpuTimeout = 10)
    @GradeFeedback(message = "Congrats!", on= TestResultStatus.SUCCESS)
    @GradeFeedback(message = "Something is wrong, when nobody is winning, NO_WINNER should be returned", on=TestResultStatus.FAIL)
    @GradeFeedback(message = "Too slow, infinite loop ?", on=TestResultStatus.TIMEOUT)
    public void testPlay8(){
        Pentago game = new Pentago();
        Pentago.Winner winner;
        winner = game.play(2,1, Pentago.Player.A, Pentago.BoardSubpart.TOP_LEFT, Pentago.RotationDirection.RIGHT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(1,2, Pentago.Player.B, Pentago.BoardSubpart.BOTTOM_RIGHT, Pentago.RotationDirection.LEFT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(1, 3, Pentago.Player.A, Pentago.BoardSubpart.TOP_RIGHT, Pentago.RotationDirection.RIGHT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(2,2, Pentago.Player.B, Pentago.BoardSubpart.TOP_RIGHT, Pentago.RotationDirection.LEFT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(1,1, Pentago.Player.A, Pentago.BoardSubpart.BOTTOM_LEFT, Pentago.RotationDirection.LEFT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(3,2, Pentago.Player.B, Pentago.BoardSubpart.BOTTOM_RIGHT, Pentago.RotationDirection.RIGHT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(1,4, Pentago.Player.A, Pentago.BoardSubpart.BOTTOM_LEFT, Pentago.RotationDirection.LEFT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet as there are 5 pieces in a row but not from the same player");
    }

    // Test to check that a win that is due to a rotation is correctly found (to test the rotate part)
    @Test
    @Grade(value = 2, cpuTimeout = 10)
    @GradeFeedback(message = "Congrats!", on= TestResultStatus.SUCCESS)
    @GradeFeedback(message = "Something is wrong with victory detection of player B when winning after a rotation, try to debug debug locally", on=TestResultStatus.FAIL)
    @GradeFeedback(message = "Too slow, infinite loop ?", on=TestResultStatus.TIMEOUT)
    public void testPlay9(){
        Pentago game = new Pentago();
        Pentago.Winner winner;
        winner = game.play(1,4, Pentago.Player.B, Pentago.BoardSubpart.BOTTOM_RIGHT, Pentago.RotationDirection.LEFT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(1,5, Pentago.Player.A, Pentago.BoardSubpart.TOP_LEFT, Pentago.RotationDirection.RIGHT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(1,1, Pentago.Player.B, Pentago.BoardSubpart.BOTTOM_RIGHT, Pentago.RotationDirection.RIGHT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(0,4, Pentago.Player.A, Pentago.BoardSubpart.BOTTOM_LEFT, Pentago.RotationDirection.LEFT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(1,2, Pentago.Player.B, Pentago.BoardSubpart.BOTTOM_RIGHT, Pentago.RotationDirection.LEFT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(3,3, Pentago.Player.A, Pentago.BoardSubpart.BOTTOM_LEFT, Pentago.RotationDirection.RIGHT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(1,0, Pentago.Player.B, Pentago.BoardSubpart.BOTTOM_LEFT, Pentago.RotationDirection.LEFT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(1,3, Pentago.Player.A, Pentago.BoardSubpart.TOP_LEFT, Pentago.RotationDirection.RIGHT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(4,1, Pentago.Player.B, Pentago.BoardSubpart.BOTTOM_LEFT, Pentago.RotationDirection.RIGHT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(3,1, Pentago.Player.A, Pentago.BoardSubpart.TOP_LEFT, Pentago.RotationDirection.LEFT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(2,4, Pentago.Player.B, Pentago.BoardSubpart.TOP_RIGHT, Pentago.RotationDirection.RIGHT);
        assertEquals(Pentago.Winner.B_WINS, winner, "The player B should have won.");
    }

    // Test to check that a DRAW is returned when the board is full
    @Test
    @Grade(value = 1, cpuTimeout = 10)
    @GradeFeedback(message = "Congrats!", on= TestResultStatus.SUCCESS)
    @GradeFeedback(message = "Something is wrong with NO_WINNER detection when the board is full, try to debug debug locally", on=TestResultStatus.FAIL)
    @GradeFeedback(message = "Too slow, infinite loop ?", on=TestResultStatus.TIMEOUT)
    public void testPlay10(){
        Pentago game = new Pentago();
        Pentago.Winner winner;
        winner = game.play(0,0, Pentago.Player.A, Pentago.BoardSubpart.BOTTOM_RIGHT, Pentago.RotationDirection.LEFT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(2,1, Pentago.Player.B, Pentago.BoardSubpart.BOTTOM_RIGHT, Pentago.RotationDirection.LEFT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(0,2, Pentago.Player.A, Pentago.BoardSubpart.BOTTOM_RIGHT, Pentago.RotationDirection.LEFT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(1,2, Pentago.Player.B, Pentago.BoardSubpart.BOTTOM_RIGHT, Pentago.RotationDirection.RIGHT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(0,4, Pentago.Player.A, Pentago.BoardSubpart.TOP_LEFT, Pentago.RotationDirection.RIGHT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(0,5, Pentago.Player.B, Pentago.BoardSubpart.TOP_LEFT, Pentago.RotationDirection.LEFT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");

        winner = game.play(4,1, Pentago.Player.A, Pentago.BoardSubpart.BOTTOM_LEFT, Pentago.RotationDirection.RIGHT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(1,0, Pentago.Player.B, Pentago.BoardSubpart.BOTTOM_RIGHT, Pentago.RotationDirection.LEFT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(1,3, Pentago.Player.A, Pentago.BoardSubpart.BOTTOM_LEFT, Pentago.RotationDirection.LEFT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(0,3, Pentago.Player.B, Pentago.BoardSubpart.BOTTOM_LEFT, Pentago.RotationDirection.RIGHT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(1,5, Pentago.Player.A, Pentago.BoardSubpart.BOTTOM_RIGHT, Pentago.RotationDirection.LEFT);//RIGHT?
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(4,4, Pentago.Player.B, Pentago.BoardSubpart.BOTTOM_LEFT, Pentago.RotationDirection.LEFT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");

        winner = game.play(2,0, Pentago.Player.A, Pentago.BoardSubpart.TOP_RIGHT, Pentago.RotationDirection.LEFT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(0,1, Pentago.Player.B, Pentago.BoardSubpart.BOTTOM_LEFT, Pentago.RotationDirection.RIGHT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(2,2, Pentago.Player.A, Pentago.BoardSubpart.TOP_RIGHT, Pentago.RotationDirection.RIGHT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(2,3, Pentago.Player.B, Pentago.BoardSubpart.BOTTOM_RIGHT, Pentago.RotationDirection.LEFT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(2,4, Pentago.Player.A, Pentago.BoardSubpart.BOTTOM_LEFT, Pentago.RotationDirection.LEFT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(2,5, Pentago.Player.B, Pentago.BoardSubpart.TOP_LEFT, Pentago.RotationDirection.RIGHT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");

        winner = game.play(3,4, Pentago.Player.A, Pentago.BoardSubpart.TOP_RIGHT, Pentago.RotationDirection.LEFT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(3,1, Pentago.Player.B, Pentago.BoardSubpart.BOTTOM_RIGHT, Pentago.RotationDirection.RIGHT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(3,2, Pentago.Player.A, Pentago.BoardSubpart.BOTTOM_RIGHT, Pentago.RotationDirection.LEFT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(3,3, Pentago.Player.B, Pentago.BoardSubpart.TOP_RIGHT, Pentago.RotationDirection.RIGHT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(3,0, Pentago.Player.A, Pentago.BoardSubpart.TOP_LEFT, Pentago.RotationDirection.LEFT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(3,5, Pentago.Player.B, Pentago.BoardSubpart.TOP_LEFT, Pentago.RotationDirection.RIGHT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");

        winner = game.play(4,5, Pentago.Player.A, Pentago.BoardSubpart.TOP_LEFT, Pentago.RotationDirection.RIGHT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(4,0, Pentago.Player.B, Pentago.BoardSubpart.TOP_LEFT, Pentago.RotationDirection.RIGHT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(1,1, Pentago.Player.A, Pentago.BoardSubpart.TOP_LEFT, Pentago.RotationDirection.LEFT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(4,2, Pentago.Player.B, Pentago.BoardSubpart.TOP_RIGHT, Pentago.RotationDirection.LEFT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(4,3, Pentago.Player.A, Pentago.BoardSubpart.TOP_LEFT, Pentago.RotationDirection.RIGHT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(1,4, Pentago.Player.B, Pentago.BoardSubpart.TOP_RIGHT, Pentago.RotationDirection.RIGHT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");

        winner = game.play(5,4, Pentago.Player.A, Pentago.BoardSubpart.TOP_RIGHT, Pentago.RotationDirection.LEFT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(5,1, Pentago.Player.B, Pentago.BoardSubpart.TOP_LEFT, Pentago.RotationDirection.LEFT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(5,0, Pentago.Player.A, Pentago.BoardSubpart.TOP_RIGHT, Pentago.RotationDirection.RIGHT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(5,3, Pentago.Player.B, Pentago.BoardSubpart.TOP_LEFT, Pentago.RotationDirection.RIGHT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(5,2, Pentago.Player.A, Pentago.BoardSubpart.BOTTOM_LEFT, Pentago.RotationDirection.LEFT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(5,5, Pentago.Player.B, Pentago.BoardSubpart.BOTTOM_LEFT, Pentago.RotationDirection.RIGHT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "There should be a NO_WINNER as the board is full and no one has won");

    }

    @Test
    @Grade(value = 0.3, cpuTimeout = 3)
    @GradeFeedback(message = "Congrats!", on= TestResultStatus.SUCCESS)
    @GradeFeedback(message = "Something is wrong in the finding of a vertical win", on=TestResultStatus.FAIL)
    @GradeFeedback(message = "Too slow, infinite loop ?", on=TestResultStatus.TIMEOUT)
    public void testSimpleVertical(){
        Pentago game = new Pentago();
        Pentago.Winner winner;
        winner = game.play(3, 3, Pentago.Player.A, Pentago.BoardSubpart.BOTTOM_LEFT, Pentago.RotationDirection.LEFT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(5, 3, Pentago.Player.A, Pentago.BoardSubpart.TOP_LEFT, Pentago.RotationDirection.RIGHT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(1, 3, Pentago.Player.A, Pentago.BoardSubpart.BOTTOM_LEFT, Pentago.RotationDirection.LEFT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(4, 3, Pentago.Player.A, Pentago.BoardSubpart.TOP_LEFT, Pentago.RotationDirection.LEFT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(2, 3, Pentago.Player.A, Pentago.BoardSubpart.BOTTOM_LEFT, Pentago.RotationDirection.RIGHT);
        assertEquals(Pentago.Winner.A_WINS, winner, "A should have won");
    }

    @Test
    @Grade(value = 0.3, cpuTimeout = 3)
    @GradeFeedback(message = "Congrats!", on= TestResultStatus.SUCCESS)
    @GradeFeedback(message = "Something is wrong in the finding of an horizontal win", on=TestResultStatus.FAIL)
    @GradeFeedback(message = "Too slow, infinite loop ?", on=TestResultStatus.TIMEOUT)
    public void testSimpleHorizontal(){
        Pentago game = new Pentago();
        Pentago.Winner winner;
        winner = game.play(5, 1, Pentago.Player.B, Pentago.BoardSubpart.TOP_RIGHT, Pentago.RotationDirection.LEFT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(5, 3, Pentago.Player.B, Pentago.BoardSubpart.TOP_LEFT, Pentago.RotationDirection.RIGHT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(5, 2, Pentago.Player.B, Pentago.BoardSubpart.TOP_RIGHT, Pentago.RotationDirection.LEFT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(5, 0, Pentago.Player.B, Pentago.BoardSubpart.TOP_LEFT, Pentago.RotationDirection.LEFT);
        assertEquals(Pentago.Winner.NO_WINNER, winner, "No one should have won yet");
        winner = game.play(5, 4, Pentago.Player.B, Pentago.BoardSubpart.TOP_RIGHT, Pentago.RotationDirection.RIGHT);
        assertEquals(Pentago.Winner.B_WINS, winner, "B should have won");
    }
    // END STRIP
}
