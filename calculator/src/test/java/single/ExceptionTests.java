package single;

import exceptions.DivisionByZeroException;
import exceptions.WrongOperatorException;
import operations.Operations;
import org.testng.annotations.Test;

public class ExceptionTests {

    private Operations operations;

    @Test(description = "Тест на выброс исключения при делении на ноль", expectedExceptions = DivisionByZeroException.class,
            expectedExceptionsMessageRegExp = "На ноль делить нельзя!")
    public void division_by_zero_tests() {
        Integer num1 = 7;
        Integer num2 = 0;
        operations = new Operations(num1, num2);
        operations.calculate("/");
    }

    @Test(description = "Тест на выброс исключения при вводе неверного оператора", expectedExceptions = WrongOperatorException.class,
            expectedExceptionsMessageRegExp = "Такой операции не существует!")
    public void wrong_operator_tests() {
        Integer num1 = 7;
        Integer num2 = 4;
        operations = new Operations(num1, num2);
        operations.calculate("n");
    }
}