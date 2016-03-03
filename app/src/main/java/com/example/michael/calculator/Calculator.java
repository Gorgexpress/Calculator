package com.example.michael.calculator;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Created by Michael on 2/24/2016.
 */

public class Calculator {
    private static boolean isLowerPrecedence(char op1, char op2){
        switch(op1) {
            case '+':
            case '-':
                return !(op2 == '+' || op2 == '-');

            case '*':
            case '/':
                return op2 == '(';
            case '(':
                return true;
            default:
                return false;
        }

    }
    private  static String toPostfix(String infix){
        StringBuilder postfix = new StringBuilder();
        Deque<Character> stack = new ArrayDeque<>();
        int expressionLength = infix.length();
        for(int i = 0; i < expressionLength; i++){
            char token = infix.charAt(i);
            if (token < '0'){ //token is either an operator
                if (token == '-' && (i == 0 || "+-/*(^".indexOf(infix.charAt(i - 1)) >= 0)){
                    //token is an unary operator
                    //We assume the only unary operator allowed is '-'.
                    //We will use '!' to specify a unary '-'.
                    stack.push('!');
                }
                else {
                    while (!stack.isEmpty() && !isLowerPrecedence(stack.peek(), token))
                        postfix.append(stack.pop());
                    if (token == ')') {
                        while (stack.peek() != '(')
                            postfix.append(stack.pop());
                        stack.pop(); //pop '('
                    } else
                        stack.push(token);
                }

            }
            else {//token is an operand
                if (i == expressionLength - 1){
                    postfix.append(token);
                    postfix.append(' ');
                }
                else{
                    int end = i + 1;
                    token = infix.charAt(end);
                    while(token >= '0' || token == '.'){
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
    private static double evaluate(String postfix) throws InvalidExpressionException{
        Deque<Double> stack = new ArrayDeque<>();
        int length = postfix.length();
        for (int i = 0; i < postfix.length(); i++){
            char token = postfix.charAt(i);
            if(token >= '0' && token <= '9') {
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
                    default:

                }
            }
        }
        if (stack.size() > 1) throw new InvalidExpressionException("Too many operands");
        return stack.pop();
    }

    public static double calculate(String infix) throws InvalidExpressionException{
        return evaluate(toPostfix(infix));
    }



}




