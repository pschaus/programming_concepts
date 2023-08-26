package basics;

import java.util.Arrays;

/**
 * Make sure that the equal method consider
 * two QR codes as identical if they have been flipped
 * by 0,1,2 or 3 quarter rotations
 *
 * For instance the two following matrices should be
 * considered equals if you flip your head by 180 degrees
 *
 *         boolean [][] t0 = new boolean[][] {
 *                 {false,true,false,false},
 *                 {false,false,true,true},
 *                 {true,false,false,true},
 *                 {true,true,false,true}
 *         };
 *
 *         // t2 is a version of t2 with two quarter rotations
 *         boolean [][] t2 = new boolean[][] {
 *                 {true,false,true,true},
 *                 {true,false,false,true},
 *                 {true,true,false,false},
 *                 {false,false,true,false}
 *         };
 */
public class QRcode {

    protected boolean [][] data;

    /**
     * Create a QR code object
     * @param data is a n x n square matrix
     */
    public QRcode(boolean [][] data) {
        this.data = data;
    }

    /**
     * Return true if the other matrix is identical up to
     * 0, 1, 2 or 3 number of rotations
     * @param o the other matrix to compare to
     * @return
     */
    @Override
    public boolean equals(Object o) {
        // TODO
        // STUDENT return false;
        // BEGIN STRIP
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QRcode qRcode = (QRcode) o;
        boolean [][] r = ((QRcode) o).data;
        if (equalArray(data,r)) {
            return true;
        }
        for (int i = 0; i < 3; i++) {
            r = rotate(r);
            if (equalArray(data,r)) {
                return true;
            }
        }
        return  false;
        // END STRIP
    }

    // BEGIN STRIP
    private static boolean equalArray(boolean [][] array1, boolean [][] array2) {
        for (int i = 0; i < array1.length; i++) {
            if (!Arrays.equals(array1[i],array2[i])) {
                return false;
            }
        }
        return  true;
    }

    private boolean [][] rotate(boolean[][] input) {
        boolean [][] res = new boolean[data.length][data.length];
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data.length; j++) {
                res[i][j] = input[j][i];
            }
        }
        for (int i = 0; i < data.length/2; i++) {
            boolean [] tmp = res[i];
            res[i] = res[data.length-i-1];
            res[data.length-i-1] = tmp;
        }
        return res;
    }
    // END STRIP


}
