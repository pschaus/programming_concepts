package parallelization;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.function.Predicate;

/**
 * [Student contribution: Loïc Dugailliez, August 2026]
 * <p>
 * In the context of the "League of Legends" video game, you must implement the part of the game that determines
 * how champions are affected by physical and magical attacks. The damage taken by a champion can be reduced by
 * their armor and magic resistance (their "ability powers"). The three damage equations are as follows:
 * (effective physical damage) = (initial physical damage) * 100 / (100 + armor)
 * (effective magical damage) = (initial magical damage) * 100 / (100 + magic resistance)
 * (total damage received) = (effective physical damage) + (effective magical damage)
 **/

public class LeagueOfLegends {
    /**
     * Represents one League of Legends champion.
     * You don't have to implement this class, this is done for you in the unit tests.
     **/
    public interface Champion {
        String getName();

        double getArmor();

        double getMagicResistance();
    }

    /**
     * Computes the effective physical damage received by one champion.
     * Check out the equations at the beginning of this file. The floating-point result
     * must be rounded to the nearest integer using "Math.round()".
     *
     * @param champion       The Champion receiving the damage.
     * @param initialDammage Initial physical damage.
     * @return The effective physical damage received after armor mitigation.
     * @throws IllegalArgumentException if initialDammage < 0.
     */
    public static long computePhysicalDamage(Champion champion,
                                             double initialDammage) {
        // TODO
        // STUDENT return -1;
        // BEGIN STRIP
        if (initialDammage < 0) {
            throw new IllegalArgumentException("Invalid value for the initial physical damage");
        } else {
            double armor = champion.getArmor();
            return Math.round((initialDammage * 100) / (100 + armor));
        }
        // END STRIP
    }

    /**
     * Computes the effective magical damage received by one champion.
     * Check out the equations at the beginning of this file. The floating-point result
     * must be rounded to the nearest integer using "Math.round()".
     *
     * @param champion       The Champion receiving the damage.
     * @param initialDammage Initial magical damage.
     * @return The effective magical damage received after magic resistance mitigation.
     * @throws IllegalArgumentException if attackDamage < 0.
     */
    public static long computeMagicalDamage(Champion champion,
                                            double initialDammage) {
        // TODO
        // STUDENT return -1;
        // BEGIN STRIP
        if (initialDammage < 0) {
            throw new IllegalArgumentException("Invalid value for the initial magical damage");
        } else {
            double mr = champion.getMagicResistance();
            return Math.round((initialDammage * 100) / (100 + mr));
        }
        // END STRIP
    }

    /**
     * Class representing a physical attack combined with a magical attack against a champion.
     */
    public static class Attack {
        private double physicalDammage;
        private double magicalDammage;

        /**
         * Create the attack.
         *
         * @param physicalDammage Initial physical damage.
         * @param magicalDammage  Initial magical damage.
         */
        public Attack(double physicalDammage,
                      double magicalDammage) {
            this.physicalDammage = physicalDammage;
            this.magicalDammage = magicalDammage;
        }

        /**
         * Computes the effective total damage received by the given champion as the result
         * of this attack. Check out the equations at the beginning of this file.
         * Hint: Use the methods defined above.
         *
         * @param champion The champion receiving the damage.
         * @return The effective total damage.
         */
        public long computeTotalDamage(Champion champion) {
            // TODO
            // STUDENT return -1;
            // BEGIN STRIP
            return computePhysicalDamage(champion, physicalDammage) + computeMagicalDamage(champion, magicalDammage);
            // END STRIP
        }
    }


    /**
     * Consider a battle between a group of champions and a boss. The boss inflicts both physical and
     * magical damage on a subset of the champions, typically those that are in front of it and within a
     * certain distance. This method must return the total effective damage inflicted by the boss during one attack.
     * <p>
     * This method must be sequential and consider only the champions between startIndex (inclusive)
     * and endIndex (exclusive), so that it can later be parallelized.
     * <p>
     * Example:
     * champions = [A,B,C,D,E]
     * startIndex = 1
     * endIndex = 4
     * -> Only B,C,D must be considered.
     *
     * @param champions          All the champions.
     * @param bossAttack         The attack by the boss (it is the same for all the targeted champions).
     * @param isTargetedChampion Criterion used to identify the champions that are targeted by the attack.
     * @param startIndex         Start index.
     * @param endIndex           End index.
     * @return Sum of all the effective damage received by the champions satisfying the predicate.
     * @throws IllegalArgumentException for invalid indices.
     */
    public static long computeBossTotalDamageSequential(Champion[] champions,
                                                        Predicate<Champion> isTargetedChampion,
                                                        Attack bossAttack,
                                                        int startIndex,
                                                        int endIndex) {
        if (startIndex < 0 || startIndex > endIndex || endIndex > champions.length) {
            throw new IllegalArgumentException("Invalid number for (at least) one index");
        }

        // TODO
        // STUDENT return -1;
        // BEGIN STRIP
        long sum = 0;
        for (int i = startIndex; i < endIndex; i++) {
            if (isTargetedChampion.test(champions[i])) {
                sum += bossAttack.computeTotalDamage(champions[i]);
            }
        }

        return sum;
        // END STRIP
    }

