package helpers;

import org.openqa.selenium.WebElement;

import java.util.List;

public class Customer{
    int id;
    String name, email, city;

    Customer(int id, String name, String email, String city) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.city = city;
    }

    public Customer(List<WebElement> fields) {
        this(Integer.parseInt(fields.get(0).getText()), fields.get(1).getText(), fields.get(2).getText(), fields.get(3).getText());
    }

    public int getId() {
        return id;
    }
}
