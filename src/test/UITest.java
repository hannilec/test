package test;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import pageobjects.CustomersPage;


import static org.junit.jupiter.api.Assertions.*;

public class UITest {
    private static WebDriver webDriver;
    private CustomersPage customersPage;

    @BeforeAll
    static void initTest(){
        System.setProperty("webdriver.gecko.driver", "/home/robak/geckodriver-v0.26.0-linux64/geckodriver");
        webDriver = new FirefoxDriver();
    }

    @AfterAll
    static void shutdown(){
        webDriver.quit();
    }

    @BeforeEach
    void openWebsite(){
        webDriver.get("file:///home/robak/Downloads/ui/index.html");
        customersPage = new CustomersPage(webDriver);
    }

    /*
        assuming there are known unique values in the test data set
     */
    @ParameterizedTest(name = "As a user, I would like to search for a customer by {1}")
    @CsvSource({
            "Postimex, Name, 2",
            "@bond.ir, Email, 3",
            "melb, City, 1",
            "stime, Name, 2"
    })
    public void testSearchPositive(String input, String type, int expectedCustomerId){
        int customersTotalBeforeSearch = customersPage.getVisibleCustomersAmount();

        customersPage.searchBy(input, type);

        assertEquals(1, customersPage.getVisibleCustomersAmount());
        assertEquals(expectedCustomerId, customersPage.getVisibleCustomers().get(0).getId());
        assertEquals("Showing 1 of "+customersTotalBeforeSearch+" customers", customersPage.getTableResume());

    }

    @ParameterizedTest(name = "As a user, I would like to search for a customer by {1} case-sensitive")
    @CsvSource({
            "melbourne, City",
            "alabaster, Name",
            "info@BOND.ir, Email"
    })
    public void testSearchWithMatchCaseNegative(String input, String type){
        int customersTotalBeforeSearch = customersPage.getVisibleCustomersAmount();

        customersPage.searchByCaseSensitive(input, type);

        assertEquals(0, customersPage.getVisibleCustomersAmount(),  "Too many search results visible, there shouldn't be any");
        assertEquals("Showing 0 of "+customersTotalBeforeSearch+" customers", customersPage.getTableResume());
    }

    @ParameterizedTest(name = "As a user, I would like to clear search filters")
    @CsvSource({
            "Postimex, Name"
    })
    public void testClearFilters(String input, String type){
        int customersTotalBeforeSearch = customersPage.getVisibleCustomersAmount();

        assertFalse(customersPage.isClearFiltersButtonVisible(), "Clear Filters button visible");

        customersPage.searchBy(input, type);

        assertTrue(customersPage.getVisibleCustomersAmount() < customersTotalBeforeSearch, "Search by "+type+" didn't work");
        assertTrue(customersPage.isClearFiltersButtonVisible(), "Clear Filters button not visible");

        customersPage.clearFilters();

        assertEquals(customersTotalBeforeSearch, customersPage.getVisibleCustomersAmount());
        assertFalse(customersPage.isClearFiltersButtonVisible(), "Clear Filters button visible");

    }

}
