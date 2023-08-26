package parallelization;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.function.Function;

public class BrickCounter {
    /**
     * Vous pouvez ajouter de nouvelles méthodes, classes internes,
     * etc., mais vous ne pouvez pas modifier la signature
     * (paramètres, exceptions, etc.)  des méthodes existantes ou le
     * type des membres existants.
     */

    /**
     * You can add new methods, inner classes, etc. but do not modify
     * the signature (parameters, exceptions, etc.) of the existing
     * methods or the types of existing fields.
     */

    public interface Brick {
        String getColor();

        int getSize();
    }


    /**
     * Imaginez un amateur de Lego qui possède un grand nombre de
     * briques Lego (paramètre "bricks") qu'il souhaite placer dans un
     * nombre donné de boîtes (paramètre "n"). Le fan de Lego veut
     * savoir combien de briques vont dans la première boîte (boîte
     * 0), combien de briques vont dans la deuxième boîte (boîte 1),
     * etc. Pour décider à quelle boîte une brique appartient, le fan
     * de Lego utilise une fonction "sorter" qui renvoie pour une
     * brique le numéro de sa boîte.
     *
     * Ecrivez la méthode "countBricks" qui renvoie un tableau
     *   [ a_0, a_1, a_2, ..., a_n-1]
     * où l'élément a_i signifie que a_i briques vont dans la boîte i.
     *
     * Voir le test "testThreeBricks" pour un exemple.
     *
     * Complexité temporelle:
     *     Votre méthode doit s'executer en O(max(n,bricks.length))
     *
     * Vous pouvez supposer que :
     *    - n est toujours supérieur à 0
     *    - la fonction "sorter" renvoie toujours un nombre compris entre 0 et n-1.
     */

    /**
     * Imagine a Lego fan who has a large number of Lego bricks
     * (parameter "bricks") that they want to put into a given number
     * of boxes (parameter "n").  The Lego fan wants to know how many
     * bricks go into the first box (box 0), how many bricks go into
     * the second box (box 1), etc.  To decide in which box a brick
     * belongs, the Lego fan uses a "sorter" function. The sorter
     * function returns for a brick the box number.
     *
     * Write the method "countBricks" that returns an array
     *   [ a_0, a_1, a_2, ..., a_n-1]
     * where element a_i means that a_i bricks go into box i.
     *
     * See the test "testThreeBricks" for an example.
     *
     * Time complexity:
     *      Your method should execute in O(max(n,bricks.length))
     *
     * You can assume:
     *    - n is always greater than 0
     *    - the function "sorter" always returns a number between 0 and n-1.
     *
     * @param bricks  the bricks to count
     * @param n the number of boxes
     * @param sorter the function tells in which box a brick belongs
     * @return an array with the number of bricks per box
     */
    public static int[] countBricks(Brick[] bricks, int n, Function<Brick, Integer> sorter) {
        // TODO
        // BEGIN STRIP
        int[] boxes = new int[n];
        for (Brick brick : bricks) {
            int box = sorter.apply(brick);
            boxes[box]++;
        }
        return boxes;
        // END STRIP
        // STUDENT return null;
    }

    // BEGIN STRIP
    private static int[] countBricks(Brick[] bricks, int start, int end, int n, Function<Brick, Integer> sorter) {
        int[] boxes = new int[n];
        for (int i = start; i < end; i++) {
            int box = sorter.apply(bricks[i]);
            boxes[box]++;
        }
        return boxes;
    }
    // END STRIP

