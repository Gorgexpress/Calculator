package com.example.michael.calculator;

import android.test.ActivityTestCase;

import org.junit.Test;

import dalvik.annotation.TestTargetClass;

import static org.junit.Assert.assertEquals;

/**
 * Created by Michael on 3/4/2016.
 */
public class CalculatorTest extends ActivityTestCase {
    private final double epsilon = 0.0001;

    @Test
    public void testAdd() {
        String expression = "4+5";
        assertEquals(9.0, Calculator.calculate(expression), epsilon);
    }

    @Test
    public void testSub() {
        String expression = "10-5";
        assertEquals(5, Calculator.calculate(expression), epsilon);
    }

    @Test
    public void testMul() {
        String expression = "3*5";
        assertEquals(15.0, Calculator.calculate(expression), epsilon);
    }

    @Test
    public void testDiv() {
        String expression = "3/5";
        assertEquals(0.6, Calculator.calculate(expression), epsilon);
    }

    @Test
    public void testExponent() {
        String expression = "2^5";
        assertEquals(32.0, Calculator.calculate(expression), epsilon);
    }
    @Test
    public void testUnarySub() {
        String expression = "4*-5";
        assertEquals(-20.0, Calculator.calculate(expression), epsilon);
        expression = "4-(-5)";
        assertEquals(9.0, Calculator.calculate(expression), epsilon);
    }

    @Test
    public void testParenthesis() {
        String expression = "(3+5)*2";
        assertEquals(16.0, Calculator.calculate(expression), epsilon);
    }

    public void testDecimal() {
        String expression = "3.5+1.2";
        assertEquals(4.7, Calculator.calculate(expression), epsilon);
        expression = "0.1-1.2";
        assertEquals(-1.1, Calculator.calculate(expression), epsilon);
    }

    @Test
    public void complex1() {
        String expression = "-((3+5)*2/-1)";
        assertEquals(16.0, Calculator.calculate(expression), epsilon);
    }

    @Test
    public void complex2() {
        String expression = "(-1.2*5.2)-((5.45+3.3)*0.2)/1.33";
        assertEquals(-7.555789, Calculator.calculate(expression), epsilon);
    }

    @Test
    public void complex3() {
        String expression = "-(((((((2.6554/-6.1)*0.2)*-5.5)/1.1)+2.56*(3.1-2.1))-2)*1)";
        assertEquals(-0.995311, Calculator.calculate(expression), epsilon);
    }

    @Test(expected = InvalidExpressionException.class)
    public void invalid1() {
        //too many right parenthesis
        String expression = "(5-1))";
        Calculator.calculate(expression);
    }

    @Test(expected = InvalidExpressionException.class)
    public void invalid2() {
        //too many operators
        String expression = "5+3+*5";
        Calculator.calculate(expression);
    }

    @Test(expected = InvalidExpressionException.class)
    public void invalid3() {
        //too many operands
        String expression = "(5)+5(5)";
        Calculator.calculate(expression);
    }

    @Test
     public void infinite1() {
        String expression = "9999999999999^9999999999";
        double result = Calculator.calculate(expression);
        assert(Double.isInfinite(result));
    }

    @Test
    public void infinite2() {
        String expression = "9999999999999^-9999999999";
        double result = Calculator.calculate(expression);
        assert(Double.isInfinite(result));
    }

    @Test
    public void NaN() {
        String expression = "5/0";
        double result = Calculator.calculate(expression);
        assert(Double.isNaN(result));
    }
}
