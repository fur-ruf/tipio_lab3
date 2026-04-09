import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

public class RegistrationTest {

    WebDriver driver;
    WebDriverWait wait;

    @BeforeEach
    public void setup() {
        String browser = System.getProperty("browser", "chrome");

        driver = browser.equalsIgnoreCase("firefox")
                ? new FirefoxDriver()
                : new ChromeDriver();

        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

    // UC-01. Регистрация через почту (частично из-за CAPTCHA)
    @Test
    public void testRegistrationWithEmail() {
        driver.get("https://www.linkedin.com/");

        safeClick(By.xpath("//a[contains(@href,'signup') or contains(text(),'Присоединиться')]"));

        wait.until(ExpectedConditions.or(
                ExpectedConditions.presenceOfElementLocated(By.name("email-address")),
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(@id,'captcha')]"))
        ));

        if (isCaptchaPresent()) {
            System.out.println("CAPTCHA detected — test stopped");
            return;
        }

        type(By.name("email-address"), "test@email.com");
        type(By.name("password"), "test123");

        safeClick(By.xpath("//button[@type='submit']"));

        wait.until(ExpectedConditions.or(
                ExpectedConditions.presenceOfElementLocated(By.name("first-name")),
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(@id,'captcha')]"))
        ));

        if (isCaptchaPresent()) {
            System.out.println("CAPTCHA detected after submit");
            return;
        }

        type(By.name("first-name"), "Tyler");
        type(By.name("last-name"), "Durden");

        safeClick(By.xpath("//button[@type='submit']"));

        if (waitForCaptcha()) {
            System.out.println("CAPTCHA detected at final step");
        } else {
            System.out.println("Registration flow continued");
        }
    }

    // UC-01. Регистрация через Google (частично)
    @Test
    public void testRegistrationWithGoogle() {
        driver.get("https://www.linkedin.com/");

        safeClick(By.xpath("//a[contains(@href,'signup')]"));

        wait.until(ExpectedConditions.or(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(),'Google')]")),
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(@id,'captcha')]"))
        ));

        if (isCaptchaPresent()) {
            System.out.println("CAPTCHA detected — skip test");
            return;
        }

        safeClick(By.xpath("//*[contains(text(),'Google')]"));

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(),'Google')]")));

        System.out.println("Google auth page opened");
    }

    private void safeClick(By locator) {
        wait.until(driver -> {
            try {
                WebElement el = driver.findElement(locator);
                el.click();
                return true;
            } catch (StaleElementReferenceException | ElementClickInterceptedException e) {
                return false;
            }
        });
    }

    private void type(By locator, String text) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).sendKeys(text);
    }

    private boolean isCaptchaPresent() {
        return !driver.findElements(By.xpath("//*[contains(@id,'captcha')]")).isEmpty()
                || !driver.findElements(By.xpath("//iframe[contains(@src,'captcha')]")).isEmpty();
    }

    private boolean waitForCaptcha() {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            shortWait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//*[contains(@id,'captcha')]")
            ));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }
}