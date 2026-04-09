package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class TopContentPage extends BasePage {

    public TopContentPage(WebDriver driver) {
        super(driver);
    }

    public void open() {
        open("https://www.linkedin.com/top-content/");
    }

    private final By joinBtn = By.xpath("//a[contains(@href,'signup')]");
    private final By loginBtn = By.xpath("//a[contains(@href,'login')]");
    private final By title = By.xpath("//h1");
    private final By pills = By.xpath("//a[contains(@class,'pill')]");
    private final By cards = By.xpath("//div[contains(@class,'topic-category')]");
    private final By footer = By.xpath("//footer");

    public boolean isHeaderVisible() {
        return isDisplayed(joinBtn) && isDisplayed(loginBtn);
    }

    public boolean hasTitle() {
        return isDisplayed(title);
    }

    public boolean hasPills() {
        return !driver.findElements(pills).isEmpty();
    }

    public boolean hasCards() {
        return !driver.findElements(cards).isEmpty();
    }

    public void openFirstPill() {
        driver.findElements(pills).get(0).click();
    }

    public void openFirstCard() {
        driver.findElements(cards).get(0).click();
    }

    public boolean isFooterVisible() {
        return isDisplayed(footer);
    }
}