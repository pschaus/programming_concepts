package basics;

import java.util.Arrays;
import java.util.Comparator;


/**
 * Nous sommes intéressés à prédire le succès d'un étudiant à
 * l'examen LEPL1402 en fonction des notes qu'il ou elle a obtenues
 * aux exercices inginious pendant le quadrimestre.
 * Pour faire cette prédiction, nous utiliserons les données collectées
 * au cours des années passées pour les anciens étudiants. Ces données
 * historiques contiennent pour chaque étudiant :
 * - les notes (sur 100) obtenues pour chaque exercice inginious.
 * - si l'étudiant a réussi (true) l'examen ou non (false)
 *
 * L'algorithme de prédiction que vous devez écrire (méthode predictSuccess)
 * prend les notes d'exercice du étudiant et doit faire un "vote
 * majoritaire" parmi les k anciens étudiants ayant le profil de notes
 * le plus similaire à celui de cet étudiant.
 * La similarité entre les notes de deux étudiants est calculée avec
 * la méthode EuclideanDistance fournie (plus la distance est petite,
 * plus les deux étudiants sont similaires).
 * Si le nombre de réussites est supérieur (strictement) à k/2 parmi les
 * k étudiants les plus similaires, l'algorithme prédit le succès (vrai),
 * sinon il prédit l'échec (faux).
 *
 *
 * Voici un exemple où nous avons les données de six anciens étudiants pour
 * deux exercices inginious:
 *
 *   KNN.Student[] étudiants = new KNN.Student[] {
 *     new KNN.Student(new double[]{90,91}, true),  // étudiant_0
 *     new KNN.Student(new double[]{80,75}, true),  // étudiant_1
 *     new KNN.Student(new double[]{70,65}, false), // étudiant_2
 *     new KNN.Student(new double[]{30,40}, true),  // étudiant_3
 *     new KNN.Student(new double[]{20,30}, false), // étudiant_4
 *     new KNN.Student(new double[]{45,33}, false), // étudiant_5
 *   };
 *
 * Essayons de prédire le succès d'un nouvel étudiant X qui a
 * obtenu les notes [88,95] aux exercices:
 * Si nous calculons la différence entre cet étudiant et les
 * six anciens, nous pouvons voir que les k=3 étudiants les plus
 * similaires sont l'étudiant_0, l'étudiant_1 et l'étudiant_2.
 * Nous pouvons également voir que l'étudiant_0 et l'étudiant_1
 * ont réussi et que l'étudiant_2 a échoué à l'examen.
 * Cela signifie que nous avons 2 x true et 1 x false (majorité true),
 * donc la méthode devrait prédire une réussite pour l'étudiant X:
 *
 *   double [] goodResults = new double[]{88,95} ;
 *   assertTrue(KNN.predictSuccess(students,goodResults,3)) ;
 *
 * Votre implémentation de la predictSuccess doit s'exécuter en O(n.log(n))
 * où n est le nombre de données historiques.
 * Notez que c'est la complexité temporelle du meilleur algorithme
 * de tri disponible dans l'API Java.
 * Ne réinventez donc pas l'eau chaude et n'hésitez pas à les utiliser.
 */

/**
 * We are interested in predicting the success of a student at the
 * LEPL1402 exam based on the grades the student obtained in inginious
 * exercises during the quadrimester.
 * To do the prediction, we will use the data collected over past years
 * from previous students. This historical data contains for each student:
 *  - the grades (on 100) obtained for each inginious exercise
 *  - if the student succeeded (true) at the exam or not (false)
 *
 * The prediction algorithm that you have to write (method predictSuccess)
 * takes the exercise grades of the student and must do a "majority vote"
 * among the k previous students having the most similar scoring profile to
 * this student.
 * The similarity between the grades of two students is calculated with the
 * euclideanDistance method provided (the smaller the distance, the more
 * similar the two students).
 * If the number of successes is strictly larger than k/2 among the k most
 * similar students, then the algorithm predicts success (true), false otherwise.
 *
 * Here is an example where we have data of six students for two inginious
 * exercises:
 *
 *   KNN.Student[] students = new KNN.Student[] {
 *     new KNN.Student(new double[]{90,91}, true),  // student_0
 *     new KNN.Student(new double[]{80,75}, true),  // student_1
 *     new KNN.Student(new double[]{70,65}, false), // student_2
 *     new KNN.Student(new double[]{30,40}, true),  // student_3
 *     new KNN.Student(new double[]{20,30}, false), // student_4
 *     new KNN.Student(new double[]{45,33}, false), // student_5
 *   };
 *
 * Now, let's try to predict the success of a new student X who got the
 * grades [88,95] at the exercises:
 * If we calculate the distance between this new student and the other six
 * students, we can see that the k=3 most similar students are student_0,
 * student_1, and student_2. We can also see that student_0 and student_1
 * succeeded and student_2 failed the exam. This means we have 2 x true
 * and 1 x false (majority true), thus the method should predict a
 * success for student X:
 *
 *    double [] goodResults = new double[]{88,95};
 *    assertTrue(KNN.predictSuccess(students,goodResults,3));
 *
 * Your implementation of predict success should execute in O(n.log(n))
 * where n is the number of historical data.
 * Notice that this is the time complexity of the best sort algorithm
 * available in Java API.
 * Don't reinvent the wheel and feel free to use them.
 */
public class KNN {

    static class Student {

        final double [] grades; // grades[i] = inginious grades obtained by this student for exercise i
        final boolean success;  // true if the student succeeded at the exam, false otherwise

        public Student(double [] grades, boolean success) {
            this.grades = grades;
            this.success = success;
        }
    }

    /**
     * Predict the success at the exam for a student given
     * - their grades on inginious for each exercise, and
     * - the historical data about past students.
     * grades.length = students[i].grades.length for all i (they all have followed exactly the same exercises)
     * @param students the past students (inginious grades + exam result success/failure)
     * @param grades, the inginious grades of a student for whom we want to predict the success
     * @param k >= 1 the number of most similar students used to make the prediction
     * @return the prediction of success. True if the number of successes
     *         among the k most similar students is > k/2, false otherwise
     */
    public static boolean predictSuccess(Student [] students, double[] grades, int k) {
        // TODO
        // STUDENT return false;
        // BEGIN STRIP

        Arrays.sort(students, new Comparator<Student>() {
            @Override
            public int compare(Student s1, Student s2) {
                return (int) Math.signum(euclideanDistance(s1.grades,grades) - euclideanDistance(s2.grades, grades));
            }
        });

        int numberOfSuccess = 0;
        for (int i = 0; i < k; i++) {
            if (students[i].success) {
                numberOfSuccess++;
            }
        }
        return numberOfSuccess > k/2;
        // END STRIP
    }

    public static double euclideanDistance(double[] a, double[] b) {
        double dist = 0;
        for (int i = 0; i < a.length; i++) {
            dist += Math.pow(a[i] - b[i], 2);
        }
        return Math.sqrt(dist);
    }


}