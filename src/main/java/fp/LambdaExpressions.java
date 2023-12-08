package fp;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.*;

public class LambdaExpressions {
    /**
     * Return a binary operator that computes the sum of two Integer objects.
     */
    public static Object sumOfIntegers() {
        // TODO
        // STUDENT return null;
        // BEGIN STRIP
        BinaryOperator<Integer> f = (a, b) -> a + b;
        return f;
        // END STRIP
    }

    /**
     * Return a predicate that tests whether a String is empty.
     */
    public static Object isEmptyString() {
        // TODO
        // STUDENT return null;
        // BEGIN STRIP
        Predicate<String> f = s -> s.isEmpty();
        return f;
        // END STRIP
    }

    /**
     * Return a predicate that tests whether an Integer is an odd number.
     */
    public static Object isOddNumber() {
        // TODO
        // STUDENT return null;
        // BEGIN STRIP
        Predicate<Integer> f = x -> (x % 2 == 1);
        return f;
        // END STRIP
    }

    /**
     * Return a function that computes the mean of a List of Double objects.
     * If the list is empty, an IllegalArgumentException must be thrown.
     */
    public static Object computeMeanOfListOfDoubles() {
        // TODO
        // STUDENT return null;
        // BEGIN STRIP
        Function<List<Double>,Double> f = x -> {
            if (x.isEmpty()) {
                throw new IllegalArgumentException();
            } else {
                double sum = 0;
                for (Double d: x) {
                    sum += d;
                }
                return sum / (double) x.size();
            }
        };
        return f;
        // END STRIP
    }

    /**
     * Remove the even numbers from a list of Integer objects.
     */
    public static void removeEvenNumbers(List<Integer> lst) {
        // TODO
        // BEGIN STRIP
        lst.removeIf(x -> x % 2 == 0);
        // END STRIP
    }

    /**
     * Return a function that computes the factorial of an Integer.
     * If the number is zero, the factorial equals 1 by convention.
     * If the number is negative, an IllegalArgumentException must be thrown.
     */
    public static Object computeFactorial() {
        // TODO
        // STUDENT return null;
        // BEGIN STRIP
        UnaryOperator<Integer> f = x -> {
            if (x < 0) {
                throw new IllegalArgumentException();
            } else {
                int y = 1;
                for (int i = 2; i <= x; i++) {
                    y *= i;
                }
                return y;
            }
        };
        return f;
        // END STRIP
    }

    /**
     * Return a function that converts a list of String objects to lower case.
     */
    public static Object listOfStringsToLowerCase() {
        // TODO
        // STUDENT return null;
        // BEGIN STRIP
        UnaryOperator<List<String>> f = a -> {
            List<String> b = new ArrayList<>();
            for (String s: a) {
                b.add(s.toLowerCase());
            }
            return b;
        };
        return f;
        // END STRIP
    }

    /**
     * Return a function that concatenates two String objects.
     */
    public static Object concatenateStrings() {
        // TODO
        // STUDENT return null;
        // BEGIN STRIP
        BinaryOperator<String> f = (a, b) -> a + b;
        return f;
        // END STRIP
    }

    public static class MinMaxResult {
        private int minValue;
        private int maxValue;

        MinMaxResult(int minValue,
                     int maxValue) {
            this.minValue = minValue;
            this.maxValue = maxValue;
        }

        int getMinValue() {
            return minValue;
        }

        int getMaxValue() {
            return maxValue;
        }
    }

    /**
     * Return a function that computes the minimum and maximum values in a list.
     * The content of the Optional must be present if and only if the list is non-empty.
     */
    public static Function<List<Integer>, Optional<MinMaxResult>> computeMinMax() {
        // TODO
        // STUDENT return null;
        // BEGIN STRIP
        return lst -> {
            if (lst.isEmpty()) {
                return Optional.empty();
            } else {
                int minValue = lst.get(0);
                int maxValue = lst.get(0);
                for (Integer i : lst) {
                    minValue = Math.min(minValue, i);
                    maxValue = Math.max(maxValue, i);
                }
                return Optional.of(new MinMaxResult(minValue, maxValue));
            }
        };
        // END STRIP
    }

    /**
     * Return a function that, given a String object and a character, counts
     * the number of occurrences of the character inside the string.
     */
    public static Object countInstancesOfLetter() {
        // TODO
        // STUDENT return null;
        // BEGIN STRIP
        BiFunction<String, Character, Integer> f = (s, c) -> {
            int count = 0;
            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) == c) {
                    count++;
                }
            }
            return count;
        };
        return f;
        // END STRIP
    }
}
