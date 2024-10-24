package algorithms;

import java.util.Random;

public class SumComplexity {

    public static int sum(int[] values) {
        int total = 0;
        for (int i = 0; i < values.length; i++) {
            total += values[i];
        }
        return total;
    }

    public static void main(String[] args) {
        // Define different sizes of input
        int[] inputSizes = {1 << 1, 1 << 2, 1 << 3, 1 << 4, 1 << 5, 1 << 6, 1 << 7, 1 << 8, 1 << 9, 1 << 10, 1 << 11, 1 << 12, 1 << 13, 1 << 14, 1 << 15, 1 << 16, 1 << 17, 1 << 18, 1 << 19, 1 << 20, 1 << 21, 1 << 22, 1 << 23, 1 << 24, 1 << 25, 1 << 26, 1 << 27, 1 << 28, 1 << 29, 1 << 30};

        for (int size : inputSizes) {
            // Generate random input array of the current size
            int[] values = generateRandomArray(size);

            // Measure wall clock time for the sum function
            long startTime = System.nanoTime();
            int result = sum(values);
            long endTime = System.nanoTime();

            // Calculate elapsed time in milliseconds
            long durationInMillis = (endTime - startTime) / 1_000_000;
            int a = Integer.MAX_VALUE;

            System.out.println("n " + size + " time " + durationInMillis + " ms");
        }
    }

    // Helper method to generate a random array of a given size
    public static int[] generateRandomArray(int size) {
        Random random = new Random();
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(100);  // Generate random numbers between 0 and 100
        }
        return array;
    }
}