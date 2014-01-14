package pa3;

import java.util.*;
public class InfixToPostfix
{
    public class SyntaxErrorException extends Exception
    { 
        SyntaxErrorException(String message) 
        { 
            super(message); 
        }
    }

    private Stack<Character> operatorStack;
    private static final String OPERATORS = "+-*/%^()";
    private static final int[] PRECEDENCE = {1, 1, 2, 2, 2, 3, -1, -1};
    private StringBuilder postfix;
////
    //Convert a string from infix to postfix.
    public String convert(String infix) throws SyntaxErrorException
    {
        operatorStack = new Stack<Character>();
        postfix = new StringBuilder();
        //String[] tokens = infix.split("\\s+");
        try {
            String nextToken;
            Scanner s = new Scanner(infix);
            while ((nextToken = s.findInLine("[\\p{L}\\p{N}\\.]+|[+-/\\*()\\^%{}\\[\\]]")) != null) 
            {
                char firstChar = nextToken.charAt(0);
                //Is this char an operand?
                if (Character.isJavaIdentifierStart(firstChar) || Character.isDigit(firstChar))
                {
                    postfix.append(nextToken);
                    postfix.append(' ');
                } else if (isOperator(firstChar)) { //Is it an operator?
                    processOperator(firstChar);
                } else {
                    throw new SyntaxErrorException("Unexpected Character Encountered: " + firstChar);
                }
            }

            //Pop any remaining operators and append them to postfix.
            while (!operatorStack.empty())
            {
                char op = operatorStack.pop();
                //Any '(' on the stack is not matched.
                if (op == '(')
                    throw new SyntaxErrorException("Unmatched opening parentheses.");
                postfix.append(op);
                postfix.append(' ');
            }

            //Once the stack is empty, return the final result.
            return postfix.toString();

        } catch (EmptyStackException ex) {
            throw new SyntaxErrorException("Syntax Error: The stack is empty.");
        }
    }

    //Method to process operators
    private void processOperator(char op){    
        if (operatorStack.empty() || op == '(') 
            operatorStack.push(op);        
        else {
            char topOp = operatorStack.peek();
            if (precedence(op) > precedence(topOp))
                operatorStack.push(op);
            else{ //Pop all stacked operators with equal or higher precedence than op.
                while (!operatorStack.empty() && precedence(op) <= precedence(topOp)){
                    operatorStack.pop();
                    //Matching ( popped - exit loop.
                    if (topOp == '(')
                        break;
                    postfix.append(topOp);
                    postfix.append(' ');
                    if (!operatorStack.empty()) {
                        topOp = operatorStack.peek();
                    }
                }
                if (op != ')'){
                    //Reset topOp.
                    operatorStack.push(op);
                }
            }            
        }    
    }

    //Determine whether a character is an operator.
    private boolean isOperator (char ch) {
        return OPERATORS.indexOf(ch) != -1;
    }

    //Determines the precedence of an operator.
    private int precedence(char op){
        return PRECEDENCE[OPERATORS.indexOf(op)];
    }
}