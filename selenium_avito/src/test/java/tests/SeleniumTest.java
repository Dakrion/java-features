package tests;

import config.Setup;
import org.testng.annotations.Test;

import static utils.Category.CONSUMABLES;
import static utils.Sort.EXPENSIVE;

public class SeleniumTest extends Setup {

    @Test(description = "Тест авито")
    public void avitoTest() {

        homePage
                .waitPage()
                .goToCategory(CONSUMABLES)
                .search("Принтеры")
                .getRegion("Владивосток")
                .setSort(EXPENSIVE)
                .activateCheckBoxWithPhoto()
                .getItemsByCount(5);
    }
}