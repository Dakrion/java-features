package exceptions;

import java.util.InputMismatchException;

public class InputException extends InputMismatchException {
    public InputException(String s) {
        super(s);
    }
}
