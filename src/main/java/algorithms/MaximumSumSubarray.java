package algorithms;

public class MaximumSumSubarray {
    
    /**
     * Class representing a sub-array in an array. It is defined by the start
     * and end position (both inclusive) of the sub-array in the array
     */
    public static class ArrayIndex {
        int start;
        int end;

        public ArrayIndex(int start, int end) {
            this.start = start;
            this.end = end;
        }
        
        @Override
        public boolean equals(Object other) {
            if (other instanceof ArrayIndex) {
                ArrayIndex o = (ArrayIndex) other;
                return o.start == this.start && o.end == this.end;
            }
            return false;
        }
        
        @Override
        public String toString() {
            return String.format("(%d, %d)", this.start, this.end);
        }
    }
    
    /**
     * Finds the contiguous non-empty sub-array for which the sum of its elements is maximal.
     * If multiple sub-arrays have the same maximal sum, returns the one that starts at
     * the lowest index.
     * 
     * For example, in the sub-array [1 ,1 , 3, -10, 3, 4, -5, -3, 2, 1], the methods returns
     * (4, 5).
     * 
     * @param array A non-empty array filled with non-zero integers (which might be negative)
     * @return The position of the array for which the sum of its element is maximal (if there
     *          is a tie, the one that starts the earliest is returned)
     */
    public static ArrayIndex maximumSumSubarray(int [] array) {
        // STUDENT return new ArrayIndex(-1, -1);
        // BEGIN STRIP
        ArrayIndex subarray = new ArrayIndex(0, 0);
        int currentSum = array[0];
        int maxSum = Integer.MIN_VALUE;
        int start = 0;
        int end = 1;

        // Loop Invariant:
        // start = argmax_{i < end} sum(array[i:end-1])
        // currentSum = sum(array[start:end-1])
        // maxSum is the largest sum subarray on the interval [0, end - 1]
        // and subarray is the subarray that achieves this sum
        while (end < array.length) {
            if (array[end] > currentSum + array[end]) {
                currentSum = array[end];
                start = end;
            } else {
                currentSum += array[end];
            }
            if (currentSum > maxSum) {
                maxSum = currentSum;
                subarray.start = start;
                subarray.end = end;
            }
            end++;
        }
        return subarray;
        // END STRIP
    }
}
