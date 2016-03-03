package com.example.michael.calculator;

/**
 * Created by Michael on 2/25/2016.
 */
public class InvalidExpressionException extends ArithmeticException {
    public InvalidExpressionException(String message) {
        super(message);
    }
}