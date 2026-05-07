import org.junit.jupiter.api.*;
import pages.SearchPage;

import static org.junit.jupiter.api.Assertions.*;

public class SearchTest extends BaseTest {

    private SearchPage page;

    @BeforeEach
    void init() {
        page = new SearchPage(driver);
        page.open();
    }

    @Test
    void testSelectPeople() {
        page.selectPeople();
        assertTrue(page.isSearchInputDisplayed(), "Search input not visible");
    }

    @Test
    void testSelectJobs() {
        page.selectJobs();
        assertTrue(page.isSearchInputDisplayed(), "Search input not visible");
    }

    @Test
    void testSelectLearning() {
        page.selectLearning();
        assertTrue(page.isSearchInputDisplayed(), "Search input not visible");
    }

    // UC-15. Поиск людей
    @Test
    void testSearchPeople() {
        page.selectPeople();
        page.typePeopleSearch("Tyler", "Durden");

        assertTrue(page.isSearchInputDisplayed(), "Search failed");
    }

    // UC-22. Поиск вакансий
    @Test
    void testSearchJobs() {
        page.searchJobs("QA Engineer");

        assertTrue(page.hasJobResults(), "No job results");
    }

    @Test
    void testOpenJobFromSearch() {
        page.searchJobs("QA Engineer");

        assertTrue(page.hasJobResults(), "No job cards found");

        String oldUrl = driver.getCurrentUrl();

        page.openFirstJob();

        assertTrue(page.isOnJobPage(), "Did not navigate to job page");
        assertNotEquals(oldUrl, driver.getCurrentUrl(), "URL did not change");
    }

    @Test
    void testSearchLearning() {
        page.selectLearning();
        page.typeLearningSearch("Java");

        assertTrue(page.isSearchInputDisplayed(), "Learning search failed");
    }
}