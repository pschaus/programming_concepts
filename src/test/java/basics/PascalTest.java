package basics;// This file must *not* be modified!

import org.javagrader.Grade;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.math.BigDecimal;
import java.util.Random;


// END STRIP

public class PascalTest {
    
    // BEGIN STRIP
    private Random random = new Random(622322);
    // END STRIP
    
    @Test
    @Grade(value=1, cpuTimeout=100)
    public void testBasic() {    
        int [] row1 = new int[]{1};
        int [] row2 = new int[]{1, 1};
        int [] row3 = new int[]{1, 2, 1};
        int [] row4 = new int[]{1, 3, 3, 1};
        int [] row5 = new int[]{1, 4, 6, 4, 1};
        
        int [] r1 = Pascal.pascal(1);
        int [] r2 = Pascal.pascal(2);
        int [] r3 = Pascal.pascal(3);
        int [] r4 = Pascal.pascal(4);
        int [] r5 = Pascal.pascal(5);
        assertEquals(1, r1.length,"The first row is not of size 1");
        assertArrayEquals(row1, r1);
        assertEquals(2, r2.length, "The second row is not of size 2");
        assertArrayEquals(row2, r2);
        assertEquals( 3, r3.length, "The third row is not of size 3");
        assertArrayEquals(row3, r3);
        assertEquals(4, r4.length, "The fourth row is not of size 4");
        assertArrayEquals(row4, r4);
        assertEquals( 5, r5.length, "The fifth row is not of size 5");
        assertArrayEquals(row5, r5);
    }
    
    // BEGIN STRIP
    
    private BigDecimal [][] getPascalUntil(int n) {
        BigDecimal [][] pascal = new BigDecimal[n][];
        pascal[0] = new BigDecimal[]{ new BigDecimal(1) };
        pascal[1] = new BigDecimal[]{ new BigDecimal(1), new BigDecimal(1) };
        for (int i = 2; i < n; i++) {
           pascal[i] = new BigDecimal[pascal[i-1].length + 1];
           pascal[i][0] = new BigDecimal(1);
           pascal[i][pascal[i].length - 1] = new BigDecimal(1);
           for (int j = 1; j < pascal[i].length - 1; j++) {
               pascal[i][j] = pascal[i-1][j-1].add(pascal[i-1][j]);
           }
        }
        return pascal;
    }

    @Test
    @Grade(value=5, cpuTimeout=1000)
    public void testRandom() {
        BigDecimal [][] pascal = getPascalUntil(30); // Above 35, Java "int" type will overflow
        int [] queries = random.ints(1, pascal.length).distinct().limit(10).toArray();
        // int [] queries = random.ints(1, 11).distinct().limit(7).toArray();
        //int [] queries = IntStream.range(1, pascal.length).toArray();
        for (int query : queries) {
            int [] answer = Pascal.pascal(query);
            int expectedLength = pascal[query - 1].length;
            assertEquals(expectedLength, answer.length, "The row number " + query + " has not a size of " + expectedLength);
            for (int i = 0; i < answer.length; i++) {
                assertEquals(pascal[query - 1][i], new BigDecimal(answer[i]));
            }
        }
    }

    // END STRIP
}
