import java.io.*;
import java.util.Iterator;
import java.util.Stack;

public class CalculatorTest
{
    public static void main(String args[]) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            try {
                String input = br.readLine();
                if (input.compareTo("q") == 0)
                    break;

                command(input);
            } catch (Exception e) {
                //System.out.println(e.toString());
                System.out.println("ERROR");
            }
        }
    }
    private static int precedence(char operator) {//Determine the priority order of operations
        if(operator == '+' || operator == '-')
            return 1;
        else if(operator == '*' || operator == '/' || operator == '%')
            return 2;
        else if(operator == '^')
            return 3;
        else if(operator == '~')
            return 4;
        else
            return -1;
    }
    private static long applyOp(char operator, long operand1, long operand2) { //the action of proper operations
        switch(operator) {
            case '+':
                return operand1 + operand2;
            case '-':
                return operand1 - operand2;
            case '*':
                return operand1 * operand2;
            case '/':
                if (operand2 == 0) throw new UnsupportedOperationException("Cannot divide by zero");//if operand2==0
                return operand1 / operand2;
            case '%':
                if (operand2 == 0) throw new UnsupportedOperationException("Cannot divide by zero");//if operand2==0
                return operand1 % operand2;
            case '^':
                if (operand1 == 0&&operand2 < 0) throw new IllegalArgumentException("Cannot power by negative number");//if operand1==0 and operand2<0
                return (long)Math.pow(operand1, operand2);
            default: throw new IllegalArgumentException("Invalid operator");
        }
    }
    private static void command(String input) {
        StringBuilder result = new StringBuilder();
        Stack<Character> operatorStack = new Stack<>();
        boolean checkBlank=false;//true if previous value is blank
        boolean checkOperator=true;//true if previous value is operator
        long checkBracket=0;//count opening parenthesis - closing parenthesis
        boolean checkAvg=false;//true when calculate avg
        boolean checkNum=false;//true if previous value is number
        long countAvg=1;//count total number

        for(int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if(c == ' ' || c == '\t'){//if the character is blank
                checkBlank=true;
            }
            else if(Character.isDigit(c)) {
                if(checkOperator==false&&checkNum==false) throw new IllegalArgumentException("Need Operator");//if there is no operator and number
                if(checkNum==true&&checkBlank==true) throw new IllegalArgumentException("There is a blank between numbers");//if there is blank between number
                checkBlank=false;
                checkOperator=false;
                checkNum=true;
                result.append(c);
                if(i+1<input.length()&&!Character.isDigit(input.charAt(i+1)))
                    result.append(" ");
                else if(i+1==input.length())
                    result.append(" ");
            } else if(c == ','){//when we calculate avg
                if(checkOperator==true) throw new IllegalArgumentException("Invalid operator");
                if(checkBracket<=0) throw new IllegalArgumentException("Is not Avg");
                checkOperator=true;
                checkBlank=false;
                checkNum=false;
                while(!operatorStack.empty() && (operatorStack.peek() != ','&&operatorStack.peek()!='(')) {
                    result.append(operatorStack.pop());
                    result.append(" ");
                }
                operatorStack.push(',');
            } else if(c == '(') {//open parenthesis
                if(checkNum==true) throw new IllegalArgumentException("Need Operator");
                checkNum=false;
                checkBracket++;
                checkBlank=false;
                checkOperator=true;
                operatorStack.push(c);
            } else if(c == ')') {//close parenthesis
                if(checkBracket<=0) throw new IllegalArgumentException("Wrong Bracket");//wrong number of parenthesis
                if(checkOperator==true) throw new IllegalArgumentException("Need Number");//no number after operator
                checkBlank=false;
                checkOperator=false;
                checkNum=false;
                while(!operatorStack.empty() && operatorStack.peek() != '(') {
                    if(operatorStack.peek()==','){
                        checkAvg=true;
                        countAvg++;
                        operatorStack.pop();
                    }
                    else {
                        result.append(operatorStack.pop());
                        result.append(" ");
                    }
                }
                if(checkAvg==true){
                    result.append(countAvg);
                    result.append(" ");
                    result.append("avg ");
                }
                countAvg=1;
                checkAvg=false;
                checkBracket--;
                operatorStack.pop();
            } else if(c == '+' || c == '*' || c == '/' || c == '%') {//operator
                if(checkOperator==true)
                    throw new IllegalArgumentException("Invalid operator");
                checkBlank=false;
                checkOperator=true;
                checkNum=false;
                while(!operatorStack.empty() && precedence(c) <= precedence(operatorStack.peek())) {
                    result.append(operatorStack.pop());
                    result.append(" ");
                }
                operatorStack.push(c);
            } else if(c == '^') {//operator ^
                if(checkOperator==true)
                    throw new IllegalArgumentException("Invalid operator");
                checkBlank=false;
                checkOperator=true;
                checkNum=false;
                operatorStack.push(c);
            } else if(c == '-'){//operator -
                checkBlank=false;
                checkNum=false;
                if(checkOperator==true||i==0)
                {
                    operatorStack.push('~');
                } else{
                    checkOperator=true;
                    while(!operatorStack.empty() && precedence(c) <= precedence(operatorStack.peek())) {
                        result.append(operatorStack.pop());
                        result.append(" ");
                    }
                    operatorStack.push(c);
                }
            }else throw new IllegalArgumentException("Weird");//else
        }
        if(checkBracket!=0) throw new IllegalArgumentException("Wrong Bracket");//wrong parenthesis
        if(checkOperator==true) throw new IllegalArgumentException("Need Number");//end with operator
        if(result.toString()=="") throw new IllegalArgumentException("No Equation");//no equation
        while(!operatorStack.empty()) {
            result.append(operatorStack.pop());
            result.append(" ");
        }
        long answer=evaluate(result.toString());

        System.out.println(result.toString().substring(0, result.toString().length()-1));//erase the last blank
        System.out.println(answer);
    }
    private static long evaluate(String expression) {
        Stack<Long> numberStack = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            if (Character.isDigit(c)) {//calculate correct number
                long num = 0;
                while (Character.isDigit(c)) {
                    num = num * 10 + (c - '0');
                    i++;
                    if (i < expression.length())
                        c = expression.charAt(i);
                    else
                        break;
                }
                i--;
                numberStack.push(num);
            } else if (c == '+' || c == '-' || c == '*' || c == '/' || c == '^' || c == '%') {
                long num2 = numberStack.pop();
                long num1 = numberStack.pop();
                long result = 0;

                result = applyOp(c, num1, num2);

                numberStack.push(result);
            } else if(c == '~'){//calculate unary minus
                long num =  numberStack.pop();
                numberStack.push(-1*num);
            }
            else if(c == 'a'){//calculate avg
                i+=2;
                long count = numberStack.pop();
                long tot = 0;
                for(int j=0; j<count; j++){
                    tot += numberStack.pop();
                }
                numberStack.push((long)(tot/count));
            }
        }
        return numberStack.pop();
    }
}