package oop;


import java.util.Random;

import org.javagrader.Grade;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Grade
public class ExpressionTest {

// BEGIN STRIP
    private final Random r = new Random();
    private __CorrectExpression _currentExpr = null;
    private final double CMP_THRESHOLD = 0.0001;

    private enum Operator {
        PLUS,
        MINUS,
        MUL
    };

    private abstract class __CorrectExpression {
        
        public __CorrectExpression plus(__CorrectExpression r) {
            return new __BinaryExpr(this, r, Operator.PLUS);
        }

        public __CorrectExpression minus(__CorrectExpression r) {
            return new __BinaryExpr(this, r, Operator.MINUS);
        }

        public __CorrectExpression mul(__CorrectExpression r) {
            return new __BinaryExpr(this, r, Operator.MUL);
        }

        public abstract Expression __copyForStudents();
        public abstract double evaluate(double xValues);
        public abstract __CorrectExpression derivate();

    }

    private class __BinaryExpr extends __CorrectExpression {

        private __CorrectExpression left, right;
        private Operator op;

        public __BinaryExpr(__CorrectExpression left, __CorrectExpression right, Operator op) {
            this.left = (__CorrectExpression) left;
            this.right = (__CorrectExpression) right;
            this.op = op;
        }
        
        @Override
        public double evaluate(double xValue) {
            switch (this.op) {
                case PLUS:
                    return this.left.evaluate(xValue) + this.right.evaluate(xValue);
                case MINUS:
                    return this.left.evaluate(xValue) - this.right.evaluate(xValue);
                case MUL:
                    return this.left.evaluate(xValue) * this.right.evaluate(xValue);
            }
            throw new IllegalArgumentException();
        }

        @Override
        public __CorrectExpression derivate() {
            __CorrectExpression lderive = this.left.derivate();
            __CorrectExpression rderive = this.right.derivate();
            switch (this.op) {
                case PLUS:
                    return lderive.plus(rderive);
                case MINUS:
                    return lderive.minus(rderive);
                case MUL:
                    return lderive.mul(this.right).plus(this.left.mul(rderive));
            }
            throw new IllegalArgumentException();
        }

        @Override
        public Expression __copyForStudents() {
            switch (this.op) {
                case PLUS:
                    return this.left.__copyForStudents().plus(this.right.__copyForStudents());
                case MINUS:
                    return this.left.__copyForStudents().minus(this.right.__copyForStudents());
                case MUL:
                    return this.left.__copyForStudents().mul(this.right.__copyForStudents());
            }
            throw new IllegalArgumentException();
        }

    }

    private class __ValueExpr extends __CorrectExpression {

        private double value;

        public __ValueExpr(double value) {
            this.value = value;
        }

        @Override
        public double evaluate(double xValue) {
            return this.value;
        }

        @Override
        public __CorrectExpression derivate() {
            return new __ValueExpr(0.0);
        }

        @Override
        public Expression __copyForStudents() {
            return Expression.value(this.value);
        }

    }

    private class __XExpr extends __CorrectExpression {

        @Override
        public double evaluate(double xValue) {
            return xValue;
        }

        @Override
        public __CorrectExpression derivate() {
            return new __ValueExpr(1);
        }

        @Override
        public Expression __copyForStudents() {
            return Expression.x();
        }

    }

    private __CorrectExpression _x() {
        return new __XExpr();
    }

    private __CorrectExpression _value(double value) {
        return new __ValueExpr(value);
    }

    private __CorrectExpression _constructLit(String s) {
        if (s.equals("x"))
            return _x();
        return _value(Double.valueOf(s));
    }

    private __CorrectExpression _constructMul(String s) {
        String [] split = s.split("\\*");
        __CorrectExpression exp = null;
        for (String part : split)
            exp = exp == null ? _constructLit(part) : exp.mul(_constructLit(part));
        return exp;
    }

