import org.junit.jupiter.api.*;
import pages.TopContentPage;
import static org.junit.jupiter.api.Assertions.*;

// UC-09. Просмотр ленты
public class TopContentTest extends BaseTest {

    private TopContentPage page;

    @BeforeEach
    void init() {
        page = new TopContentPage(driver);
        page.open();
    }

    @Test
    void testHeader() {
        assertTrue(page.isHeaderVisible(), "Header is not visible");
    }

    @Test
    void testTitle() {
        assertTrue(page.hasTitle(), "Title is missing");
    }

    @Test
    void testPillsExist() {
        assertTrue(page.hasPills(), "No topic pills found");
    }

    @Test
    void testPillsClick() {
        String oldUrl = driver.getCurrentUrl();

        page.openFirstPill();

        assertNotEquals(oldUrl, driver.getCurrentUrl(), "Click did not change page");
    }

    @Test
    void testCardsExist() {
        assertTrue(page.hasCards(), "No cards found");
    }

    @Test
    void testCardClick() {
        String oldUrl = driver.getCurrentUrl();

        page.openFirstCard();

        assertNotEquals(oldUrl, driver.getCurrentUrl(), "Click did not change page");
    }

    @Test
    void testFooter() {
        assertTrue(page.isFooterVisible(), "Footer not found");
    }
}
