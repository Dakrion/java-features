package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import utils.Category;
import utils.BasePage;
import utils.Sort;

import static utils.ScreenShooter.makeScreenShot;


public class HomePage extends BasePage {

    public HomePage(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath = "//*[@class=\"logo-root-UhAJ6\"]")
    private WebElement logo;

    @FindBy(xpath = "//*[@id=\"category\"]")
    private WebElement categoryButton;

    @FindBy(xpath = "//*[@id=\"app\"]/div[2]/div/div[2]/div/div[1]/div")
    private WebElement categoryBox;

    @FindBy(xpath = "//*[@id=\"app\"]/div[2]/div/div[2]/div/div[2]/div/div/div/label[1]")
    private WebElement inputSearch;

    @FindBy(xpath = "//*[@id=\"app\"]/div[2]/div/div[2]/div/div[5]/div[1]/span/span/div")
    private WebElement regionField;

    @FindBy(xpath = "//*[@id=\"app\"]/div[2]/div/div[2]/div/div[6]/div/div/span/div/div[1]/div[2]/div/input")
    private WebElement inputRegion;

    @FindBy(xpath = "//*[@id=\"app\"]/div[2]/div/div[2]/div/div[6]/div/div")
    private WebElement regionPopUp;

    @FindBy(xpath = "//*[@id=\"app\"]/div[2]/div/div[2]/div/div[6]/div/div/span/div/div[1]/div[2]/div/ul/li[1]/span/span/span[1]/strong")
    private WebElement regionListFirstPosition;

    @FindBy(xpath = "//*[@id=\"app\"]/div[2]/div/div[2]/div/div[6]/div/div/span/div/div[1]/div[2]/div/div/svg")
    private WebElement regionPopUpCloseButton;

    @FindBy(xpath = "//*[@id=\"app\"]/div[2]/div/div[2]/div/div[4]/div[1]/label[2]/span")
    private WebElement checkBoxWithPhoto;

    @FindBy(xpath = "//*[@id=\"app\"]/div[2]/div/div[2]/div/div[6]/div/div/span/div/div[3]/div/div/div/button")
    private WebElement showButton;

    @FindBy(xpath = "//*[@id=\"app\"]/div[2]/div/div[2]/div/div[4]/div[1]/label[2]/span")
    private WebElement checkWithPhoto;

    @FindBy(xpath = "//*[@id=\"app\"]/div[3]/div[3]/div[3]/div[1]/div[2]/select/option[3]")
    private WebElement expensiveSort;

    @FindBy(xpath = "//*[@id=\"app\"]/div[3]/div[3]/div[3]/div[1]/div[2]/select/option[4]")
    private WebElement dataSort;

    @FindBy(xpath = "//*[@id=\"app\"]/div[3]/div[3]/div[3]/div[1]/div[2]/select/option[2]")
    private WebElement cheaperSort;

    @FindBy(xpath = "//*[@id=\"app\"]/div[3]/div[3]/div[3]/div[1]/div[2]/select/option[1]")
    private WebElement defaultSort;

    @FindBy(xpath = "//*[@id=\"category\"]/option[3]")
    private WebElement autoCategory;

    @FindBy(xpath = "//*[@id=\"category\"]/option[55]")
    private WebElement consumablesCategory;

    @FindBy(xpath = "//*[@id=\"category\"]/option[47]")
    private WebElement phonesCategory;

    @FindBy(xpath = "//*[@id=\"app\"]/div[3]/div[3]/div[3]/div[3]/div[1]")
    private WebElement contentBox;


    @Step("Ожидание загрузки сайта")
    public HomePage waitPage() {
        waitVisible(logo);
        makeScreenShot(driver);
        return this;
    }

    @Step("Перейти в категорию {category.name}")
    public HomePage goToCategory(Category category) {
        categoryButton.click();

        switch (category) {
            case PHONES -> phonesCategory.click();
            case AUTO -> autoCategory.click();
            case CONSUMABLES -> consumablesCategory.click();
        }
        makeScreenShot(driver);
        return this;
    }

    @Step("Искать {searchContex}")
    public HomePage search(String searchContex) {
        waitVisible(inputSearch);
        inputSearch.sendKeys(searchContex);
        inputSearch.sendKeys(Keys.ENTER);
        makeScreenShot(driver);
        return this;
    }

    @Step("Фильтр по региону {region}")
    public HomePage getRegion(String region) {
        regionField.click();
        waitVisible(regionPopUp);
        inputRegion.sendKeys(region);
        regionListFirstPosition.click();
        showButton.click();
        makeScreenShot(driver);
        return this;
    }

    @Step("Активировать чекбокс 'Только с фото'")
    public HomePage activateCheckBoxWithPhoto() {
        checkBoxWithPhoto.click();
        makeScreenShot(driver);
        return this;
    }

    @Step("Выбрать в фильтре сортировки '{sort.name}'")
    public HomePage setSort(Sort sort) {
        switch (sort) {
            case DATA -> dataSort.click();
            case CHEAPER -> cheaperSort.click();
            case EXPENSIVE -> expensiveSort.click();
            case DEFAULT -> defaultSort.click();
        }
        makeScreenShot(driver);
        return this;
    }

    @Step("Вывести цену первых {count} вариантов из списка товаров")
    public HomePage getItemsByCount(int count) {
        for (int i = 1; i <= count; i++) {
            System.out.println(driver.findElement(By.xpath("/html/body/div[1]/div[3]/div[3]/div[3]/div[3]/div[1]/div[" + i + "]/div/div[2]/div[2]/a/h3"))
                    .getText() + ": " +
                    driver.findElement(By.xpath("/html/body/div[1]/div[3]/div[3]/div[3]/div[3]/div[1]/div[" + i + "]/div/div[2]/div[3]/span/span/meta[2]"))
                            .getAttribute("content") + " руб.");
        }
        return this;
    }
}