package algorithms;

import org.javagrader.Grade;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Grade
public class FunctionRootTest {

    private class Function1 implements FunctionRoot.Function {

        public int getMinDomain() {
            return -10;
        }

        public int getMaxDomain() {
            return 10;
        }

        public int evaluates(int x) {
            if (x < -10 && x > 10) {
                throw new IllegalArgumentException("Querying the function on a value outside its domain");
            }
            return x;
        }
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void test1() {
        FunctionRoot.Function f = new Function1();
        assertEquals(0, FunctionRoot.findRoot(f));
    }

    // BEGIN STRIP

    private class FunctionStep implements FunctionRoot.Function {

        public int getMinDomain() {
            return -100;
        }

        public int getMaxDomain() {
            return 100;
        }

        public int evaluates(int x) {
            if (x < -100 && x > 100) {
                throw new IllegalArgumentException("Querying the function on a value outside its domain");
            }
            if (x == 10) {
                return 0;
            } else if (x < -40) {
                return -5;
            } else if (x < -20) {
                return -4;
            } else if (x < 10) {
                return -1;
            } else {
                return 100;
            }
        }

    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testStep() {
        FunctionRoot.Function f = new FunctionStep();
        assertEquals(10, FunctionRoot.findRoot(f));
    }

    private class FunctionMax implements FunctionRoot.Function {
        public int getMinDomain() {
            return -50;
        }

        public int getMaxDomain() {
            return 75;
        }

        public int evaluates(int x) {
            if (x < -50 && x > 75) {
                throw new IllegalArgumentException("Querying the function on a value outside its domain");
            }
            if (x == 75) {
                return 0;
            } else if (x < -20) {
                return -10;
            } else if (x < 0) {
                return -6;
            } else if (x < 50) {
                return -2;
            } else {
                return -1;
            }
        }

    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testMax() {
        FunctionRoot.Function f = new FunctionMax();
        assertEquals(75, FunctionRoot.findRoot(f));
    }

    private class FunctionMin implements FunctionRoot.Function {
        public int getMinDomain() {
            return -50;
        }

        public int getMaxDomain() {
            return 75;
        }

        public int evaluates(int x) {
            if (x < -50 && x > 75) {
                throw new IllegalArgumentException("Querying the function on a value outside its domain");
            }
            if (x == -50) {
                return 0;
            } else if (x < -20) {
                return 1;
            } else if (x < 0) {
                return 2;
            } else if (x < 50) {
                return 10;
            } else {
                return 20;
            }
        }

    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testMin() {
        FunctionRoot.Function f = new FunctionMin();
        assertEquals(-50, FunctionRoot.findRoot(f));
    }

    private class FunctionLargeDomain1 implements FunctionRoot.Function {
        public int getMinDomain() {
            return -10_000;
        }

        public int getMaxDomain() {
            return 10_000;
        }

        public int evaluates(int x) {
            if (x < -10_000 && x > 10_000) {
                throw new IllegalArgumentException("Querying the function on a value outside its domain");
            }
            if (x == 9375) {
                return 0;
            } else if (x < 9375) {
                return -10;
            } else {
                return 10;
            }
        }

    }

    @Test
    @Grade(value = 1, cpuTimeout = 10)
    public void testLarge1() {
        FunctionRoot.Function f = new FunctionLargeDomain1();
        assertEquals(9375, FunctionRoot.findRoot(f));
    }

    private class FunctionLargeDomain2 implements FunctionRoot.Function {
        public int getMinDomain() {
            return -10_000;
        }

        public int getMaxDomain() {
            return 10_000;
        }

        public int evaluates(int x) {
            if (x < -10_000 && x > 10_000) {
                throw new IllegalArgumentException("Querying the function on a value outside its domain");
            }
            if (x == -9375) {
                return 0;
            } else if (x < -9375) {
                return -10;
            } else {
                return 10;
            }
        }

    }

    @Test
    @Grade(value = 1, cpuTimeout = 10)
    public void testLarge2() {
        FunctionRoot.Function f = new FunctionLargeDomain2();
        assertEquals(-9375, FunctionRoot.findRoot(f));
    }

    // END STRIP

}
