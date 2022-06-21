package utils;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

public class BasePage {

    public static WebDriver driver;

    public BasePage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        BasePage.driver = driver;
    }

    @Step("Подождать отображения элемента")
    public WebElement waitVisible(WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20), Duration.ofMillis(100));
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    @Step("Сверить текст элемента")
    public static void checkText(String expectedText, WebElement element) {
        assertThat(expectedText).isEqualTo(element.getText());
    }
}
