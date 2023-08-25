package oop;

import java.util.Iterator;

/**
 * Dans cette question, vous devez implementer le produit scalaire de
 * deux vecteurs donnes sous la forme d'iterateurs.
 * Pour rappel, le produit scalaire entre les vecteurs [4, 5] et [6, 7]
 * est égal à 4 * 6 + 5 * 7. Il se calcule donc en Theta(n) où n est
 * la taille des deux vecteurs
 */

/**
 * In this question, you must implement the dot product of two vectors
 * that are provided as iterators.
 * As a reminder, the scalar product between the vectors [4, 5] and [6, 7]
 * is equal to 4 * 6 + 5 * 7. It is therefore computed in Theta(n) where n is
 * the size of the two vectors
 */
public class DotProductIterator {
    /**
     * Classe qui implemente un iterateur vers les elements d'un
     * tableau contenant des "double". Vous devez implementer cette
     * classe, en respectant la semantique des iterateurs Java.
     */

    /**
     * Class that implements an iterator over an array of "double".
     * This class must fulfill the semantics of Java iterators.
     */
    public static class VectorIterator implements Iterator<Double> {
        // BEGIN STRIP
        private final double[] values;
        private int position;
        // END STRIP

        /**
         * Constructeur de l'iterateur.
         * @param values Le tableau de valeurs sur lequel iterer.
         */

        /**
         * Constructor of the iterator.
         * @param values The array over which to iterate.
         */
        VectorIterator(double[] values) {
            // TODO
            // BEGIN STRIP
            this.values = values;
            this.position = 0;
            // END STRIP
        }

        /**
         * Teste si l'iterateur a atteint la fin du tableau.
         * @return "false" si et seulement si l'iterateur a atteint la
         * fin du tableau.
         */

        /**
         * Checks whether the iterator has reached the end of the array.
         * @return "false" iff. the end of the array is reacher.
         */
        @Override
        public boolean hasNext() {
            // TODO
            // STUDENT return false;
            // BEGIN STRIP
            return position < values.length;
            // END STRIP
        }

        /**
         * Retourne l'element actuellement pointe par l'iterateur,
         * puis avance l'iterateur sur l'element suivant du tableau.
         * @return La valeur de l'element courant.
         * @throws IllegalStateException Si et seulement si cette
         * methode est appelee alors qu'on a deja atteint le dernier
         * element du tableau.
         */

        /**
         * Returns the double that is currently pointed by the iterator,
         * then advance to the next element.
         * @return The value of the current element.
         * @throw IllegalStateException Iff. this method is called
         * once the iterator has already reached the end of the vector.
         */
        @Override
        public Double next() {
            // TODO
            // STUDENT return null;
            // BEGIN STRIP
            if (position == values.length) {
                throw new IllegalStateException();
            } else {
                double value = values[position];
                position++;
                return value;
            }
            // END STRIP
        }
    }

    /**
     * Calcule le produit scalaire (dot product) de deux vecteurs,
     * cad. la somme ponderee des elements des deux vecteurs. Les deux
     * vecteurs sont donnes sous la forme d'iterateurs Java.
     * @param a Iterateur vers les elements du premier vecteur.
     * @param b Iterateur vers les elements du second vecteur.
     * @return Le produit scalaire.
     * @throws IllegalArgumentException Si les deux iterateurs ne
     * contiennent pas le meme nombre d'elements.
     */

    /**
     * Compute the dot product of two vectors, i.e. the weighted sum
     * of two vectors. The two vectors are provided as Java iterators
     * over doubles.
     * @param a Iterator over the first vector.
     * @param b Iterator over the second vector.
     * @return The dot product.
     * @throws IllegalArgumentException If the two iterators don't
     * contain the same number of elements.
     */
    public static double computeDotProduct(Iterator<Double> a,
                                           Iterator<Double> b) {
        // TODO
        // STUDENT return 0;
        // BEGIN STRIP
        double accumulator = 0;

        while (a.hasNext() ||
                b.hasNext()) {
            if (!a.hasNext() ||
                    !b.hasNext()) {
                throw new IllegalArgumentException();
            } else {
                accumulator += a.next() * b.next();
            }
        }

        return accumulator;
        // END STRIP
    }
}
