package com.example.michael.calculator;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Created by Michael on 2/24/2016.
 */

/**
 * This class contains a public static method that evaluates an infix expression and
 * returns the result. Also contains a bunch of private helper methods.
 */
public class Calculator {
    /**
     * Compares the precedence between two operators.
     * @param op1 the first operator
     * @param op2 the second operator
     * @return true if the first operator has lower presedence than the second operator, false if not
     */
    private static boolean isLowerPrecedence(char op1, char op2){
        switch(op1) {
            case '+':
            case '-':
                return !(op2 == '+' || op2 == '-');

            case '*':
            case '/':
                return (op2 == '(' || op2 == '^');
            case '^':
                return op2 == '(';
            case '(':
                return true;
            default:
                return true;
        }

    }

    /**
     * Converts an infix expression to postfix.
     * We eplace unary '-'s to ! operators, so our evaluate method can tell the difference
     * between unary and binary '-'.
     * A space character is used as a separator between different numbers.
     * @param infix
     * @return
     */
    private  static String toPostfix(String infix){
        StringBuilder postfix = new StringBuilder();
        Deque<Character> stack = new ArrayDeque<>();
        int expressionLength = infix.length();
        for(int i = 0; i < expressionLength; i++){
            char token = infix.charAt(i);
            if (!Character.isDigit(token)){ //token is an operator
                if (token == '-' && (i == 0 || "+-/*(^".indexOf(infix.charAt(i - 1)) >= 0)){
                    //token is an unary minus
                    //We will use '!' to specify a unary '-'.
                    token = '!';
                }
                else if(Character.isLetter(token)){
                    //token is sin, cos, etc...
                    //use first letter to specify that unary operator
                    //since sin,cos, etc.. take up 3 letters instead of 1, move up an extra 2 spaces
                    i += 2;
                }

                    while (!stack.isEmpty() && !isLowerPrecedence(stack.peek(), token))
                        postfix.append(stack.pop());
                    if (token == ')') {
                        while (stack.peek() != '(')
                            postfix.append(stack.pop());
                        stack.pop(); //pop '('
                    } else
                        stack.push(token);


            }
            else {//token is an operand
                if (i == expressionLength - 1){
                    postfix.append(token);
                    postfix.append(' ');
                }
                else{
                    int end = i + 1;
                    token = infix.charAt(end);
                    while(Character.isDigit(token) || token == '.' || token == 'E'){
                        end++;
                        if(end == expressionLength) break;
                        token = infix.charAt(end);
                    }
                    postfix.append(infix.substring(i, end));
                    postfix.append(' ');
                    i = end - 1;

                }
            }


        }
        //If the last character in the postfix expression was not a right
        //parenthesis, we append a space so that the evaluate method
        //knows it is the end of a number.
        while(!stack.isEmpty())
            postfix.append(stack.pop());
        return postfix.toString();
    }

    /**
     * Evaluates a postfix expression and returns the result as a single double.
     * @param postfix the expression to evaluate, in postfix format.
     * @return the resulting double. Can be Infinite or NaN.
     * @throws InvalidExpressionException if the postfix expression is not valid.
     */
    private static double evaluate(String postfix) throws InvalidExpressionException{
        Deque<Double> stack = new ArrayDeque<>();
        int length = postfix.length();
        for (int i = 0; i < postfix.length(); i++){
            char token = postfix.charAt(i);
            if(Character.isDigit(token) || token == 'E') {
                //we have the beginning of the substring containing our number, but we
                //need to find the end
                int end = i + 1;
                while(postfix.charAt(end) != ' ')
                    end++;
             //   if (token == '!') //! needs to be replaced with negative sign
             //       stack.push(Double.parseDouble('-' + postfix.substring(i + 1, end)));
              //  else
                    stack.push(Double.parseDouble(postfix.substring(i, end)));
                i = end;
            }
            else
            {
                if(token == '!' && stack.size() > 0){
                    stack.push(-stack.pop());
                    continue;
                }
                else if(token == 's' && stack.size() > 0){
                    stack.push(Math.sin(stack.pop()));
                    continue;
                }
                else if(token == 'c' && stack.size() > 0){
                    stack.push(Math.cos(stack.pop()));
                    continue;
                }
                if(stack.size() < 2) throw new InvalidExpressionException("Too few operands for operator " + token);
                double x = stack.pop();
                double y = stack.pop();
                switch (token){
                    case '+':
                        stack.push(x + y);
                        break;
                    case '-':
                        stack.push(y - x);
                        break;
                    case '*':
                        stack.push(x * y);
                        break;
                    case '/':
                        stack.push(y / x);
                        break;
                    case '^':
                        stack.push(Math.pow(y, x));
                        break;
                    default:

                }
            }
        }
        if (stack.size() > 1) throw new InvalidExpressionException("Too many operands");
        return stack.pop();
    }

    /**
     * Evaluates an infix expression. It does this by first calling the toPostfix method to convert
     * infix to postfix, then the evaluate method to evaluate the resulting postfix expresson.
     * evaluate() will throw InvalidExpressionException if the postfix expression was not valid.
     * This is usually thrown when the infix param is an invalid expression, so we let the function
     * calling evaluate() handle instead. It is, however, possible that the it was thrown if some
     * of the code in this class is incorrect. That has not been the case so far, but it is something
     * to consider.
     * @param infix String containig the infix expression to evaluate.
     * @return The resulting double. Can be infinite or NaN.
     * @throws InvalidExpressionException if the expression was invalid.
     */
    public static double calculate(String infix) throws InvalidExpressionException{
        infix.replaceAll("sin", "s");
        infix.replaceAll("cos", "c");
        return evaluate(toPostfix(infix));
    }



}




