package parameterized;

import operations.Operations;
import org.testng.Assert;
import org.testng.annotations.Test;

public class OperationsTests {

    private Operations operations;

    @Test(dataProvider = "positiveCases", dataProviderClass = Providers.class,
            description = "Тест на правильность расчета в методе calculate")
    public void positive_tests_for_operations(Integer num1, Integer num2, Integer result, String operator) {
        operations = new Operations(num1, num2);
        Assert.assertEquals(operations.calculate(operator), result);
    }
}