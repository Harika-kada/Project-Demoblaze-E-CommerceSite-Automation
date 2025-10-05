package pages;

import managers.ConfigManager;
import managers.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.LoggerUtil;

import java.time.Duration;

public class CheckoutPage extends BasePage {
    @FindBy(xpath = "//button[text()='Place Order']")
    private WebElement placeOrderButton;

    @FindBy(id = "name")
    private WebElement nameField;

    @FindBy(id = "country")
    private WebElement countryField;

    @FindBy(id = "city")
    private WebElement cityField;

    @FindBy(id = "card")
    private WebElement cardField;

    @FindBy(id = "month")
    private WebElement monthField;

    @FindBy(id = "year")
    private WebElement yearField;

    @FindBy(xpath = "//button[text()='Purchase']")
    private WebElement purchaseButton;

    @FindBy(xpath = "//div[@class='sweet-alert  showSweetAlert visible']//h2")
    private WebElement confirmationHeader;

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    public void simulateUser(String userType) {
        if ("Registered".equalsIgnoreCase(userType)) {

            LoginPage loginPage = new LoginPage(driver);
            String username = ConfigManager.getProperty("valid.username");
            String password = ConfigManager.getProperty("valid.password");

            loginPage.navigateToLoginPage();
            loginPage.login(username, password);
            LoggerUtil.info("Simulated registered user login.");
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(3));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("logout2")));
        } else {
            LoggerUtil.info("Simulated guest user flow (no login).");
        }
    }

    public void clickPlaceOrder() {
        click(placeOrderButton, "Place Order Button");
    }

    public void fillCheckoutForm(String name, String country, String city, String card, String month, String year) {

        type(nameField, name, "Name Field");
        type(countryField, country, "Country Field");
        type(cityField, city, "City Field");
        type(cardField, card, "Card Field");
        type(monthField, month, "Month Field");
        type(yearField, year, "Year Field");
    }

    public void submitOrder() {
        click(purchaseButton, "Purchase Button");
    }

    public String getConfirmationMessage() {
        return getText(confirmationHeader, "Confirmation Header");
    }
}