    /**
     * Because League of Legends is a multiplayer game, thousands of players may be playing in different arenas
     * simultaneously. It is therefore important to use multithreading to reduce computation time. You must
     * consequently implement a parallel version of "computeBossTotalDamageSequential(...)".
     * <p>
     * You MUST: Use 2 threads, use the provided ExecutorService, and divide the computation approximately equally
     * between the two threads.
     * You MUST NOT: Create another ExecutorService or call shutdown().
     * <p>
     * If some exception occurs, return -1.
     */
    public static long computeBossTotalDamageParallel(Champion[] champions,
                                                      Predicate<Champion> isTargetedChampion,
                                                      Attack bossAttack,
                                                      ExecutorService executor) {
        // TODO
        // STUDENT return -1;
        // BEGIN STRIP
        final int mid = champions.length / 2;
        Future<Long> future1 = executor.submit(() -> {
            return computeBossTotalDamageSequential(champions, isTargetedChampion, bossAttack, 0, mid);
        });
        Future<Long> future2 = executor.submit(() -> {
            return computeBossTotalDamageSequential(champions, isTargetedChampion, bossAttack, mid, champions.length);
        });

        try {
            return future1.get() + future2.get();
        } catch (ExecutionException | InterruptedException e) {
            return -1;
        }
        // END STRIP
    }


    /**
     * Given a boss attack, this method must return the list of the champions who receive the least damage
     * from the attack. The conventions for the arguments are identical to those of method
     * "computeBossTotalDamageSequential()".
     */
    public static List<Champion> findMostResistentChampionsSequential(Champion[] champions,
                                                                      Predicate<Champion> isTargetedChampion,
                                                                      Attack bossAttack,
                                                                      int startIndex,
                                                                      int endIndex) {
        // TODO
        // STUDENT return null;
        // BEGIN STRIP
        List<Champion> result = new ArrayList<>();
        for (int i = startIndex; i < endIndex; i++) {
            if (isTargetedChampion.test(champions[i])) {
                if (result.isEmpty()) {
                    result.add(champions[i]);
                } else {
                    long championDamage = bossAttack.computeTotalDamage(champions[i]);
                    long bestDamage = bossAttack.computeTotalDamage(result.get(0));
                    if (championDamage < bestDamage) {
                        result.clear();
                        result.add(champions[i]);
                    } else if (championDamage == bestDamage) {
                        result.add(champions[i]);
                    }
                }
            }
        }
        return result;
        // END STRIP
    }


    /**
     * Given a boss attack, this method must return the list of the champions who receive the least damage
     * from the attack. The conventions for the arguments are identical to those of method
     * "computeBossTotalDamageParallel()".
     * <p>
     * If some exception occurs, return null.
     */
    public static List<Champion> findMostResistentChampionsParallel(Champion[] champions,
                                                                    Predicate<Champion> isTargetedChampion,
                                                                    Attack bossAttack,
                                                                    ExecutorService executor) {
        // TODO
        // STUDENT return null;
        // BEGIN STRIP
        try {
            final int mid = champions.length / 2;
            Future<List<Champion>> f1 = executor.submit(
                    () -> findMostResistentChampionsSequential(champions, isTargetedChampion, bossAttack, 0, mid));
            Future<List<Champion>> f2 = executor.submit(
                    () -> findMostResistentChampionsSequential(champions, isTargetedChampion, bossAttack, mid, champions.length));

            List<Champion> l1 = f1.get();
            List<Champion> l2 = f2.get();
            if (l1.isEmpty()) {
                return l2;
            } else if (l2.isEmpty()) {
                return l1;
            } else {
                long damage1 = bossAttack.computeTotalDamage(l1.get(0));
                long damage2 = bossAttack.computeTotalDamage(l2.get(0));
                if (damage1 < damage2) {
                    return f1.get();
                } else if (damage1 > damage2) {
                    return f2.get();
                } else {
                    // Equal damage, merge the two lists
                    for (Champion c : f2.get()) {
                        f1.get().add(c);
                    }
                    return f1.get();
                }
            }
        } catch (ExecutionException | InterruptedException e) {
            return null;
        }
        // END STRIP
    }
}
