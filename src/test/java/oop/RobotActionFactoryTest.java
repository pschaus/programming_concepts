package oop;// This file must *not* be modified!

import org.javagrader.Grade;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RobotActionFactoryTest {
    private static class RobotTest implements RobotActionFactory.Robot {
        private String result = new String();

        @Override
        public void moveForward() {
            result += "F";
        }

        @Override
        public void turnLeft() {
            result += "L";
        }

        @Override
        public void turnRight() {
            result += "R";
        }

        public String getResult() {
            return result;
        }
    }        
    
    private String execute(String[] commands) {
        RobotActionFactory f = new RobotActionFactory();
        RobotTest robot = new RobotTest();
        RobotActionFactory.Action action = f.parse(commands);
        action.apply(robot);
        return robot.getResult();
    }        
    
    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testNoAction() {
        // should only get the grade of no-action if single action works:
        assertEquals("F", execute(new String[] { "FORWARD" }));
        assertEquals("L", execute(new String[] { "LEFT" }));
        assertEquals("R", execute(new String[] { "RIGHT" }));

        assertEquals("", execute(new String[] { }));
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testSingleAction() {
        assertEquals("F", execute(new String[] { "FORWARD" }));
        assertEquals("L", execute(new String[] { "LEFT" }));
        assertEquals("R", execute(new String[] { "RIGHT" }));
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testSequence() {
        assertEquals("FRFLLF", execute(new String[] { "FORWARD",
                                                      "RIGHT",
                                                      "FORWARD",
                                                      "LEFT",
                                                      "LEFT",
                                                      "FORWARD" }));
    }
    
    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testNoRepeat() {

        assertEquals("R", execute(new String[] {
                                                "RIGHT",
                                                "REPEAT 0",
                                                "FORWARD",
                                                "RIGHT",
                                                "END REPEAT" }));
    }
   
    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testOneRepeat() {
        assertEquals("FR", execute(new String[] { "REPEAT 1",
                                                  "FORWARD",
                                                  "RIGHT",
                                                  "END REPEAT" }));
    }
   
    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testThreeRepeats() {
        assertEquals("FRFRFR", execute(new String[] { "REPEAT 3",
                                                      "FORWARD",
                                                      "RIGHT",
                                                      "END REPEAT" }));
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testStatement() {
        // Those are the commands from the example in "RobotActionFactory.java"
        assertEquals("FFRFRFRFF", execute(new String[] { "FORWARD",
                                                         "REPEAT 3",
                                                         "FORWARD",
                                                         "RIGHT",
                                                         "END REPEAT",
                                                         "FORWARD",
                                                         "FORWARD" }));
    }

    /*
    @Test(expected = IllegalArgumentException.class)
    @Grade(value = 1, cpuTimeout = 1000)
    public void testRepeatWithoutEnd() {
        // to get this test, you should at least have basic actions working
        assertEquals("FR", execute(new String[] { "FORWARD", "RIGHT",}));

        execute(new String[] { "REPEAT 1" });
    }
    
    @Test(expected = IllegalArgumentException.class)
    @Grade(value = 1, cpuTimeout = 1000)
    public void testUnknownAction() {
        // to get this test, you should at least have basic actions working
        assertEquals("FR", execute(new String[] { "FORWARD", "RIGHT",}));

        execute(new String[] { "FORWARD 100" });
    }

     */

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testNested() {
        assertEquals("FRFRFRLFRFRFRL", execute(new String[] { "REPEAT 2",
                                                              "REPEAT 3",
                                                              "FORWARD",
                                                              "RIGHT",
                                                              "END REPEAT",
                                                              "LEFT",
                                                              "END REPEAT" }));
    }

    // BEGIN STRIP
    static String repeatCharacter(char c, int times) {
        String s = new String();
        for (int i = 0; i < times; i++) {
            s += c;
        }
        return s;
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testRepeatTwice() {
        for (int i = 0; i < 5; i++) {
            String result = execute(new String[] { "REPEAT " + Integer.toString(i),
                                                   "FORWARD",
                                                   "END REPEAT",
                                                   "LEFT",
                                                   "REPEAT " + Integer.toString(i),
                                                   "RIGHT",
                                                   "END REPEAT" });
            assertEquals(repeatCharacter('F', i) + "L" + repeatCharacter('R', i), result);
        }
    }
    
    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testFinal() {
        assertEquals("FRFLFRFLFRFLFRFL", execute(new String[] { "REPEAT 4",
                                                                "REPEAT 1",
                                                                "FORWARD",
                                                                "REPEAT 1",
                                                                "RIGHT",
                                                                "REPEAT 1",
                                                                "FORWARD",
                                                                "LEFT",
                                                                "END REPEAT",
                                                                "END REPEAT",
                                                                "END REPEAT",
                                                                "END REPEAT" }));
    }
    // END STRIP
}
