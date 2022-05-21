import app.Calculator;
import exceptions.DivisionByZeroException;
import exceptions.WrongOperatorException;

@SuppressWarnings("InfiniteLoopStatement")
public class Application {
    public static void main(String[] args) throws WrongOperatorException, DivisionByZeroException {
        Calculator calculator = new Calculator();

        while (true) {
            try {
                calculator.innerNum();
                calculator.innerOperator();
                calculator.printResult();
                calculator.closeApp();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}