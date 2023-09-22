package complexity;

import org.javagrader.Grade;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// BEGIN STRIP
import java.util.Random;
import java.util.Arrays;
// END STRIP

@Grade
public class MaximumSumSubarrayTest {
    
    // BEGIN STRIP

    private final Random random = new Random(366359);
    private final int arraySizeLower = 20;
    
    // END STRIP
    
    @Test
    @Grade(value = 1)
    public void testSimple() {
        int [] array = new int[]{1 ,1 , 3, -10, 3, 4, -5, -3, 2, 1};
        
        MaximumSumSubarray.ArrayIndex solution = MaximumSumSubarray.maximumSumSubarray(array);
        assertEquals(4, solution.start);
        assertEquals(5, solution.end);
    }
    
    // BEGIN STRIP
    
    private int[] generateArray() {
        int size = arraySizeLower + random.nextInt(arraySizeLower);
        int [] array = new int[size];
        for (int i = 0; i < array.length; i++ ) {
            if (random.nextBoolean()) {
                array[i] = 1 + random.nextInt(20);
            } else {
                array[i] = -1 - random.nextInt(20);
            }
        }
        return array;
    }
    
    @Test
    @Grade(value = 1)
    public void testSubArrayAtBeginning() {
        int [] array = generateArray();
        Arrays.sort(array);
        for (int i = 0; i < array.length / 2; i++) {
            int tmp = array[i];
            array[i] = array[array.length - 1 - i];
            array[array.length - 1 - i] = tmp;
        }
        int end = 1;
        while (end < array.length && array[end] >= 0) {
            end += 1;
        }
        assertEquals(new MaximumSumSubarray.ArrayIndex(0, end - 1), MaximumSumSubarray.maximumSumSubarray(array));
    }

    @Test
    @Grade(value = 1)
    public void testSubArrayAtEnd() {
        int [] array = generateArray();
        Arrays.sort(array);
        int start = 0;
        while (start < array.length - 1 && array[start] < 0) {
            start += 1;
        }
        assertEquals(new MaximumSumSubarray.ArrayIndex(start, array.length - 1), MaximumSumSubarray.maximumSumSubarray(array));
    }

    @Test
    @Grade(value = 1)
    public void testSubArrayValue() {
        int [] array = generateArray();
        MaximumSumSubarray.ArrayIndex subarray = MaximumSumSubarray.maximumSumSubarray(array);
        int sum = 0;
        for (int i = subarray.start; i <= subarray.end; i++) {
            sum += array[i];
        }
        int maximum = Integer.MIN_VALUE;
        // This array contains for every possible subarray the value of the sums of its elements.
        // First index gives the index of start
        // Second index gives the size of the subarray
        int [][] subArraysSums = new int[array.length][array.length];
        for (int i = 0; i < array.length; i++) {
            subArraysSums[i][0] = array[i];
            if (array[i] > maximum) {
                maximum = array[i];
            }
        }
        for (int start = 0; start < array.length; start++) {
            for (int size = 2; size + start < array.length; size ++) {
                subArraysSums[start][size - 1] = subArraysSums[start][size - 2] + subArraysSums[start + size - 1][0];
                if (subArraysSums[start][size - 1] > maximum) {
                    maximum = subArraysSums[start][size - 1];
                }
            }
        }
        assertEquals(maximum, sum);
    }
    
    @Test
    @Grade(value = 1)
    public void testSubArrayIndex() {
        int [] array = generateArray();
        MaximumSumSubarray.ArrayIndex subarray = MaximumSumSubarray.maximumSumSubarray(array);
        int maximum = Integer.MIN_VALUE;
        MaximumSumSubarray.ArrayIndex solution = new MaximumSumSubarray.ArrayIndex(0, 0);
        // This array contains for every possible subarray the value of the sums of its elements.
        // First index gives the index of start
        // Second index gives the size of the subarray
        int [][] subArraysSums = new int[array.length][array.length];
        for (int i = 0; i < array.length; i++) {
            subArraysSums[i][0] = array[i];
            if (array[i] > maximum) {
                maximum = array[i];
                solution.start = i;
                solution.end = i;
            }
        }
        for (int start = 0; start < array.length; start++) {
            for (int size = 2; size + start < array.length; size ++) {
                subArraysSums[start][size - 1] = subArraysSums[start][size - 2] + subArraysSums[start + size - 1][0];
                if (subArraysSums[start][size - 1] > maximum) {
                    maximum = subArraysSums[start][size - 1];
                    solution.start = start;
                    solution.end = start + size - 1;
                }
            }
        }
        assertEquals(solution, subarray);
    }
    
    @Test
    @Grade(value = 1, cpuTimeout=100)
    public void testComplexity() {
        int [] array = new int[1_000_000];
        for (int i = 0; i < array.length; i++) {
            if (i % 2 == 0) {
                array[i] = 1+ random.nextInt(20);
            } else {
                array[i] = -1 - random.nextInt(20);
            }
        }
        MaximumSumSubarray.maximumSumSubarray(array);
    }

    // END STRIP
    
}
