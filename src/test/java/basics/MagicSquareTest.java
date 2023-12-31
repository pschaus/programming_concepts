package basics;

import org.javagrader.Grade;
import org.javagrader.GradeFeedback;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Grade
public class MagicSquareTest {



    @Test
    @Grade(value = 6, cpuTimeout = 1000)
    @GradeFeedback(message = "Verify permutation (all numbers from 1..n^2 present exactly once")
    public void testPermutation() {

        int[][] magic = new int[][]
                       {{2, 7, 6},
                        {9, 5, 1},
                        {4, 3, 8}};

        int[][] notMagic1 = new int[][]
                       {{1, 9, 5},
                        {9, 5, 1},
                        {5, 1, 9}};

        int[][] notMagic2 = new int[][]
                       {{1,1,16,16},
                        {1,16,1,16},
                        {16,1,16,1},
                        {16,16,1,1}};



        assertTrue(MagicSquare.isMagicSquare(magic));
        assertFalse(MagicSquare.isMagicSquare(notMagic1));
        assertFalse(MagicSquare.isMagicSquare(notMagic2));
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    @GradeFeedback(message = "Verify the sum of columns")
    public void testSumColumns() {
        int[][] magic = new int[][]
                       {{2, 7, 6},
                        {9, 5, 1},
                        {4, 3, 8}};

        int[][] notMagic = new int[][]
                       {{2, 7, 4},
                        {9, 5, 1},
                        {6, 3, 8}};



        assertTrue(MagicSquare.isMagicSquare(magic));
        assertFalse(MagicSquare.isMagicSquare(notMagic));
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    @GradeFeedback(message = "Verify the sum of rows")
    public void testSumRows() {
        int[][] magic = new int[][]
                       {{2, 7, 6},
                        {9, 5, 1},
                        {4, 3, 8}};

        int[][] notMagic = new int[][]
                       {{2, 7, 4},
                        {9, 5, 1},
                        {6, 3, 8}};

        assertTrue(MagicSquare.isMagicSquare(magic));
        assertFalse(MagicSquare.isMagicSquare(notMagic));
    }


    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    @GradeFeedback(message = "Verify sum of diagonals")
    public void testSumDiag() {
        int[][] magic = new int[][]
                       {{2, 7, 6},
                        {9, 5, 1},
                        {4, 3, 8}};

        // sum of diagonal problem
        int[][] notMagic = new int[][]
                       {{7, 2, 6},  // 15
                        {5, 9, 1},  // 15
                        {3, 4, 8}}; //

        assertTrue(MagicSquare.isMagicSquare(magic));
        assertFalse(MagicSquare.isMagicSquare(notMagic));
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    @GradeFeedback(message = "Verify the range of values {1..n*n}")
    public void testRange() {
        int[][] magic = new int[][]
                       {{2, 7, 6},
                        {9, 5, 1},
                        {4, 3, 8}};

        int[][] notMagic = new int[][]
                       {{1, 6, 5},
                        {8, 4, 0},
                        {3, 2, 7}};

        assertTrue(MagicSquare.isMagicSquare(magic));
        assertFalse(MagicSquare.isMagicSquare(notMagic));
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testSquare6() {

        int [][] sol6 = new int[][]{
                {1, 2, 3, 34, 35, 36, 4, 18, 28, 29, 5, 27, 10, 26, 30, 8, 23, 14, 31, 25, 13, 21, 15, 6, 32, 16, 20, 12, 22, 9, 33, 24, 17, 7, 11, 19},
                {1, 2, 3, 34, 35, 36, 4, 18, 29, 27, 5, 28, 10, 25, 30, 9, 23, 14, 31, 26, 12, 21, 15, 6, 32, 16, 20, 13, 22, 8, 33, 24, 17, 7, 11, 19},
                {1, 2, 3, 34, 35, 36, 4, 18, 29, 27, 5, 28, 10, 26, 30, 9, 23, 13, 31, 24, 12, 21, 15, 8, 32, 16, 20, 14, 22, 7, 33, 25, 17, 6, 11, 19},
                {1, 2, 3, 34, 35, 36, 4, 18, 29, 28, 5, 27, 10, 25, 30, 7, 24, 15, 31, 26, 13, 21, 14, 6, 32, 17, 20, 12, 22, 8, 33, 23, 16, 9, 11, 19},
                {1, 2, 3, 34, 35, 36, 4, 19, 29, 26, 5, 28, 10, 27, 30, 9, 21, 14, 31, 25, 8, 23, 13, 11, 32, 20, 24, 7, 22, 6, 33, 18, 17, 12, 15, 16},
                {1, 2, 3, 34, 35, 36, 4, 19, 29, 26, 5, 28, 10, 27, 30, 9, 22, 13, 31, 25, 11, 23, 15, 6, 32, 17, 24, 12, 18, 8, 33, 21, 14, 7, 16, 20},
                {1, 2, 3, 34, 35, 36, 4, 19, 29, 26, 5, 28, 10, 27, 30, 9, 24, 11, 31, 23, 13, 21, 17, 6, 32, 15, 16, 14, 22, 12, 33, 25, 20, 7, 8, 18},
                {1, 2, 3, 34, 35, 36, 4, 19, 29, 28, 5, 26, 10, 27, 30, 6, 21, 17, 31, 25, 11, 22, 15, 7, 32, 20, 14, 13, 23, 9, 33, 18, 24, 8, 12, 16},
                {1, 2, 3, 34, 35, 36, 4, 19, 29, 28, 5, 26, 10, 27, 30, 6, 24, 14, 31, 20, 13, 22, 17, 8, 32, 18, 15, 12, 23, 11, 33, 25, 21, 9, 7, 16},
                {1, 2, 3, 34, 35, 36, 4, 19, 29, 28, 5, 26, 10, 27, 30, 6, 23, 15, 31, 21, 13, 25, 9, 12, 32, 18, 20, 11, 22, 8, 33, 24, 16, 7, 17, 14},
                {1, 2, 3, 34, 35, 36, 4, 19, 29, 28, 5, 26, 10, 27, 30, 6, 17, 21, 31, 22, 13, 24, 14, 7, 32, 18, 16, 11, 25, 9, 33, 23, 20, 8, 15, 12},
                {1, 2, 3, 34, 35, 36, 4, 19, 29, 28, 5, 26, 10, 30, 27, 6, 21, 17, 31, 20, 13, 24, 14, 9, 32, 18, 16, 12, 25, 8, 33, 22, 23, 7, 11, 15},

        };

        int [][] notSol6 = new int[][]{
                {1, 2, 3, 34, 36, 36, 4, 18, 28, 29, 5, 27, 10, 26, 30, 8, 23, 14, 31, 25, 13, 21, 15, 6, 32, 16, 20, 12, 22, 9, 33, 24, 17, 7, 11, 19},
                {1, 2, 3,34, 36,  4, 18, 29, 27, 5, 28, 10, 25, 30, 9, 23, 14, 31, 26, 12, 21, 15, 6, 32, 16, 20, 35, 13, 22, 8, 33, 24, 17, 7, 11, 19},
                {2, 3, 34, 35, 36, 4, 18, 29, 27, 5, 28, 10, 26, 30, 9, 23, 13, 1, 31, 24, 12, 21, 15, 8, 32, 16, 20, 14, 22, 7, 33, 25, 17, 6, 11, 19},
                {1, 2, 3, 34, 35, 36, 4, 18, 29, 28, 5, 27, 10, 25, 30, 7, 24, 15, 31, 26, 13, 21, 14, 6, 33, 17, 20, 12, 22, 8, 33, 23, 16, 9, 11, 19},
                {1, 2, 3, 34, 35, 36, 4, 18, 29, 30, 5, 25, 11, 27, 28, 8, 22, 16, 31, 26, 14, 21, 13, 6, 32, 15, 20, 11, 24, 9, 33, 23, 17, 7, 12, 19},
                {1, 2, 3, 34, 35, 36, 4,18, 30, 26, 5, 28, 10, 25, 29, 12, 20, 15, 31, 27, 9, 22, 14, 8, 32, 16, 21, 11, 7,  24, 33, 23, 19, 6, 13, 17},
                {1, 2, 3, 34, 35, 36, 4, 19, 27, 30, 5, 26, 10, 29, 28, 6, 23, 15, 31, 22, 17, 21, 13, 7, 32, 15, 20, 12, 24, 9, 33, 25, 16, 8, 11, 18},
                {1, 2, 3, 34, 35, 36, 4, 19, 27, 30, 5, 26, 10, 29, 28, 6, 24, 14, 31, 21, 16, 22, 13, 8, 32, 15, 21, 12, 21, 9, 33, 25, 17, 7, 11, 18},
                {1, 2, 3, 34, 35, 36, 19, 28, 25, 5, 30, 10, 29, 8, 23, 14, 31, 20, 12, 24, 4, 15, 9, 32, 17, 21, 13, 22, 6, 33, 26, 18, 7, 27, 11, 16},
                {7, 2, 3, 34, 35, 36, 4, 19, 28, 25, 5, 30, 10, 29, 27, 8, 20, 17, 31, 24, 13, 23, 14, 6, 32, 16, 18, 12, 26, 7, 33, 21, 22, 9, 15, 11},
                {1, 3, 2, 34, 35, 36, 4, 19, 28, 26, 5, 29, 10, 30, 27, 6, 24, 14, 31, 23, 15, 22, 13, 7, 32, 16, 18, 12, 25, 8, 33, 21, 20, 11, 9, 17},
                {1, 4, 2, 3, 34, 35, 36, 19, 29, 28, 5, 26, 10, 27, 30, 6, 17, 21, 31, 22, 13, 24, 14, 7, 32, 18, 16, 11, 25, 9, 33, 23, 20, 8, 15, 12},
                {1, 2, 3, 34, 35, 36, 4, 19, 29, 28, 5, 26, 10, 30, 27, 6, 21, 17, 31, 20, 13, 24, 14, 9, 32, 18, 16, 25, 12, 8, 33, 22, 23, 7, 11, 15},
                {1, 2, 3, 34, 35, 36, 5, 19, 29, 28, 5, 26, 10, 30, 27, 6, 21, 17, 31, 20, 13, 24, 14, 9, 32, 18, 16, 12, 25, 8, 33, 22, 23, 7, 11, 15},
                {4, 19, 29, 28, 5, 26, 1, 2, 3, 34, 35, 36, 10, 30, 27, 6, 21, 17, 31, 20, 13, 24, 14, 9, 32, 18, 16, 12, 25, 8, 33, 22, 23, 7, 11, 15},
        };

        for (int i = 0; i < sol6.length; i++) {
            assertTrue(MagicSquare.isMagicSquare(toMatrix(sol6[i])));
        }

        for (int i = 0; i < notSol6.length; i++) {
            assertFalse(MagicSquare.isMagicSquare(toMatrix(notSol6[i])));
        }
    }

    static int[][] toMatrix(int [] array) {
        int n = (int) Math.round(Math.sqrt(array.length));
        int [][] res = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                res[i][j] = array[i*n+j];
            }
        }
        return res;
    }

    // BEGIN STRIP

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testSquare5() {
        int[][] sol5 = new int[][]{
                {1, 2, 13, 24, 25, 5, 23, 14, 6, 17, 19, 15, 12, 8, 11, 22, 4, 16, 20, 3, 18, 21, 10, 7, 9},
                {1, 2, 13, 24, 25, 5, 21, 17, 7, 15, 19, 16, 12, 10, 8, 22, 3, 14, 20, 6, 18, 23, 9, 4, 11},
                {1, 2, 13, 24, 25, 5, 23, 17, 8, 12, 19, 15, 10, 7, 14, 22, 4, 16, 20, 3, 18, 21, 9, 6, 11},
                {1, 2, 13, 24, 25, 5, 23, 12, 8, 17, 19, 21, 10, 6, 9, 22, 4, 16, 20, 3, 18, 15, 14, 7, 11},
                {1, 2, 13, 24, 25, 5, 23, 19, 4, 14, 20, 16, 8, 9, 12, 18, 7, 15, 22, 3, 21, 17, 10, 6, 11},
                {1, 2, 13, 24, 25, 5, 23, 22, 4, 11, 20, 16, 8, 15, 6, 18, 7, 12, 19, 9, 21, 17, 10, 3, 14},
                {1, 2, 13, 24, 25, 5, 23, 19, 6, 12, 20, 22, 9, 11, 3, 18, 4, 16, 17, 10, 21, 14, 8, 7, 15},
                {1, 2, 13, 24, 25, 5, 23, 22, 7, 8, 20, 19, 11, 12, 3, 21, 4, 9, 16, 15, 18, 17, 10, 6, 14},
                {1, 2, 13, 24, 25, 5, 22, 17, 7, 14, 20, 16, 9, 8, 12, 21, 6, 11, 23, 4, 18, 19, 15, 3, 10},
                {1, 2, 13, 24, 25, 5, 23, 14, 7, 16, 20, 22, 9, 11, 3, 21, 6, 19, 15, 4, 18, 12, 10, 8, 17},
                {1, 2, 13, 24, 25, 5, 21, 17, 3, 19, 20, 14, 15, 12, 4, 23, 6, 11, 18, 7, 16, 22, 9, 8, 10},
                {1, 2, 13, 24, 25, 5, 22, 17, 6, 15, 20, 18, 14, 3, 10, 23, 4, 9, 21, 8, 16, 19, 12, 11, 7},
                {1, 2, 13, 24, 25, 5, 22, 21, 10, 7, 20, 18, 6, 9, 12, 23, 8, 11, 19, 4, 16, 15, 14, 3, 17},
                {1, 2, 13, 24, 25, 5, 23, 17, 4, 16, 21, 15, 10, 12, 7, 18, 6, 11, 22, 8, 20, 19, 14, 3, 9},
                {1, 2, 13, 24, 25, 5, 22, 17, 6, 15, 21, 23, 9, 4, 8, 20, 7, 16, 19, 3, 18, 11, 10, 12, 14},
                {1, 2, 13, 24, 25, 5, 22, 16, 7, 15, 21, 23, 9, 4, 8, 20, 6, 17, 19, 3, 18, 12, 10, 11, 14},
                {1, 2, 13, 24, 25, 5, 23, 16, 9, 12, 21, 15, 10, 11, 8, 20, 3, 19, 17, 6, 18, 22, 7, 4, 14},
                {1, 2, 13, 24, 25, 5, 23, 16, 9, 12, 21, 22, 10, 4, 8, 20, 3, 19, 17, 6, 18, 15, 7, 11, 14},
                {1, 2, 13, 24, 25, 5, 23, 12, 9, 16, 21, 22, 10, 8, 4, 20, 3, 19, 17, 6, 18, 15, 11, 7, 14},
                {1, 2, 13, 24, 25, 5, 23, 18, 4, 15, 21, 12, 11, 14, 7, 22, 9, 6, 20, 8, 16, 19, 17, 3, 10},
                {1, 2, 13, 24, 25, 5, 23, 12, 7, 18, 21, 17, 14, 9, 4, 22, 3, 11, 19, 10, 16, 20, 15, 6, 8},
                {1, 2, 13, 24, 25, 5, 23, 12, 7, 18, 21, 20, 14, 6, 4, 22, 3, 11, 19, 10, 16, 17, 15, 9, 8},
                {1, 2, 13, 24, 25, 5, 20, 18, 10, 12, 21, 23, 8, 9, 4, 22, 6, 11, 19, 7, 16, 14, 15, 3, 17},
                {1, 2, 13, 24, 25, 5, 23, 20, 11, 6, 21, 18, 10, 4, 12, 22, 3, 15, 17, 8, 16, 19, 7, 9, 14},
                {1, 2, 13, 24, 25, 5, 18, 22, 4, 16, 21, 19, 12, 6, 7, 23, 9, 10, 20, 3, 15, 17, 8, 11, 14},
                {1, 2, 13, 24, 25, 5, 19, 20, 4, 17, 21, 16, 11, 9, 8, 23, 10, 7, 22, 3, 15, 18, 14, 6, 12},
                {1, 2, 13, 24, 25, 5, 18, 22, 6, 14, 21, 20, 10, 11, 3, 23, 9, 12, 17, 4, 15, 16, 8, 7, 19},
                {1, 2, 13, 24, 25, 5, 19, 22, 7, 12, 21, 16, 10, 14, 4, 23, 8, 11, 17, 6, 15, 20, 9, 3, 18},
                {1, 2, 13, 24, 25, 5, 22, 19, 9, 10, 21, 17, 12, 7, 8, 23, 4, 18, 14, 6, 15, 20, 3, 11, 16},
                {1, 2, 13, 24, 25, 5, 19, 20, 9, 12, 21, 16, 10, 11, 7, 23, 6, 14, 18, 4, 15, 22, 8, 3, 17},
                {1, 2, 13, 24, 25, 5, 20, 18, 12, 10, 21, 22, 9, 7, 6, 23, 4, 11, 19, 8, 15, 17, 14, 3, 16},
                {1, 2, 13, 24, 25, 5, 20, 23, 3, 14, 22, 17, 8, 11, 7, 18, 10, 12, 21, 4, 19, 16, 9, 6, 15},
                {1, 2, 13, 24, 25, 5, 23, 15, 6, 16, 22, 20, 12, 4, 7, 18, 3, 14, 21, 9, 19, 17, 11, 10, 8},
                {1, 2, 13, 24, 25, 5, 23, 12, 8, 17, 22, 21, 11, 7, 4, 19, 3, 14, 20, 9, 18, 16, 15, 6, 10},
                {1, 2, 13, 24, 25, 5, 23, 17, 8, 12, 22, 21, 11, 7, 4, 19, 3, 9, 20, 14, 18, 16, 15, 6, 10},
                {1, 2, 13, 24, 25, 5, 21, 18, 6, 15, 22, 16, 14, 9, 4, 20, 3, 12, 19, 11, 17, 23, 8, 7, 10},
                {1, 2, 13, 24, 25, 5, 23, 12, 6, 19, 22, 21, 9, 10, 3, 20, 8, 15, 18, 4, 17, 11, 16, 7, 14},
                {1, 2, 13, 24, 25, 5, 23, 21, 7, 9, 22, 18, 6, 15, 4, 20, 10, 11, 16, 8, 17, 12, 14, 3, 19},
                {1, 2, 13, 24, 25, 5, 23, 12, 6, 19, 22, 17, 15, 7, 4, 21, 3, 14, 18, 9, 16, 20, 11, 10, 8},
                {1, 2, 13, 24, 25, 5, 23, 20, 7, 10, 22, 15, 11, 14, 3, 21, 6, 17, 12, 9, 16, 19, 4, 8, 18},
                {1, 2, 13, 24, 25, 5, 23, 11, 7, 19, 22, 20, 9, 10, 4, 21, 8, 15, 18, 3, 16, 12, 17, 6, 14},
                {1, 2, 13, 24, 25, 5, 21, 15, 4, 20, 22, 17, 16, 7, 3, 23, 6, 10, 18, 8, 14, 19, 11, 12, 9},
                {1, 2, 13, 24, 25, 5, 20, 15, 7, 18, 22, 16, 11, 10, 6, 23, 8, 9, 21, 4, 14, 19, 17, 3, 12},
                {1, 2, 13, 24, 25, 5, 21, 15, 8, 16, 22, 17, 12, 10, 4, 23, 6, 7, 20, 9, 14, 19, 18, 3, 11},
        };

        int[][] notSol5 = new int[][]{
                {1, 2, 13, 24, 25, 5, 23, 14, 6, 17, 19, 12, 15, 8, 11, 22, 4, 16, 20, 3, 18, 21, 10, 7, 9},
                {2, 2, 13, 24, 25, 5, 21, 17, 7, 15, 19, 16, 12, 10, 8, 22, 3, 14, 20, 6, 18, 23, 9, 4, 11},
                {0, 2, 13, 24, 25, 5, 23, 12, 8, 17, 19, 21, 10, 6, 9, 22, 4, 16, 20, 3, 18, 15, 14, 7, 11},
                {2, 1, 13, 24, 25, 5, 23, 19, 4, 14, 20, 16, 8, 9, 12, 18, 7, 15, 22, 3, 21, 17, 10, 6, 11},
                {1, 2, 13, 24, 25, 5, 23, 22, 4, 11, 20, 16, 8, 15, 6, 18, 7, 12, 19, 9, 21, 17, 10, 14, 3},
                {1, 2, 13, 24, 25, 6, 22, 16, 7, 15, 21, 23, 9, 4, 8, 20, 6, 17, 19, 3, 18, 12, 10, 11, 14},
                {1, 2, 13, 24, 25, 5, 24, 18, 4, 15, 21, 12, 11, 14, 7, 22, 9, 6, 20, 8, 16, 19, 17, 3, 10},
                {1, 2, 13, 24, 25, 5, 20, 23, 3, 14, 22, 17, 8, 11, 7, 18, 10, 12, 21, 4, 19, 16, 9, 15, 6},
                {1, 2, 13, 24, 25, 5, 23, 6, 16, 15, 22, 20, 12, 4, 7, 18, 3, 14, 21, 9, 19, 17, 11, 10, 8},
                {1, 2, 13, 24, 25, 5, 20, 15, 7, 18, 22, 16, 10, 6, 11, 23, 8, 9, 21, 4, 14, 19, 17, 3, 12},
                {1, 2, 13, 24, 25, 5, 21, 15, 8, 16, 17, 12, 10, 22, 4, 23, 6, 7, 20, 9, 14, 19, 18, 3, 11},
                {2, 1, 13, 24, 25, 21, 5, 15, 8, 16, 17, 22, 12, 10, 4, 6, 23, 7, 20, 9, 19, 14, 18, 3, 11},
        };




        for (int i = 0; i < sol5.length; i++) {
            assertTrue(MagicSquare.isMagicSquare(toMatrix(sol5[i])));
        }

        for (int i = 0; i < notSol5.length; i++) {
            assertFalse(MagicSquare.isMagicSquare(toMatrix(notSol5[i])));
        }
    }

    // END STRIP

}
