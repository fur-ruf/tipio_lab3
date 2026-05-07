package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class SearchPage extends BasePage {

    public SearchPage(WebDriver driver) {
        super(driver);
    }

    public void open() {
        open("https://www.linkedin.com/");
    }

    private final By loginModal = By.cssSelector(".modal__overlay--visible");
    private final By closeModalBtn = By.cssSelector("button[aria-label='Dismiss']");

    private void closeLoginModalIfPresent() {
        try {
            if (!driver.findElements(loginModal).isEmpty()) {
                click(closeModalBtn);
                wait.until(ExpectedConditions.invisibilityOfElementLocated(loginModal));
            }
        } catch (Exception ignored) {}
    }

    private void safeClick(By locator) {
        closeLoginModalIfPresent();
        click(locator);
    }

    private final By switcher = By.xpath("//button[contains(@class,'search-bar')]");

    private final By peopleTab = By.xpath("//span[contains(text(),'Люди')]");
    private final By jobsTab = By.xpath("//span[contains(text(),'Вакансии') or contains(text(),'Jobs')]");
    private final By learningTab = By.xpath("//span[contains(text(),'Обучение') or contains(text(),'Learning')]");

    private final By searchInput = By.xpath("//input[contains(@placeholder,'Поиск') or contains(@placeholder,'Search')]");

    private final By firstNameInput = By.xpath("//section[@data-searchbar-type='PEOPLE']//input[@name='firstName']");
    private final By lastNameInput = By.xpath("//section[@data-searchbar-type='PEOPLE']//input[@name='lastName']");

    private final By jobsInput = By.xpath("//section[@data-searchbar-type='JOBS']//input[@name='keywords']");
    private final By learningInput = By.xpath("//section[@data-searchbar-type='LEARNING']//input[@name='keywords']");

    private final By jobCards = By.xpath("//div[contains(@class,'job-search-card')]");
    private final By firstJob = By.xpath("(//div[contains(@class,'job-search-card')]//a)[1]");

    private final By companyLink = By.cssSelector("a[href*='/company/']");

    private final By companyName = By.xpath("//h1");

    public void selectPeople() {
        openSwitcher();
        safeClick(peopleTab);
    }

    public void selectJobs() {
        openSwitcher();
        safeClick(jobsTab);
    }

    public void selectLearning() {
        openSwitcher();
        safeClick(learningTab);
    }

    public boolean isSearchInputDisplayed() {
        return isDisplayed(searchInput);
    }

    public void typePeopleSearch(String firstName, String lastName) {
        type(firstNameInput, firstName);
        type(lastNameInput, lastName);
    }

    public void typeJobsSearch(String keyword) {
        type(jobsInput, keyword);
    }

    public void typeLearningSearch(String keyword) {
        type(learningInput, keyword);
    }

    public boolean areJobCardsDisplayed() {
        return isDisplayed(jobCards);
    }

    public void clickFirstJob() {
        safeClick(firstJob);
    }

    public void searchJobs(String keyword) {
        selectJobs();
        typeJobsSearch(keyword);
    }

    public boolean hasJobResults() {
        return areJobCardsDisplayed();
    }

    public void openFirstJob() {
        clickFirstJob();
    }

    public boolean isOnJobPage() {
        return driver.getCurrentUrl().contains("jobs");
    }

    public void openCompany() {
        safeClick(companyLink);
    }

    public boolean isOnCompanyPage() {
        return driver.getCurrentUrl().contains("/company/");
    }

    public boolean isCompanyVisible() {
        return isDisplayed(companyName);
    }

    private void openSwitcher() {
        List<WebElement> elements = driver.findElements(switcher);
        if (!elements.isEmpty()) {
            safeClick(switcher);
        }
    }
}