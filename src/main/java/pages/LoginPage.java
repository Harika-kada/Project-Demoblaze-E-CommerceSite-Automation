package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import utils.LoggerUtil;

public class LoginPage extends BasePage {
    @FindBy(id = "login2")
    private WebElement loginPageLink;

    @FindBy(id = "loginusername")
    private WebElement usernameField;

    @FindBy(id = "loginpassword")
    private WebElement passwordField;

    @FindBy(xpath = "//button[contains(text(),'Log in')]")
    private WebElement loginButton;

    @FindBy(xpath = "//div[@id='logInModal']//button[contains(text(),'Close')]")
    private WebElement closeButton;

    public LoginPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    public void navigateToLoginPage() {
        click(loginPageLink, "Clicked on login link");
        wait.until(ExpectedConditions.visibilityOf(usernameField));
        LoggerUtil.info("Opened login page.");
    }

    public void login(String username, String password) {
        type(usernameField, username, "UserName");
        type(passwordField, password, "Password");

        click(loginButton, "Login Button");
    }
}