    private __CorrectExpression _constructAdd(String s) {
        String [] split = s.split("\\+");
        __CorrectExpression exp = null;
        for (String part : split) {
            __CorrectExpression expexp = null;
            String [] splitsplit = part.split("-");
            for (String partpart : splitsplit) {
                expexp = expexp ==  null ? _constructMul(partpart) : expexp.minus(_constructMul(partpart));
            }
            exp = exp == null ? expexp : exp.plus(expexp);
        }
        return exp;
    }

    private Expression _constructFromString(String s) {
        _currentExpr = _constructAdd(s);
        return _currentExpr.__copyForStudents();
    }


    private Expression _randomExpression(String [] ops, boolean includeX) {
        int nbOperands = r.nextInt(20) + 10;
        double [] numbers = new Random().doubles(nbOperands, 0.0, 100.0).toArray();
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < numbers.length - 1; i++) {
            s.append(numbers[i]);
            s.append(ops[r.nextInt(ops.length)]);
            if (includeX && r.nextDouble() <= 0.25) {
                s.append("x");
                s.append(ops[r.nextInt(ops.length)]);
            }
        }
        s.append(numbers[numbers.length - 1]);
        return _constructFromString(s.toString());
    }

// END STRIP
    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void test1() {

        Expression x = Expression.x();
        Expression four = Expression.value(4);

        // x^2+x+4
        Expression exp = x.mul(x).plus(x).plus(four);
        assertEquals(10,exp.evaluate(2),0.001);

        // 2x + 1
        Expression expPrime = exp.derivate();
        assertEquals(5,expPrime.evaluate(2),0.001);


    }

// BEGIN STRIP
    private void _compareDerivate(String msg, Expression actual) {
        __CorrectExpression expected = _currentExpr.derivate();
        for (double v : r.doubles(100).toArray())
            assertEquals(expected.evaluate(v), actual.evaluate(v), CMP_THRESHOLD, msg);
    }

    private void _compareEvaluate(String msg, Expression actual) {
        double value = r.nextDouble()*100;
        assertEquals(_currentExpr.evaluate(value), actual.evaluate(value), CMP_THRESHOLD, msg);
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testConstant() {
        double value = r.nextDouble()*100;
        Expression exp = _constructFromString(String.valueOf(value));
        _compareEvaluate("La methode evaluate ne fonctionne pas sur une valeur constante", exp);
        _compareDerivate("La méthode derivate ne fonctionne pas sur une valeur constante", exp.derivate());
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testX() {
        double value = r.nextDouble()*100;
        Expression exp = _constructFromString("x");
        _compareEvaluate("La méthode evaluate ne fonctionne pas sur l'expression x", exp); 
        _compareDerivate("La méthode derivate ne fonctionne pas sur l'expression x", exp.derivate());
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testAdd() {
        Expression exp = _randomExpression(new String[]{"+"}, true);
        double value = r.nextDouble()*100;
        _compareEvaluate("La méthode evaluate ne fonctionne pas sur une expression ne contenant que des +", exp);
        _compareDerivate("La méthode derivate ne fonctionne pas sur une expression ne contenant que des +", exp.derivate());
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testMinus() {
        Expression exp = _randomExpression(new String[]{"-"}, true);
        _compareEvaluate("La méthode evaluate ne fonctionne pas sur une expression ne contenant que des -", exp);
        _compareDerivate("La méthode derivate ne fonctionne pas sur une expression ne contenant que des -", exp.derivate());
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testMul() {
        Expression exp = _randomExpression(new String[]{"*"}, true);
        _compareEvaluate("La méthode evaluate ne fonctionne pas sur une expression ne contenant que des *", exp);
        _compareDerivate("La méthode derivate ne fonctionne pas sur une expression ne contenant que des *", exp.derivate());
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testRandom() {
        Expression exp = _randomExpression(new String[]{"+", "-", "*"}, true);
        _compareEvaluate("La méthode evaluate ne fonctionne pas sur une expression quelconque", exp);
        _compareDerivate("La méthode derivate ne fonctionne pas sur une expression quelconque", exp.derivate());
    }
// END STRIP
}
