package basics;

import org.javagrader.Grade;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// BEGIN STRIP
import java.util.Random;
// END STRIP

@Grade
public class CommonElementsTest {

// BEGIN STRIP
    Random r = new Random();
    final int RNG_BOUND=100;

    private int [] _getDistinctRandom(int lb, int ub, int n) {
        return new Random().ints(lb, ub).limit(n).toArray();
    }

    private int _goodCommon(int [] tab1, int [] tab2) {
        int count = 0;
        for (int i = 0; i < Math.min(tab1.length, tab2.length); i++) {
            if (tab1[i] == tab2[i])
                count ++;
        }
        return count;
    }

    private int _goodCommon(int [][] mat1, int [][] mat2) {
        int count = 0;
        for (int row=0; row < Math.min(mat1.length, mat2.length); row++)
            count += _goodCommon(mat1[row], mat2[row]);
        return count;
    }

    private int [] _createRandomArray() {
        int size = r.nextInt(20) + 5;
        return _createRandomArray(size);
    }

    private int [] _createRandomArray(int size) {
        int [] array = new int[size];
        for (int i = 0; i < array.length; i++)
            array[i] = r.nextInt(RNG_BOUND);
        return array;
    }

    private int [][] _createRandomMatrix() {
        int nRows = r.nextInt(10) + 1;
        int nCols = r.nextInt(10) + 1;
        return _createRandomMatrix(nRows, nCols);
    }
    
    private int [][] _createRandomMatrix(int nRows, int nCols) {
        int [][] matrix = new int[nRows][];
        for (int row = 0; row < nRows; row++) {
            matrix[row] = _createRandomArray(nCols);
        }
        return matrix;
    }

    private void _addSameElements(int [] tab1, int [] tab2) {
        int nbSame = r.nextInt(Math.min(tab1.length, tab2.length)) + 1;
        _addSameElements(tab1, tab2, nbSame);
    }

    private void _addSameElements(int [] tab1, int [] tab2, int nbSame) {
        final int [] index = _getDistinctRandom(0, Math.min(tab1.length, tab2.length), nbSame);
        for (int idx : index) {
            int n = r.nextInt(RNG_BOUND);
            tab1[idx] = n;
            tab2[idx] = n;
        }
    }

    private void _addSameElements(int [][] mat1, int [][] mat2) {
        int nbSame = r.nextInt(Math.min(mat1.length*mat1[0].length, mat2.length*mat2[0].length)) + 1;
        _addSameElements(mat1, mat2, nbSame);
    }

