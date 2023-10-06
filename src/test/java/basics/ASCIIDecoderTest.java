package basics;

import org.javagrader.Grade;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Grade
public class ASCIIDecoderTest {
    @Test
    @Grade(value = 1)
    public void basicTest(){
        String [][] input = new String[][]{{"072" ,"101", "108", "108", "111", "032", "119", "111", "114", "108", "100"}};
        String [] res = ASCIIDecoder.decode(null, input);
        assertEquals(1, res.length);
        assertEquals(res[0], "Hello world");
    }


    @Test
    @Grade(value = 1)
    public void npeTest(){
        String [] [] input = new String[][]{{}};
        String [] res = ASCIIDecoder.decode(null, input);
        assertEquals(1, res.length);
        assertNotNull(res);
        assertNotNull(res[0]);
        assertEquals(0, res[0].length());
    }


    @Test
    @Grade(value = 1)
    public void biggerTest(){
        String [] [] input = new String[][]{{"084", "104", "105", "115", "032", "105", "115", "032"},
                                            {"097", "032", "108", "097", "114", "103", "101", "114"},
                                            {"115", "101", "116", "032", "111", "102", "032", "115", "101", "110",
                                                    "116", "101", "110", "099", "101", "115", "046", "046", "046"}};
        String [] res = ASCIIDecoder.decode(null, input);
        assertEquals(3, res.length);
        assertEquals(res[0], "This is ");
        assertEquals(res[1], "a larger");
        assertEquals(res[2], "set of sentences...");
    }


    @Test
    @Grade(value = 1)
    public void forbiddenCharsTest(){
        String [] [] input = new String[][]{{"084", "104", "105", "115", "032", "105", "115", "032"},
                {"097", "032", "108", "097", "114", "103", "101", "114"},
                {"115", "101", "116", "032", "111", "102", "032", "115", "101", "110",
                        "116", "101", "110", "099", "101", "115", "046", "046", "046"}};
        String [] res = ASCIIDecoder.decode(new int[]{32, 101}, input);
        assertEquals(3, res.length);
        assertEquals(res[0], "Thisis");
        assertEquals(res[1], "alargr");
        assertEquals(res[2], "stofsntncs...");
    }


    @Test
    @Grade(value = 1)
    public void forbiddenOnlyTest(){
        String [] [] input = new String[][]{{"097", "098", "099", "100", "101", "102", "103"},
                                            {"097", "098", "099", "100", "101", "102", "103"},
                                            {"097", "098", "099", "100", "101", "102", "104"}};
        String [] res = ASCIIDecoder.decode(new int[]{97, 98, 99, 100, 101, 102, 103}, input);
        assertEquals(3, res.length);
        assertNotNull(res[0]);
        assertNotNull(res[1]);
        assertNotNull(res[2]);
        assertEquals(res[0], "");
        assertEquals(res[1], "");
        assertEquals(res[2], "h");
    }

}
