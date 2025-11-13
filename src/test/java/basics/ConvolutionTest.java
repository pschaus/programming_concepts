package basics;

import org.javagrader.Grade;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// BEGIN STRIP

import java.util.Random;

// END STRIP

@Grade
public class ConvolutionTest {

// BEGIN STRIP
    private final Random r = new Random();

    private int [][] _correctConvolution(int [][] input, int [][] kernel) {
        int nRows = input.length - 2;
        int nCols = input[0].length - 2;
        int [][] res = new int[nRows][nCols];
        for (int row = 0; row < nRows; row++) {
            for (int col = 0; col < nCols; col++) {
                int s = 0;
                for (int k = 0; k < kernel.length; k++) {
                    for (int l = 0; l < kernel[0].length; l++)
                        s += input[row+k][col+l]*kernel[k][l];
                }
                res[row][col] = s;
            }
        }
        return res;
    }

    private int [][] _createKernel() {
        int [][] kernel = new int[3][3];
        for (int i = 0; i < kernel.length; i++) {
            for (int j = 0; j < kernel[0].length; j++)
                kernel[i][j] = r.nextInt(10);
        }
        return kernel;
    }

    private int [][] _createMatrix() {
        int nRows = r.nextInt(20) + 3;
        int nCols = r.nextInt(20) + 3;
        return _createMatrix(nRows, nCols);
    }

    private int [][] _createMatrix(int nRows, int nCols) {
        int [][] matrix = new int[nRows][nCols];
        for (int row = 0; row < nRows; row++) {
            for (int col = 0; col < nCols; col++) {
                matrix[row][col] = r.nextInt(100);
            }
        }
        return matrix;
    }
// END STRIP

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void test1() {
        int [][] input = new int[][] {
                {1,2,1,5,6},
                {3,1,5,7,2},
                {1,4,5,2,1},
                {6,7,1,0,3}
        };

        int [][] kernel = new int[][] {
                {0,0,0},
                {0,0,0},
                {0,0,0}
        };

        int [][] expected = new int[][] {
                {0,0,0},
                {0,0,0}
        };

        int [][] res = Convolution.convolution(input,kernel);

        assertEquals(2, res.length);
        assertEquals(3, res[0].length);

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals(expected[i][j], res[i][j]);
            }
        }

    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void test2() {
        int [][] input = new int[][] {
                {1,2,1,5,6},
                {3,1,5,7,2},
                {1,4,5,2,1},
                {6,7,1,0,3}
        };

        int [][] kernel = new int[][] {
                {0,0,0},
                {0,1,0},
                {0,0,0}
        };

        int [][] expected = new int[][] {
                {1,5,7},
                {4,5,2}
        };

        int [][] res = Convolution.convolution(input,kernel);

        assertEquals(2, res.length);
        assertEquals(3, res[0].length);

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals(expected[i][j], res[i][j]);
            }
        }

    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void test3() {
        int [][] input = new int[][] {
                {1,2,1,5,6},
                {3,1,5,7,2},
                {1,4,5,2,1},
                {6,7,1,0,3}
        };

        int [][] kernel = new int[][] {
                {0,0,0},
                {0,1,1},
                {0,0,0}
        };

        int [][] expected = new int[][] {
                {6,12,9},
                {9,7,3}
        };

        int [][] res = Convolution.convolution(input,kernel);

        assertEquals(2, res.length);
        assertEquals(3, res[0].length);

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals(expected[i][j], res[i][j]);
            }
        }

    }

// BEGIN STRIP
    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testSameRows() {
        int nCols = r.nextInt(10) + 3;
        int [][] input = _createMatrix(3, nCols);
        int [][] kernel = _createKernel();
        
        int [][] expected = _correctConvolution(input, kernel);
        int [][] actual = Convolution.convolution(input, kernel);
        assertArrayEquals(expected, actual, "La méthode ne fonctionne pas lorsque l'input a le même nombre de lignes que le kernel");
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testSameCols() {
        int nRows = r.nextInt(10) + 3;
        int [][] input = _createMatrix(nRows, 3);
        int [][] kernel = _createKernel();
        
        int [][] expected = _correctConvolution(input, kernel);
        int [][] actual = Convolution.convolution(input, kernel);
        assertArrayEquals(expected, actual, "La méthode ne fonctionne pas lorsque l'input a le même nombre de colonnes que le kernel");
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testRandom() {
        int [][] input = _createMatrix();
        int [][] kernel = _createKernel();

        int [][] expected = _correctConvolution(input, kernel);
        int [][] actual = Convolution.convolution(input, kernel);
        assertArrayEquals(expected, actual, "La méthode ne fonctionne pas avec une matrice quelconque");
    }
// END STRIP

}
