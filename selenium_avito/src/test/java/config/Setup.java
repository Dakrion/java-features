package config;

import org.aeonbits.owner.ConfigFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import pages.HomePage;

import java.time.Duration;

public abstract class Setup {

    protected WebDriver driver;

    protected HomePage homePage;

    protected static final BaseConfig config = ConfigFactory.create(BaseConfig.class, System.getenv());

    public void setup() {
        System.setProperty("webdriver.chrome.driver", config.chromeDriver());
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(25));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(25));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(25));
        driver.get(config.avitoUrl());

        homePage = new HomePage(driver);
    }

    @BeforeSuite
    public void precondition() {
        setup();
    }
    @AfterTest
    public void tearDown() {
        driver.quit();
    }
}
