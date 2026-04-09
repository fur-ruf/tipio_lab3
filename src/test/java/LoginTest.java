import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

public class LoginTest {

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

    // UC-02 Авторизация (частично из-за CAPTCHA)
    @Test
    public void testLoginWithEmail() {
        driver.get("https://www.linkedin.com/");

        safeClick(By.xpath("//a[contains(@href,'signin')]"));

        wait.until(ExpectedConditions.or(
                ExpectedConditions.presenceOfElementLocated(By.name("session")),
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(@id,'captcha')]"))
        ));

        if (isCaptchaPresent()) {
            System.out.println("CAPTCHA detected — test stopped");
            return;
        }

        type(By.name("session"), "test@email.com");
        type(By.name("session_password"), "test123");

        safeClick(By.xpath("//button[@type='submit']"));

        if (waitForCaptcha()) {
            System.out.println("CAPTCHA detected after submit");
        } else {
            System.out.println("Login flow continued");
        }
    }

    @Test
    public void testLoginWithGoogle() {
        driver.get("https://www.linkedin.com/");

        safeClick(By.xpath("//a[contains(@href,'signin')]"));

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

    // UC-03 Восстановление пароля (частично из-за CAPTCHA)
    @Test
    public void testForgotPassword() {
        driver.get("https://www.linkedin.com/login");

        safeClick(By.xpath("//a[contains(text(),'Забыли пароль') or contains(text(),'Forgot password')]"));

        wait.until(ExpectedConditions.or(
                ExpectedConditions.presenceOfElementLocated(By.name("userName")),
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(@id,'captcha')]"))
        ));

        if (isCaptchaPresent()) {
            System.out.println("CAPTCHA detected — test stopped");
            return;
        }

        type(By.name("userName"), "test@email.com");

        safeClick(By.xpath("//button[contains(text(),'Далее') or contains(text(),'Next')]"));

        if (waitForCaptcha()) {
            System.out.println("CAPTCHA detected after submit");
        }
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