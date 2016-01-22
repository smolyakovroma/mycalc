package smolyakov.roman.ru.mycalculator.exceptions;


public class DivisionByZeroException extends ArithmeticException {
    private static final long serialVersionUID = 1L;

    public DivisionByZeroException() {
    }

    public DivisionByZeroException(String detailMessage) {
        super(detailMessage);
    }
}
