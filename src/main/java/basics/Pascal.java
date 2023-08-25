package basics;

import java.util.Arrays;


/**
 * ***********
 * * English *
 * ***********
 *  The Pascal triangle up to row5 looks like this:
 *
 *  row1      1
 *  row2     1 1
 *  row3    1 2 1
 *  row4   1 3 3 1
 *  row5  1 4 6 4 1
 *
 * As you can observe, each element of each
 * row is either 1 or the sum of
 * the two elements right above it.
 * For example, the last row
 * of the triangle is constructed as such :
 *
 *   1   (since the first element of each row doesn't have two elements above it)
 *   4   (1 + 3)
 *   6   (3 + 3)
 *   4   (3 + 1)
 *   1   (since the last element of each row doesn't have two elements above it)
 *
 *  Your task is to return an array that represents
 *  the nth row of Pascal's triangle
 *
 *  You can add methods in the class.
 *
 *  ************
 *  * Français *
 *  ************
 *
 *  Le triangle de Pascal jusqu'à la 5ème rangée est donné ci-dessous:
 *
 *  row1      1
 *  row2     1 1
 *  row3    1 2 1
 *  row4   1 3 3 1
 *  row5  1 4 6 4 1
 *
 * Comme vous pouvez l'observer, chaque élément de chaque
 * ligne est soit 1, soit la somme de
 * des deux éléments situés juste au-dessus.
 * Par exemple, la dernière ligne
 * du triangle est construite comme suit :
 *
 * 1 (puisque le premier élément de chaque rangée n'a pas deux éléments au-dessus de lui)
 * 4 (1 + 3)
 * 6 (3 + 3)
 * 4 (3 + 1)
 * 1 (puisque le dernier élément de chaque ligne n'a pas deux éléments au-dessus de lui)
 *
 * Votre tâche est de retourner un tableau qui représente
 * la nième ligne du triangle de Pascal
 *
 * Vous pouvez ajouter des méthodes dans la classe.
 *

 */
public class Pascal {
    // BEGIN STRIP
    public static void main(String[] args) {
        System.out.println(Arrays.toString(pascal(12)));
    }
    // END STRIP

    /**
     * Computes the nth row of Pascal triangle
     *
     * @param n > 0
     * @return the nth row of Pascal triangle
     */
    public static int[] pascal(int n) {
        // STUDENT return null;
        // BEGIN STRIP
        return pascal(n, new int[]{1});
        // END STRIP
    }


    // BEGIN STRIP

    /**
     * Computes the nth row of Pascal triangle given a previous valid row
     *
     * @param n    >= prev.length > 0
     * @param prev a valid previous level of Pascal triangle.
     * @return the nth row of Pascal triangle
     */
    public static int[] pascal(int n, int[] prev) {

        if (prev.length == n) return prev;
        else {
            int[] res = new int[prev.length + 1];
            res[0] = 1;
            res[prev.length] = 1;
            for (int i = 1; i < res.length - 1; i++) {
                res[i] = prev[i - 1] + prev[i];
            }
            return pascal(n, res);
        }

    }
    // END STRIP

}
