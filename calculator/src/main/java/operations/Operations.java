package operations;

import exceptions.InputException;
import exceptions.WrongOperatorException;

public class Operations {

    private final Integer num1;
    private final Integer num2;

    public Operations(Integer num1, Integer num2) {
        this.num1 = num1;
        this.num2 = num2;
    }

    public Integer calculate(String operator) {
        switch (operator) {
            case "+":
                return num1 + num2;
            case "-":
                return num1 - num2;
            case "*":
                return num1 * num2;
            case "/":
                if (num2 != 0) {
                    return num1 / num2;
                } else throw new WrongOperatorException("На ноль делить нельзя!");
            default:
                 throw new InputException("Такой операции не существует");
        }
    }
}
