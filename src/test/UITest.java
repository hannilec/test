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
    }

    @AfterEach
    void removeCookies(){
        webDriver.manage().deleteAllCookies();
    }

    @ParameterizedTest(name = "As a user, I would like to search for a customer by {1}")
    @CsvSource({
            "Postimex, Name, 2",
            "@bond.ir, Email, 3",
            "melb, City, 1",
            "stime, Name, 2"
    })
    public void testSearchPositive(String input, String type, int expected){
        CustomersPage customersPage = new CustomersPage(webDriver);
        int customersTotal = customersPage.getVisibleCustomersAmount();

        customersPage.searchBy(input, type);

        assertEquals(customersPage.getVisibleCustomersAmount(), 1);
        assertEquals(customersPage.getVisibleCustomers().get(0).getId(), expected);
        assertEquals(customersPage.getTableResume(), "Showing 1 of "+customersTotal+" customers");

    }

    @ParameterizedTest(name = "As a user, I would like to search for a customer by {1} case-sensitive")
    @CsvSource({
            "melbourne, City",
            "alabaster, Name",
            "BOND, Email"
    })
    public void testSearchWithMatchCaseNegative(String input, String type){
        CustomersPage customersPage = new CustomersPage(webDriver);
        int customersTotal = customersPage.getVisibleCustomersAmount();

        customersPage.searchByCaseSensitive(input, type);

        assertEquals(customersPage.getVisibleCustomersAmount(), 0, "Too many search results visible, there shouldn't be any");
        assertEquals(customersPage.getTableResume(), "Showing 0 of "+customersTotal+" customers");
    }

    @ParameterizedTest(name = "As a user, I would like to clear search filters")
    @CsvSource({
            "Postimex, Name"
    })
    public void testClearFilers(String input, String type){
        CustomersPage customersPage = new CustomersPage(webDriver);
        int customersSize = customersPage.getVisibleCustomersAmount();

        assertFalse(customersPage.isClearFiltersButtonVisible());

        customersPage.searchBy(input, type);

        assertTrue(customersPage.getVisibleCustomersAmount() < customersSize, "Search by "+type+" didn't work");
        assertTrue(customersPage.isClearFiltersButtonVisible());

        customersPage.clearFilters();

        assertEquals(customersSize, customersPage.getVisibleCustomersAmount());

    }

}
