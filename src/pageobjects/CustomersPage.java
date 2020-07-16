package pageobjects;

import helpers.Customer;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class CustomersPage {
    @FindBy(id="search-input")
    private WebElement searchInput;

    @FindBy(id="search-column")
    private WebElement searchType;

    @FindBy(id="match-case")
    private WebElement caseCheckbox;

    @FindBy(className="table")
    private WebElement resultTable;

    @FindBy(id="clear-button")
    private WebElement clearButton;

    @FindBy(id="search-slogan")
    private WebElement searchSlogan;

    @FindBy(id="table-resume")
    private WebElement tableResume;

    private WebDriver webDriver;

    private WebDriverWait wait;


    public CustomersPage(WebDriver driver){
        this.webDriver = driver;
        PageFactory.initElements(webDriver, this);
        wait = new WebDriverWait(webDriver, Duration.ofSeconds(30).toSeconds());
    }

    public void clearFilters() {
        clearButton.click();
        wait.until(ExpectedConditions.textToBePresentInElement(searchInput, ""));
    }

    public boolean isClearFiltersButtonVisible(){
        return clearButton.isDisplayed();
    }

    public void searchBy(String input, String type) {
        searchInput.clear();
        searchInput.sendKeys(input);
        Select searchTypeSelect = new Select(searchType);
        searchTypeSelect.selectByVisibleText(type);

        wait.until(ExpectedConditions.textToBePresentInElement(searchSlogan, "filtered by term \""+input+"\" in "+type+" column without match case"));
    }

    public void searchByCaseSensitive(String input, String type) {
        searchBy(input, type);
        if (!caseCheckbox.isSelected())
            caseCheckbox.click();

        wait.until(ExpectedConditions.textToBePresentInElement(searchSlogan, "filtered by term \""+input+"\" in "+type+" column with match case"));

    }

    public List<Customer> getVisibleCustomers() {
        List<WebElement> customers = resultTable.findElement(By.tagName("tbody")).findElements(By.tagName("tr"));
        List<Customer> result = new ArrayList();

        for (WebElement customer: customers){
            result.add(new Customer(customer.findElements(By.tagName("td"))));
        }
        return result;
    }

    public int getVisibleCustomersAmount(){
        return resultTable.findElement(By.tagName("tbody")).findElements(By.tagName("tr")).size();
    }

    public String getTableResume() {
        return tableResume.getText();
    }
}
