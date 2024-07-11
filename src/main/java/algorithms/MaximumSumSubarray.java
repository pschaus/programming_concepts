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
     * Finds the contiguous sub-array for which the sum of its elements is maximal.
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
        // The first element is the basic solution
        ArrayIndex subarray = new ArrayIndex(0, 0);
        // The accumulated sum in the currently considered sub-array
        int currentSum = array[0];
        // The greatest sum we have seen so far
        int maximalSeenSum = Integer.MIN_VALUE;
        // Start of the currently considered sub-array
        int start = 0;
        for (int end = 1; end < array.length; end++) {
            // Extends the current sub-array with the next element
            currentSum += array[end];
            // We have an array with a sum greater than the maximum we have seen !
            // Update the solution and the maximum
            if (currentSum > maximalSeenSum) {
                maximalSeenSum = currentSum;
                subarray.start = start;
                subarray.end = end;
            }
            // If the sum of the currently considered sub-array is less than 0, then the next element either
            //      1) is negative, and decrease the sum of the sub-array if we extend to it
            //      2) Is positive and it will increase the sum of the sub-array. But taking the positive element alone
            //         yields a sub-array with a better sum
            // Thus we can stop extending this sub-array and starting one from the next index.
            // If the sum, is positive then we must extend the array to the next element. The best sub-array
            // can not start at the next element since the current sub-array has a positive sum.
            if (currentSum < 0) {
                currentSum = 0;
                start = end + 1;
            }
        }
        return subarray;
        // END STRIP
    }
}
