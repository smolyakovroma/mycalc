package smolyakov.roman.ru.mycalculator.calculations;


import smolyakov.roman.ru.mycalculator.exceptions.DivisionByZeroException;

public class CalcOperations {


    public static Double add(double a, double b) {
        return a+b;
    }

    public static Double divide(double a, double b) {
        if(b==0){
            throw new DivisionByZeroException();
        }
        return a/b;
    }

    public static Double multiply(double a, double b) {
        return a*b;
    }

    public static Double substract(double a, double b) {
        return a-b;
    }
}
