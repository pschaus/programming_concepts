package basics;

import java.util.Random;


import org.javagrader.Grade;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


@Grade
public class PatternMatchingTest {

    // BEGIN STRIP
    private final Random r = new Random();

    private String _randomString() {
        int size = r.nextInt(10) + 5;
        return _randomString(size);
    }

    private String _randomString(int size) {
        String s = "";
        int [] chars = r.ints(97, 97+25).limit(size).toArray();
        for (int i : chars)
            s += (char) i;
        return s;
    }
    
    // END STRIP
    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void test1() {
        assertEquals(3, PatternMatching.find("Hello", "abcHello"));
        assertEquals(-1, PatternMatching.find("hello", "abcHello"));
        assertEquals(-1, PatternMatching.find("Hello", "abcHell"));
        assertEquals(0, PatternMatching.find("Hello", "HelloHelloHello"));
        assertEquals(4, PatternMatching.find("Hello", "elloHelloHello"));
    }

    // BEGIN STRIP
    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testBeginning() {
        String pattern = _randomString();
        String input = pattern + _randomString();
        assertEquals(0, PatternMatching.find(pattern, input),"La méthode ne retourne pas 0 quand le pattern est au début de l'input");
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testEnd() {
        String pattern = _randomString();
        String input = _randomString(pattern.length()*2).replace(pattern, "") + pattern;
        assertEquals(input.length() - pattern.length(), PatternMatching.find(pattern, input),"La méthode ne retourne pas le bon résultat quand le pattern est à la fin de l'input");
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testMultiple() {
        String pattern = _randomString();
        String input = _randomString() + pattern + _randomString() + pattern;
        assertEquals(input.indexOf(pattern), PatternMatching.find(pattern, input),"La méthode ne retourne pas le bon résultat lorsque le pattern est plusieurs fois dans l'input");
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testNotInt() {
        String pattern = _randomString();
        String input = _randomString(pattern.length()*2).replace(pattern, "");
        assertEquals(-1, PatternMatching.find(pattern, input),"La méthode ne retourne pas -1 lorsque le pattern n'est pas dans l'input");
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testPatternEmpty() {
        String pattern = "";
        String input = _randomString();
        assertEquals(0, PatternMatching.find(pattern, input),"La méthode ne fonctionne pas lorsque le pattern est le string vide");
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testInputEmpty() {
        String pattern = _randomString();
        String input = "";
        assertEquals( -1, PatternMatching.find(pattern, input),"La méthode ne fonctionne pas lorsque l'input est le string vide");
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testSame() {
        String pattern = _randomString();
        assertEquals(0, PatternMatching.find(pattern, pattern),"La méthode ne fonctionne pas lorsque le pattern est égal à l'input");
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testCaseSensitive() {
        String pattern = _randomString().toUpperCase();
        String input = _randomString().replace(pattern, "") + pattern.toLowerCase() + _randomString().replace(pattern, "");
        assertEquals(-1, PatternMatching.find(pattern, input),"La méthode n'est pas case-sensitive");
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testCaseSensitiveTwoTimes() {
        String pattern = _randomString().toLowerCase();
        String input = _randomString().replace(pattern, "") + pattern.toUpperCase() + _randomString().replace(pattern, "") + pattern + _randomString().replace(pattern, "");
        assertEquals(input.indexOf(pattern), PatternMatching.find(pattern, input));
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testPatternLarger() {
        String pattern = _randomString();
        String input = _randomString(pattern.length() - 1);
        assertEquals(-1, PatternMatching.find(pattern, input),"La méthode ne fonctionne pas lorsque le pattern est plus large que l'input");
    }
    // END STRIP

}
