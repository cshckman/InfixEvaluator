package pa3;

import java.util.EmptyStackException;
import java.util.Scanner;
import java.util.Stack;

public class Calculator {

    private Stack<Character> operatorStack;
    private Stack<Double> operandStack;
    private static final String OPERATORS = "+-*/%^(){}[]";
    private static final int[] PRECEDENCE = {1, 1, 2, 2, 2, 3, -1, -1, -1, -1, -1, -1};
    boolean error = false;

    //Convert a string from infix to postfix.
    public double convert(String infix) {
        operatorStack = new Stack<>();
        operandStack = new Stack<>();
        double result;
        try {
            String nextToken;
            Scanner s = new Scanner(infix);
            while ((nextToken = s.findInLine("[\\p{L}\\p{N}\\.]+|[+-/\\*()\\^%{}\\[\\]]")) != null) {
                char firstChar = nextToken.charAt(0);
                if (Character.isDigit(firstChar)) {
                    operandStack.push(Double.parseDouble(nextToken));
                } else if (isOperator(firstChar)) { //Is it an operator?
                    processOperator(firstChar);
                }
            }
        } catch (EmptyStackException ex) {
            System.out.println("That is not a valid expression.");
            error = true;
        }
        //Pop any remaining operators and append them to postfix.
        while (!operatorStack.empty()) {
            char op = operatorStack.pop();

            if (op == '+' || op == '-' || op == '*' || op == '/' || op == '^' || op == '%') {
                evalOp(op);
            }
            if (op == '(' || op == '{' || op == '[') {
                System.out.println("Parenthetical Error");
                error = true;
                break;
            }
        }
        result = operandStack.pop();
        //Once the stack is empty, return the final result.
        return result;
    }

    //Method to process operators
    private void processOperator(char op) {
        if (operatorStack.empty() || op == '(' || op == '{' || op == '[') {
            operatorStack.push(op);
        } else {
            char topOp = operatorStack.peek();
            if (precedence(op) > precedence(topOp)) {
                operatorStack.push(op);
            } else { //Pop all stacked operators with equal or higher precedence than op.
                while (!operatorStack.empty() && precedence(op) <= precedence(topOp)) {
                    if (topOp == '(' && op != ')' || topOp == '{' && op != '}' || topOp == '[' && op != ']') {
                        error = true;
                        break;
                    }
                    operatorStack.pop();
                    if (topOp == '(' || topOp == '{' || topOp == '[') {
                        break;
                    }
                    evalOp(topOp);

                    if (!operatorStack.empty()) {
                        topOp = operatorStack.peek();
                    }
                }
                if (op != ')' && op != '}' && op != ']') {
                    //Reset topOp.
                    operatorStack.push(op);
                }
            }
        }
    }

    //Determine whether a character is an operator.
    private boolean isOperator(char ch) {
        return OPERATORS.indexOf(ch) != -1;
    }

    //Determines the precedence of an operator.
    private int precedence(char op) {
        return PRECEDENCE[OPERATORS.indexOf(op)];
    }

    private void evalOp(char op) {
        double rhs = 0;
        double lhs = 0;
        try {
            rhs = operandStack.pop();
            lhs = operandStack.pop();
        } catch (EmptyStackException e) {
            System.out.println("Please enter a proper expression");
            error = true;
        }
        double result = 0;
        switch (op) {
            default:
                break;
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
                result = Math.pow(lhs, rhs);
                break;
        }
        operandStack.push(result);
    }

    public static void main(String args[]) {
        Calculator calc = new Calculator();
        double answer = 0;
        Scanner in = new Scanner(System.in);
        String infixExpression = "";
        int looper = -1;
        boolean retry;
        String FLAG = "Exit";

//        System.out.println(calc.convert("{100+ 90 + 75 - 30} / 4 "));
        do {
            try {
                System.out.print("Please enter the expression you wish to compute(Type Exit to stop): ");
                infixExpression = in.nextLine();
                if (!infixExpression.equals(FLAG)) {
                    answer = calc.convert(infixExpression);
                    if(calc.error == false){
                        System.out.println(answer);
                    }
                }
            } catch (EmptyStackException e) {
                System.out.println("Please use only Operands or Operators");
            } catch (NumberFormatException e) {
                System.out.println("Please use only Operands or Operators");
            }
        } while (!infixExpression.equals(FLAG));
    }
}
//        do {
//            System.out.print("Please enter the expression you desire to compute: ");
//            infixExpression = in.next();
//            try {
//                answer = calc.convert(infixExpression);
//            } catch (EmptyStackException e) {
//                System.out.println("This is not a proper equation.");
//                calc.error = true;
//            }
//            if (calc.error != true) {
//                System.out.println(answer);
//            } else {
//                System.out.println("Please try again.");
//            }
//            do {
//                retry = true;
//                System.out.println("Would you like to try a different expression?");
//                System.out.println("1) Yes, 2) No");
//                while (!in.hasNextInt()) {
//                    System.out.println("This is not an Integer.");;
//                    in.nextLine();
//                }
//                looper = in.nextInt();
//                if (looper < 1 || looper > 2) {
//                    System.out.println("This is not a valid answer");
//                } else {
//                    retry = false;
//                }
//            } while (retry);
//            calc.error = false;
//        } while (looper == 1);