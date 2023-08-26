package basics;

public class MagicSquare {


    /**
     * A magic square is an (n x n) matrix such that:
     *
     * - all the positive numbers 1,2, ..., n*n are present (thus each number appears exactly once)
     * - the sums of the numbers in each row, each column and both main diagonals are the same
     *
     *   For instance a 3 x 3 magic square is the following
     *
     *   2 7 6
     *   9 5 1
     *   4 3 8
     *
     *   You have to implement the method that verifies if a matrix is a valid magic square
     */

    /**
     *
     * @param matrix a square matrix of size n x n
     * @return true if matrix is a n x n magic square, false otherwise
     */
    public static boolean isMagicSquare(int [][] matrix) {
        // TODO Implement the body of this method, feel free to add other methods but do not change the signature of isMagiSquare
        // STUDENT return false;
        // BEGIN STRIP
        int n = matrix.length;
        // range
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] <= 0 || matrix[i][j] > n*n) {
                    return false;
                }
            }
        }

        // permutation
        boolean [] present = new boolean[n*n+1];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int v = matrix[i][j];
                if (present[v]) {
                    return false;
                }
                present[v] = true;
            }
        }

        // sum lines and columns
        int expectedTot = (n*n+1)*n/2;
        for (int i = 0; i < n; i++) {
            int totLine = 0;
            int totCol = 0;
            for (int j = 0; j < n; j++) {
                totLine += matrix[i][j];
                totCol += matrix[j][i];
            }
            if (totLine != expectedTot || totCol != expectedTot) {
                return  false;
            }
        }
        // sum diagonals
        int totDiag1 = 0;
        int totDiag2 = 0;
        for (int j = 0; j < n; j++) {
            totDiag1 += matrix[j][j];
            totDiag2 += matrix[j][matrix.length-1-j];
        }
        if (totDiag1 != expectedTot || totDiag2 != expectedTot) {
            return  false;
        }
        return true;
        // END STRIP
    }
}
