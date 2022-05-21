package parameterized;

import org.testng.annotations.DataProvider;

public class Providers {

    @DataProvider
    public static Object[][] positiveCases() {
        return new Object[][]{
                {5, 4, 9, "+"},
                {5, 4, 1, "-"},
                {5, 4, 20, "*"},
                {8, 2, 4, "/"},
                {8, -2, -4, "/"},
                {8, 7, 1, "/"},
                {7, 8, 0, "/"},
                {-7, -8, 1, "-"},
                {-7, 8, -15, "-"}
        };
    }
}