    private void _addSameElements(int [][] mat1, int [][] mat2, int nbSame) {
        int remaining = nbSame;
        int nRows = Math.min(mat1.length, mat2.length);
        for (int row = 0; row < nRows && remaining != 0; row++) {
            int nb;
            if (row == nRows - 1) {
                nb = remaining;
            } else {
                nb = r.nextInt(remaining + 1);
            }
            _addSameElements(mat1[row], mat2[row], nb);
            remaining -= nb;
        }
    }

// END STRIP
    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void test1() {
        int [] tab1 = new int[] {3,1,5,7,7};
        int [] tab2 = new int[] {3,1,6,7,7,5};

        assertEquals(4, CommonElements.count(tab1,tab2));
        assertEquals(4, CommonElements.count(tab2,tab1));

    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void test2() {
        int [] tab1 = new int[] {3,5,2,2,2};
        int [] tab2 = new int[] {3,1,6,7,2,5};

        assertEquals(2, CommonElements.count(tab1,tab2));
        assertEquals(2, CommonElements.count(tab2,tab1));

    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void test3() {
        int [][] tab1 = new int[][] {
                {1,2,1,5,6},
                {3,1,5,7,2},
                {1,4,5,2,1},
                {6,7,1,0,3}
        };

        int [][] tab2 = new int[][] {
                {1,2,1,5,6},
                {3,3,5,3,2},
                {1,4,6,2,1},
        };

        assertEquals(12, CommonElements.count(tab2,tab1));
    }
// BEGIN STRIP

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void arraySameSize() {
        int size = r.nextInt(20) + 5;
        int [] tab1 = _createRandomArray(size);
        int [] tab2 = _createRandomArray(size);
        _addSameElements(tab1, tab2, 5);
        int answer = _goodCommon(tab1, tab2);
        assertEquals(answer, CommonElements.count(tab1, tab2),"La méthode ne fonctionne pas avec deux tableau de la même taille");
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void sameArray() {
        int [] tab = _createRandomArray();
        assertEquals(tab.length, CommonElements.count(tab, tab),"La méthode ne fonctionne pas quand le même tableau est passé aux deux arguments");
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void sameArrayDiffRef() {
        int [] tab1 = _createRandomArray();
        int [] tab2 = new int[tab1.length];
        System.arraycopy(tab1, 0, tab2, 0, tab1.length);
        assertEquals(tab1.length, CommonElements.count(tab1, tab2),"La méthode ne fonctionne pas lorsque les deux tableaux contiennent les même éléments");
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void arrayDiff() {
        int [] tab1 = _createRandomArray();
        int [] tab2 = _createRandomArray();
        _addSameElements(tab1, tab2);
        int answer = _goodCommon(tab1, tab2);
        assertEquals(answer, CommonElements.count(tab1, tab2),"La méthode ne fonctionne pas sur des tableaux quelconques");
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testCommutativityArray() {
        int [] tab1 = _createRandomArray();
        int [] tab2 = _createRandomArray();
        assertEquals(CommonElements.count(tab1, tab2), CommonElements.count(tab2, tab1),"La méthode retourne des résultats différent lorsque les paramètres sont inversés");
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void matrixSameRows() {
        int nRows = r.nextInt(10) + 1;
        int [] nCols = _getDistinctRandom(1, 10, 2);
        int [][] mat1 = _createRandomMatrix(nRows, nCols[0]);
        int [][] mat2 = _createRandomMatrix(nRows, nCols[1]);
        _addSameElements(mat1, mat2);
        int answer = _goodCommon(mat1, mat2);
        assertEquals(answer, CommonElements.count(mat1, mat2),"La méthode ne fonctionne pas sur des matrices avec le même nombre de lignes");
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void matrixSameCols() {
        int nCols = r.nextInt(10) + 1;
        int [] nRows = _getDistinctRandom(1, 10, 2);
        int [][] mat1 = _createRandomMatrix(nRows[0], nCols);
        int [][] mat2 = _createRandomMatrix(nRows[1], nCols);
        _addSameElements(mat1, mat2);
        int answer = _goodCommon(mat1, mat2);
        assertEquals(answer, CommonElements.count(mat1, mat2),"La méthode ne fonctionne pas sur des matrices avec le même nombre de colonnes");
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void sameMatrix() {
        int [][] mat = _createRandomMatrix();
        int answer = mat.length*mat[0].length;
        assertEquals(answer, CommonElements.count(mat, mat),"La méthode ne fonctionne pas lorsque la même matrice est passée en argument");
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void sameMatrixDiffRef() {
        int [][] mat1 = _createRandomMatrix();
        int [][] mat2 = new int[mat1.length][mat1[0].length];
        for (int row = 0; row < mat1.length; row++)
            System.arraycopy(mat1[row], 0, mat2[row], 0, mat1[row].length);
        int answer = mat1.length*mat1[0].length;
        assertEquals(answer, CommonElements.count(mat1, mat2),"La méthode ne fonctionne pas lorsque les deux matrices contiennent les mêmes éléments");
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void matrixDiff() {
        int [][] mat1 = _createRandomMatrix();
        int [][] mat2 = _createRandomMatrix();
        _addSameElements(mat1, mat2);
        int answer = _goodCommon(mat1, mat2);
        assertEquals(answer, CommonElements.count(mat1, mat2),"La méthode ne fonctionne pas avec deux matrices quelconques");
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void commutativityMatrix() {
        int [][] mat1 = _createRandomMatrix();
        int [][] mat2 = _createRandomMatrix();
        assertEquals(CommonElements.count(mat1, mat2), CommonElements.count(mat2, mat1),"La méthode retourne des résultats différent lorsque les paramètres sont inversés");
    }
// END STRIP
}