    /**
     * Écrivez la méthode "countBricksTwoThreads". Cette méthode fait
     * la même chose que la méthode "countBricks".  Cependant, la
     * méthode DOIT utiliser deux "Future" d'un thread pool pour
     * accélérer le comptage des briques.
     *
     * ATTENTION :
     *   - Vous DEVEZ utiliser deux threads. Vous obtiendrez 0 point
     *     pour cette méthode si vous n'utilisez pas le thread pool
     *     donné en argument, même si votre code renvoie le bon
     *     résultat sur inginious !
     *   - Les calculs doivent être équilibrés entre les deux threads (c'est-à-dire
     *     que les deux threads doivent faire à peu près la même quantité de travail).
     *   - Le paramètre "executor" correspond au "thread pool" a
     *     utiliser. Vous ne devez *pas* appeler vous-memes la methode
     *     "Executors.newFixedThreadPool()" pour creer un thread pool,
     *     ni la methode "executor.shutdown()" (cela est deja fait
     *     pour vous dans les tests unitaires).
     *   - Vous DEVEZ attraper toutes les exceptions. Renvoyez "null" en
     *     cas de problème.
     */

    /**
     * Write the method "countBricksTwoThreads". The method does the
     * same thing as the method "countBricks".  However, the method
     * MUST use two "Future" of a thread pool to accelerate the brick
     * counting.
     *
     * WARNING:
     *   - You MUST use two threads.  You will get 0 points for this
     *     method if you don't use threads, even if your code returns
     *     the correct result on inginious!
     *   - The computations must be balanced between the two threads
     *     (i.e. they both must do roughly the same amount of work)
     *   - The "executor" parameter corresponds to the thread pool to
     *     use. You *don't have* to call the
     *     "Executors.newFixedThreadPool()" method by yourself to
     *     create the thread pool, nor the "executor.shutdown()"
     *     method (this is already done for you in the unit tests).
     *   - You MUST catch all exceptions. You can return null if there is a problem.
     */
    public static int[] countBricksTwoThreads(Brick[] bricks, int n, Function<Brick, Integer> sorter,
                                              ExecutorService executor) {
        // TODO
        // BEGIN STRIP
        if (true) {
            // CORRECT SOLUTION
            final Future<int[]> futureA = executor.submit(() -> countBricks(bricks, 0, bricks.length / 2, n, sorter));
            final Future<int[]> futureB = executor.submit(() -> countBricks(bricks, bricks.length / 2, bricks.length, n, sorter));
            try {
                final int[] resultA = futureA.get();
                final int[] resultB = futureB.get();
                for (int i = 0; i < n; i++) {
                    resultA[i] += resultB[i];
                }
                return resultA;
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return null;
            }
        } else if (false) {
            // INCORRECT SOLUTION - Futures are doing nothing
            final Future<Void> futureA = executor.submit(() -> null);
            final Future<Void> futureB = executor.submit(() -> null);
            try {
                futureA.get();
                futureB.get();
                return countBricks(bricks, n, sorter);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return null;
            }
        } else if (false) {
            // INCORRECT SOLUTION - One future does all the job, the second does nothing
            final Future<int[]> futureA = executor.submit(() -> countBricks(bricks, n, sorter));
            final Future<Void> futureB = executor.submit(() -> null);
            try {
                futureB.get();
                return futureA.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return null;
            }
        } else if (false) {
            // INCORRECT SOLUTION - Provided by Pierre, doesn't use "Future"
            int[] res = new int[n];
            for (int i = 0; i < bricks.length; i++) {
                res[sorter.apply(bricks[i])]++;
            }
            Thread t1 = new Thread(() -> {
            });
            Thread t2 = new Thread(() -> {
            });
            executor.submit(t1);
            executor.submit(t2);
            try {
                t1.join();
                t2.join();
            } catch (InterruptedException e) {
            }
            return res;
        } else if (false) {
            // SEMI-INCORRECT SOLUTION - Provided by Pierre, the threads
            // are unbalanced (the second one only "sorts" one brick)
            int [] res = new int[n];

            Future<int []> res1 = executor.submit(() -> {
                int [] r1 = new int[n];
                for (int i = 1; i < bricks.length; i++) {
                    r1[sorter.apply(bricks[i])] ++;
                }
                return r1;
            });
            Future<int []> res2 = executor.submit(() -> {
                int [] r2 = new int[n];
                for (int i = 0; i < Math.min(1, bricks.length); i++) {
                    r2[sorter.apply(bricks[i])] ++;
                }
                return r2;
            });

            int [] r = new int[n];
            try {
                int [] r1 = res1.get();
                int [] r2 = res2.get();
                for (int i = 0; i < n; i++) {
                    r[i] += r1[i];
                    r[i] += r2[i];
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
            return r;
        } else if (false) {
            // ANOTHER INCORRECT SOLUTION - By Pierre
            int[] res = new int[n];
            Future<int[]> res1 = executor.submit(() -> {
                int[] r1 = new int[n];
                for (int i = 0; i < bricks.length; i += 1) {
                    r1[sorter.apply(bricks[i])]++;
                }
                return r1;
            });
            for (int i = 0; i < bricks.length; i += 1) {
                sorter.apply(bricks[i]);
            }
            try {
                return res1.get();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        } else if (false) {
            // THIS VERSION FAILED WITH TIMEOUT DURING THE EXAM
            Brick[] brick1 = new Brick[bricks.length / 2];
            for (int i = 0; i < bricks.length / 2; i++) {
                brick1[i] = bricks[i];
            }
            Brick[] brick2 = new Brick[bricks.length / 2];
            for (int i = bricks.length / 2 + 1; i < bricks.length; i++) {
                brick2[i] = bricks[i];
            }
            if (bricks.length == 2) {
                brick1[0] = bricks[0];
                brick2[0] = bricks[1];
            }
            Future<int[]> first_count = executor.submit(() -> countBricks(brick1, n, sorter));
            Future<int[]> second_count = executor.submit(() -> countBricks(brick2, n, sorter));
            int[] m = new int[n];
            try {
                int[] m1 = first_count.get();
                int[] m2 = second_count.get();
                for (int i = 0; i < n; i++) {
                    if (m2[i] != 0 && m1[i] != 0) m[i] += (m1[i] + m2[i]);
                }
                return m;
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        } else if (false) {
            // CORRECT SOLUTION by student Mathis Delsart, that was denied
            // during the exam because it didn't launch exactly 2 threads
            // (the student launches 0 thread if the array is empty or only
            // contains 1 element)
            int[] array = new int[n];
            int lengthBricks = bricks.length;

            if (lengthBricks > 1) {

                Brick[] firstHalf = new Brick[lengthBricks / 2];
                Brick[] secondHalf = new Brick[lengthBricks - (lengthBricks / 2)];

                for (int i = 0; i < bricks.length; i++) {
                    if (i < lengthBricks / 2) {
                        firstHalf[i] = bricks[i];
                    } else {
                        secondHalf[i - lengthBricks / 2] = bricks[i];
                    }
                }

                Future<int[]> future1 = executor.submit(() -> countBricks(firstHalf, n, sorter));
                Future<int[]> future2 = executor.submit(() -> countBricks(secondHalf, n, sorter));

                try {
                    int[] firstArray = future1.get();
                    int[] secondArray = future2.get();

                    for (int i = 0; i < n; i++) {
                        array[i] = firstArray[i] + secondArray[i];
                    }

                } catch (InterruptedException | ExecutionException e) {
                    return null;
                }
            } else if (lengthBricks == 1) {
                try {
                    array = executor.submit(() -> countBricks(bricks, n, sorter)).get();
                } catch (InterruptedException | ExecutionException e) {
                    return null;
                }
            }
            return array;
        } else if (false) {
            // INCORRECT SOLUTION, where the students don't run "get()"
            final Future<int[]> futureA = executor.submit(() -> countBricks(bricks, 0, bricks.length / 2, n, sorter));
            final Future<int[]> futureB = executor.submit(() -> countBricks(bricks, bricks.length / 2, bricks.length, n, sorter));
            return null;
        } else {
            // INCORRECT SOLUTION - No thread at all
            return countBricks(bricks, n, sorter);
        }
        // END STRIP
        // STUDENT return null;
    }
}
