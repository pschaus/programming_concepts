package basics;

import org.javagrader.Grade;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.function.Supplier;
import java.util.stream.Stream;

// BEGIN STRIP
import java.util.Random;
// END STRIP

@Grade
public class StringUtilsTest {
// BEGIN STRIP
    private char [] markers = {'-',' ', '=', ','};
    private String [] words = {"hello", "world", "test"};

    private Supplier<String []> rsg_split = () -> { // return a random number of repeated  identical string and a marker

        StringBuilder sb = new StringBuilder();
        int nOcc = (int) (Math.random()*30)+2;
        int marker = (int) (Math.random() * 4);

        for(int i = 0; i < nOcc; i++){
            sb.append(words[(int) (Math.random()*3)]);
            sb.append(markers[marker]);
        }
        String val = sb.toString();
        return new String[]{val.substring(0, val.length()-1), String.valueOf(markers[marker])};

    };


    private Supplier<String> rsg = () -> {

        // source : https://www.baeldung.com/java-random-string

        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);

        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }

        return buffer.toString();

    };
// END STRIP

    @Test
    @Grade(value = 1)
    public void test1(){
        String init = "A.bcd.e.";
        char marker = '.';
        String [] expected = {"A", "bcd", "e",""};
        String [] result = StringUtils.split(init, marker);
        assertArrayEquals(expected, result);
    }

    @Test
    @Grade(value = 1)
    public void test2(){
        String init = "Abcdef";
        char marker = '.';
        String [] expected = {"Abcdef"};
        String [] result = StringUtils.split(init, marker);
        assertArrayEquals(expected, result);
    }

    @Test
    @Grade(value = 1)
    public void test3(){
        String init = "Abcd";
        String pattern = "bc";
        int expected = 1;
        int result = StringUtils.indexOf(init, pattern);
        assertEquals(expected, result);
        pattern = "z";
        expected = -1;
        result = StringUtils.indexOf(init, pattern);
        assertEquals(expected, result);
        pattern = "Abcd";
        expected = 0;
        result = StringUtils.indexOf(init, pattern);
        assertEquals(expected, result);
    }

    @Test
    @Grade(value = 1)
    public void test4(){
        String upper = "heLlo WOrlD";
        String lower = "hello world";
        assertEquals(lower, StringUtils.toLowerCase(upper));
    }

    @Test
    @Grade(value = 1)
    public void test5(){
        String palindrome = "abba";
        assertTrue(StringUtils.palindrome(palindrome));
        palindrome = "abbaa";
        assertFalse(StringUtils.palindrome(palindrome));
        palindrome = "bab";
        assertTrue(StringUtils.palindrome(palindrome));
        palindrome = "baba";
        assertFalse(StringUtils.palindrome(palindrome));
    }

// BEGIN STRIP
    @Test
    @Grade(value = 1)
    public void testLowerCase(){

        String [] seeds = Stream.generate(rsg).limit(10).toArray(String[]::new);


        for(String seed : seeds){
            String s = seed.toUpperCase();
            assertEquals(StringUtils.toLowerCase(s), s.toLowerCase());
        }

        assertEquals(StringUtils.toLowerCase("HelLo WorLd"), "HelLo WorLd".toLowerCase());
        assertEquals(StringUtils.toLowerCase("Hello World "), "Hello World ".toLowerCase());


    }


    @Test
    @Grade(value = 1)
    public void testPalindrome(){

        String [] seeds = Stream.generate(rsg).limit(10).toArray(String[]::new);

        for(String seed : seeds){
            String reversed = new StringBuilder(seed.substring(0, seed.length()-1)).reverse().toString();
            seed = seed.concat(reversed);
            assertTrue(StringUtils.palindrome(seed));
            seed = seed.concat("#");
            // the '#' char will never be generated, by concatenating it to a palindrome we're sure that seed is not
            // a palindrome anymore
            assertFalse(StringUtils.palindrome(seed));
        }

        assertTrue(StringUtils.palindrome("a")); // base case, a size 1 string should be considered a palindrome

    }


    @Test
    @Grade(value = 1)
    public void testSplit() {
        String[][] seeds = Stream.generate(rsg_split).limit(10).toArray(String[][]::new);

        for (String[] seed : seeds) {
            String[] splitJDK = seed[0].split(seed[1]);
            String[] splitStd = StringUtils.split(seed[0], seed[1].charAt(0));
            assertArrayEquals(splitJDK, splitStd);
        }
    }


    @Test
    @Grade(value = 1)
    public void testIndexOf(){

        String [] seeds = Stream.generate(rsg).limit(10).toArray(String[]::new);

        for(String seed : seeds){

            String begin = seed.substring(0, 2);
            String end = seed.substring(seed.length()-2);
            String middle = seed.substring(3, 6);
            assertEquals(seed.indexOf(begin), StringUtils.indexOf(seed, begin));
            assertEquals(seed.indexOf(end), StringUtils.indexOf(seed, end));
            assertEquals(seed.indexOf(middle), StringUtils.indexOf(seed, middle));

        }

        assertEquals(-1, StringUtils.indexOf("Test", "z"));
        assertEquals(-1, StringUtils.indexOf("test", "T"));
    }
// END STRIP
}
