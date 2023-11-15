package application;

import java.util.EmptyStackException;
import java.util.Stack;

/**
 * Evaluates an expression - the evaluation can be Standard (infix) or reverse polish.
 */
public class CalcModel implements Calculator {
    @Override
    public float evaluate(String expression, Boolean infix) throws InvalidExpression {
        try {

            // convert to postfix if necessary
            if (infix) {
                expression = infixToPostfix(expression);
//              System.out.println("updated expression is " + expression);
            }
//          System.out.println("expression is " + expression);
            // evaluate
            Stack<Float> stack = new Stack<>();
            for (String token : expression.split("\\s")) {
//              System.out.println("token is " + token);
                if (token.isEmpty()) continue;
                char c = token.charAt(0);
                if (isOperator(c)) {
                    float y = stack.pop();
                    float x = stack.pop();
                    stack.push(applyOp(c, x, y));
                } else {
                    stack.push(Float.parseFloat(token));
                }
            }

            return stack.pop();

        } catch (EmptyStackException | NumberFormatException e) {
            throw new InvalidExpression();
        }
    }

    private Float applyOp(char c, float x, float y) {
        if (c == '+') {
            return x + y;
        } else if (c == '-') {
            return x - y;
        } else if (c == '*') {
            return x * y;
        } else if (c == '/') {
            if (y == 0) {
                throw new UnsupportedOperationException("Cannot divide by zero");
            }
            return x / y;
        } else if (c == '^') {
            return (float) Math.pow(x, y);
        } else {
            throw new UnsupportedOperationException("Unknown operator");
        }
    }


    /**
     * Converts an infix expression to postfix notation.
     *
     * @param infix The infix expression to be converted.
     * @return The equivalent postfix expression.
     */
    String infixToPostfix(String infix) {
        StringBuilder postfixStr = new StringBuilder();
        Stack<Character> operatorsStack = new Stack<>();

        // Process each token in the infix expression
        for (String token : infix.split("\\s")) {
            if (token.isEmpty()) continue;
            char c = token.charAt(0);

            // Check if the token is an operator
            if (isOperator(c)) {
                if (operatorsStack.isEmpty()) {
                    operatorsStack.push(c);
                } else {
                    // Handle operators and their precedence
                    while (!operatorsStack.isEmpty()) {
                        char topOperator = operatorsStack.peek();
                        if (isOperator(topOperator) && precedence(c) <= precedence(topOperator)) {
                            postfixStr.append(operatorsStack.pop()).append(' ');
                        } else {
                            break;
                        }
                    }
                    operatorsStack.push(c);
                }
            }
            else if (c == '(') {
                operatorsStack.push('(');
            }
            else if (c == ')') {
                // Pop operators until '(' is encountered
                while (operatorsStack.peek() != '(') {
                    postfixStr.append(operatorsStack.pop()).append(' ');
                }
                operatorsStack.pop(); // Pop '('
            }
            else {
                postfixStr.append(token).append(' '); // Operand, append to the result
            }
        }

        // Pop any remaining operators from the stack
        while (!operatorsStack.isEmpty()) {
            postfixStr.append(operatorsStack.pop()).append(' ');
        }
        return postfixStr.toString();
    }

    /**
     * Checks if a given character is an operator.
     *
     * @param c The character to be checked.
     * @return True if the character is an operator, false otherwise.
     */
    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^';
    }

    /**
     * Determines the precedence of an operator.
     *
     * @param operator The operator whose precedence is to be determined.
     * @return The precedence value.
     */
    private int precedence(char operator) {
        String ops = "-+/*^";
        return ops.indexOf(operator) / 2;
    }
}
