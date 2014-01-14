package pa3;

import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PostfixEvaluator {
    // cLASS TO REPORT A SYNTAX ERROR. 

    public static class SyntaxErrorException extends Exception {
        //Conctruct a SyntaxErrorException with the specified message.

        SyntaxErrorException(String message) {
            super(message);
        }
    }
    private static final String OPERATORS = "+_*/%^";
    private Stack<Integer> operandStack;

    private int evalOp(char op) {
        int rhs = operandStack.pop();
        int lhs = operandStack.pop();
        int result = 0;
        switch (op) {
            case '+':
                result = lhs + rhs;
                break;
            case '-':
                result = lhs - rhs;
                break;
            case '/':
                result = lhs / rhs;
                break;
            case '*':
                result = lhs * rhs;
                break;
            case '%':
                result = lhs % rhs;
                break;
            case '^':
                result = lhs;
                for (int i = 0; i < rhs - 1; i++) {
                    result *= lhs;
                }
                break;
        }
        return result;
    }

    private boolean isOperator(char ch) {
        return OPERATORS.indexOf(ch) != -1;
    }

    public int eval(String expression) throws SyntaxErrorException {
        operandStack = new Stack<>();
        String[] tokens = expression.split("\\s+");
        try {
            for (String nextToken : tokens) {
                char firstChar = nextToken.charAt(0);
                if (Character.isDigit(firstChar)) {
                    int value = Integer.parseInt(nextToken);
                    operandStack.push(value);
                } else if (isOperator(firstChar)) {
                    int result = evalOp(firstChar);
                    operandStack.push(result);
                } else {
                    throw new SyntaxErrorException("Invalid character encountered: " + firstChar);
                }
            }
            int answer = operandStack.pop();
            if (operandStack.empty()) {
                return answer;
            } else {
                throw new SyntaxErrorException("Syntax Error: Stack should be empty");
            }
        } catch (Exception e) {
            throw new SyntaxErrorException("Syntax Error: Stack should be empty");
        }
    }
}