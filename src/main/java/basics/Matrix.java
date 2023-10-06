package basics;

public class Matrix {

    /**
     * Create a matrix from a String.
     *
     * The string if formatted as follow:
     *  - Each row of the matrix is separated by a newline
     *    character \n
     *  - Each element of the rows are separated by a space
     *
     *  @param s The input String
     *  @return The matrix represented by the String
     */
    public static int[][] buildFrom(String s) {
        // STUDENT return null;
        // BEGIN STRIP
        String[] lines = s.split("\n");
        int[][] res = new int[lines.length][];
        for (int i = 0; i < lines.length; i++) {
            String[] words = lines[i].split(" ");
            res[i] = new int[words.length];
            for (int j = 0; j < words.length; j++) {
                res[i][j] = Integer.parseInt(words[j]);
            }
        }
        return res;
        // END STRIP
    }


    /**
     * Compute the sum of the element in a matrix
     *
     * @param matrix The input matrix
     * @return The sum of the element in matrix
     */
    public static int sum(int[][] matrix) {
        // STUDENT return 0;
        // BEGIN STRIP
        int total = 0;
        for (int i = 0; i<matrix.length; i++){
            for (int j = 0; j<matrix[i].length; j++){
                total += matrix[i][j];
            }
        }
        return total;
        // END STRIP
    }

    /**
     * Compute the transpose of a matrix
     *
     * @param matrix The input matrix
     * @return A new matrix that is the transpose of matrix
     */
    public static int[][] transpose(int[][] matrix) {
        // STUDENT return null;
        // BEGIN STRIP
        int[][] m = new int[matrix[0].length][matrix.length];
        for (int i = 0; i<matrix.length; i++){
            for (int j = 0; j<matrix[0].length; j++){
                m[j][i] = matrix[i][j];
            }
        }
        return m;
        // END STRIP
    }

    /**
     * Compute the product of two matrix
     *
     * @param matrix1 A n x m matrix
     * @param matrix2 A m x k matrix
     * @return The n x k matrix product of matrix1 and matrix2
     */
    public static int[][] product(int[][] matrix1, int[][] matrix2) {
        // STUDENT return null;
        // BEGIN STRIP
        int[][] m = new int[matrix1.length][matrix2[0].length];
        for (int i=0; i<m.length; i++){
            for (int j=0; j<m[0].length; j++){
                int sum = 0;
                for (int k=0; k<matrix1[i].length; k++){
                    sum += matrix1[i][k]*matrix2[k][j];
                }
                m[i][j] = sum;
            }
        }
        return m;
        // END STRIP
    }
}