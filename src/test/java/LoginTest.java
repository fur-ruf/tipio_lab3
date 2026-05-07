import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class LoginTest extends BaseTest {

    private WebDriverWait wait;

    @BeforeEach
    public void setupWait() {
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // UC-02 Авторизация
    @Test
    public void testLoginWithEmail() {
        driver.get("https://www.linkedin.com/");

        safeClick(By.xpath("//a[contains(@href,'signin')]"));

        wait.until(ExpectedConditions.or(
                ExpectedConditions.presenceOfElementLocated(By.name("session")),
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(@id,'captcha')]"))
        ));

//        if (isCaptchaPresent()) {
//            System.out.println("CAPTCHA detected — test stopped");
//            return;
//        }

        type(By.name("session_key"), "linabudukova@gmail.com");
        type(By.name("session_password"), "TylerOneLove");

        String oldUrl = driver.getCurrentUrl();

        safeClick(By.xpath("//button[@type='submit']"));
        assertNotEquals(oldUrl, driver.getCurrentUrl(), "URL did not change");
//        if (waitForCaptcha()) {
//            System.out.println("CAPTCHA detected after submit");
//        } else {
//            System.out.println("Login flow continued");
//        }
    }

    @Test
    public void testLoginWithGoogle() {
        driver.get("https://www.linkedin.com/");

        safeClick(By.xpath("//a[contains(@href,'signin')]"));

        wait.until(ExpectedConditions.or(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(),'Google')]")),
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(@id,'captcha')]"))
        ));

//        if (isCaptchaPresent()) {
//            System.out.println("CAPTCHA detected — skip test");
//            return;
//        }

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