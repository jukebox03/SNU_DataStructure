import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BigInteger {
    public static final String QUIT_COMMAND = "quit";
    public static final String MSG_INVALID_INPUT = "Wrong Input";
    public static final Pattern EXPRESSION_PATTERN = Pattern.compile("(\\D*\\d+)(\\D)(\\D*\\d+)");

    int[] num=null;
    String numS=null;
    char sign='+';
    int[] answer=null;

    public BigInteger(int[] num1) {
        int i;
        for(i=0; (i<num1.length)&&(num1[num1.length-1-i]==0); i++);
        if(i==num1.length) {
            answer = new int[1];
            answer[0] = 0;
            sign='=';
        }
        else {
            answer = new int[num1.length - i];
            for (int j = i; j < num1.length; j++)
                answer[j - i] = num1[num1.length - 1 - j];
        }
    }

    public BigInteger(String s) {
        Pattern pattern=Pattern.compile("([+-]*)([0-9]+)");
        Matcher matcher=pattern.matcher(s);

        matcher.find();
        String signS=matcher.group(1);
        numS=matcher.group(2);

        if(signS.equals("-")){
            sign = '-';
        }
        String[] temp=numS.split("");
        num=new int[temp.length];
        for(int i=0; i<temp.length; i++) {
            num[i]=Integer.parseInt(temp[temp.length-1-i]);
        }
    }

    public BigInteger add(BigInteger big) {
        int q=0;
        int i;
        answer=new int[num.length+1];
        for(i=0; i<big.num.length; i++) {
            int temp=num[i]+big.num[i];
            answer[i]=(temp+q)%10;
            q=(temp+q)/10;
        }
        for(int j=i; j<num.length; j++) {
            int temp=num[j];
            answer[j]=(temp+q)%10;
            q=(temp+q)/10;
        }
        answer[num.length]=(q)%10;
        BigInteger ans=new BigInteger(answer);
        return ans;
    }

    public BigInteger subtract(BigInteger big) {
        int q=0;
        int i;
        answer=new int[num.length];
        for(i=0; i<big.num.length; i++) {
            if((num[i]-q)>=big.num[i]) {
                answer[i] = num[i] - q - big.num[i];
                q=0;
            }
            else{
                answer[i]=10+num[i]-q-big.num[i];
                q=1;
            }
        }
        for(int j=i; j<num.length; j++) {
            if((num[j]-q)>=0) {
                answer[j] = num[j] - q;
                q=0;
            }
            else{
                answer[j]=10+num[j]-q;
                q=1;
            }
        }
        BigInteger ans=new BigInteger(answer);
        return ans;
    }

    public BigInteger multiply(BigInteger big) {
        answer=new int[210];
        for(int i=0; i<num.length; i++) {
            for(int j=0; j<big.num.length; j++) {
                int temp=answer[i+j]+num[i]*big.num[j];
                answer[i+j]=(temp)%10;
                answer[i+j+1]+=(temp)/10;
            }
        }
        BigInteger ans=new BigInteger(answer);
        return ans;
    }

    @Override
    public String toString() {
        String temp=Arrays.toString(answer).replaceAll("[^0-9]", "");
        if(sign=='-') return "-"+temp;
        return temp;
    }

    static BigInteger evaluate(String input) throws IllegalArgumentException {
        input=input.replaceAll(" ", "");
        Matcher matcher = EXPRESSION_PATTERN.matcher(input);
        String s=null;
        BigInteger num1=null;
        BigInteger num2=null;

        matcher.find();
        num1 = new BigInteger(matcher.group(1));
        s = matcher.group(2);
        num2 = new BigInteger(matcher.group(3));

        BigInteger result=new BigInteger("1");
        switch (s) {
            case "-":
                if(num2.sign=='+') num2.sign='-';
                else num2.sign='+';
            case "+":
                if(num1.sign==num2.sign) {
                    if((num1.numS.length()>num2.numS.length())|((num1.numS.length()==num2.numS.length())&&(num1.numS.compareTo(num2.numS)>0))) {
                        result = num1.add(num2);
                        if (result.sign=='=') result.sign='+';
                        else result.sign=num1.sign;
                    }
                    else {
                        result = num2.add(num1);
                        if (result.sign=='=') result.sign='+';
                        else result.sign=num2.sign;
                    }
                }
                else
                {
                    if((num1.numS.length()>num2.numS.length())|((num1.numS.length()==num2.numS.length())&&(num1.numS.compareTo(num2.numS)>0))) {
                        result = num1.subtract(num2);
                        if (result.sign=='=') result.sign='+';
                        else result.sign=num1.sign;
                    }
                    else {
                        result = num2.subtract(num1);
                        if (result.sign=='=') result.sign='+';
                        else result.sign=num2.sign;
                    }
                }
                break;
            case "*":
                result = num1.multiply(num2);
                if (result.sign=='=') result.sign='+';
                else if(num1.sign==num2.sign) result.sign='+';
                else result.sign='-';
                break;
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        try (InputStreamReader isr = new InputStreamReader(System.in)) {
            try (BufferedReader reader = new BufferedReader(isr)) {
                boolean done = false;
                while (!done) {
                    String input = reader.readLine();
                    try {
                        done = processInput(input);
                    }
                    catch (IllegalArgumentException e) {
                        System.err.println(MSG_INVALID_INPUT);
                    }
                }
            }
        }
    }

    static boolean processInput(String input) throws IllegalArgumentException {
        boolean quit = isQuitCmd(input);
        if (quit) return true;
        else{
            BigInteger result = evaluate(input);
            System.out.println(result.toString());
            return false;
        }
    }
    static boolean isQuitCmd(String input) {
        return input.equalsIgnoreCase(QUIT_COMMAND);
    }
}