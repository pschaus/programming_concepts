package complexity;


import org.javagrader.Forbid;
import org.javagrader.Grade;
import org.javagrader.GradeFeedback;
import org.junit.jupiter.api.Test;

import static org.javagrader.TestResultStatus.FAIL;
import static org.junit.jupiter.api.Assertions.assertEquals;


@Grade
public class AdderTest {

    @Test
    @Grade(value = 5, cpuTimeout = 1)
    @Forbid("java.lang.Thread")
    void mytest1() {
        // this works
        assertEquals(1, Adder.add(0, 1));
    }

    @Test
    @Grade(value = 3)
    @GradeFeedback(message = "You forgot to consider this particular case [...]", on = FAIL)
    void mytest2() {
        // this doesn't
        assertEquals(2, Adder.add(1,1));
    }

}