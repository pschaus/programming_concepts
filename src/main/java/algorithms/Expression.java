package algorithms;

/**
 * This class can be used to build simple arithmetic expression
 * with binary operator +,-,* and involving one variable 'x'.
 *
 * The expression can be
 * 1) evaluated by replacing the variable x with a specific value
 * 2) derivated to obtain a new expression
 *
 * You must modify this class to make it work
 * You can/should extend this class with inner classes the way you want.
 * You can also modify it but you are not allowed to modify the signature
 * of existing methods
 *
 * As a reminder, the formulas for the derivations as are followed
 *  - (f + g)' = f' + g'
 *  - (f*g)' = f'g + fg'
 *  - (x)' = 1
 *  - (C)' = 0 with C a constant
 */
public abstract class Expression {

    /**
     * Creates the basic variable expression 'x'
     * @return the expression 'x'
     */
    public static Expression x() {
        // STUDENT return null;
        // BEGIN STRIP
        return X.INSTANCE;
        // END STRIP
    }

    /**
     * Creates the basic constant expression 'v'
     * @return the expression 'v'
     */
    public static Expression value(double v) {
        // STUDENT return null;
        // BEGIN STRIP
        return new Value(v);
        // END STRIP
    }

    /**
     * Creates the binary expression 'this + r'
     * @param r the right operator
     * @return the binary expression 'this + r'
     */
    public Expression plus(Expression r) {
        // STUDENT return null;
        // BEGIN STRIP
        return new BinaryExpression('+',this,r);
        // END STRIP
    }

    /**
     * Creates the binary expression 'this - r'
     * @param r the right operator
     * @return the binary expression 'this - r'
     */
    public Expression minus(Expression r) {
        // STUDENT return null;
        // BEGIN STRIP
        return new BinaryExpression('-',this,r);
        // END STRIP
    }

    /**
     * Creates the binary expression 'this * r'
     * @param r the right operator
     * @return the binary expression 'this * r'
     */
    public Expression mul(Expression r) {
        // STUDENT return null;
        // BEGIN STRIP
        return new BinaryExpression('*',this,r);
        // END STRIP
    }

    /**
     * Evaluate the expression with fixed value for x
     * @param xValue the value taken by x for the evaluation
     * @return the evaluation of the expression considering x=xValue
     */
    public abstract double evaluate(double xValue);

    /**
     * Derivate the expression wrt to 'x'
     * @return the derivative of the expression with respect to 'x'
     */
    public abstract Expression derivate();



    // BEGIN STRIP
    private static class BinaryExpression extends Expression {

        private final Expression left;
        private final Expression right;
        private final char operator;

        private BinaryExpression(char op, Expression l, Expression r) {
            this.operator = op;
            this.left = l;
            this.right = r;
        }

        @Override public double evaluate(double xValue) {
            double leftRes = left.evaluate(xValue);
            double rightRes = right.evaluate(xValue);
            switch (operator) {
                case '+':
                    return leftRes + rightRes;
                case '-':
                    return leftRes - rightRes;
                case '*':
                    return leftRes * rightRes;
                default:
                    throw new IllegalArgumentException("unkown operator " + operator);
            }

        }

        @Override
        public Expression derivate() {
                Expression leftPrime = left.derivate();
                Expression rightPrime = right.derivate();
                switch (operator) {
                    case '+':
                        return leftPrime.plus(rightPrime);
                    case '-':
                        return leftPrime.minus(rightPrime);
                    case '*':
                        return leftPrime.mul(right).plus(left.mul(rightPrime));
                    default:
                        throw new IllegalArgumentException("unkown operator " + operator);
                }


        }
    }

    private static class Value extends Expression {

        private final double value;

        private Value(double v) {
            this.value = v;
        }

        @Override
        public double evaluate(double xValue) {
            return  value;
        }

        @Override
        public Expression derivate() {
            return value(0);
        }
    }

    private static class X extends Expression {

        private static final X INSTANCE = new X();

        private X() {
        }

        @Override
        public double evaluate(double xValue) {
            return xValue;
        }

        @Override
        public Expression derivate() {
            return value(1);
        }

    }
    // END STRIP


}
