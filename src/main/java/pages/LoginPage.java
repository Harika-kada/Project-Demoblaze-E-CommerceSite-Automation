package pages;

import managers.ConfigManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.LoggerUtil;

import java.time.Duration;

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
//        driver.get(ConfigManager.getUrl("base_url"));
//        LoggerUtil.info("Navigating to website url " + ConfigManager.getUrl("base_url"));
//        click(loginPageLink, "Navigating to Login page");
        click(loginPageLink, "Clicked on login link");
        wait.until(ExpectedConditions.visibilityOf(usernameField));
        LoggerUtil.info("Opened login page.");
    }
    public void login(String username, String password) {
//      type(usernameField, ConfigManager.getProperty("valid.username"), "UserName");
//		type(passwordField, ConfigManager.getProperty("valid.password"), "Password");
//		click(loginButton, "Login Button");

//        type(usernameField, username, "UserName");
//        type(passwordField, password, "Password");
//        click(loginButton, "Login Button");


//        wait.until(ExpectedConditions.visibilityOf(usernameField));

        type(usernameField, username, "UserName");
        type(passwordField, password, "Password");

        click(loginButton, "Login Button");
//        WebElement logoutButton = new WebDriverWait(driver, Duration.ofSeconds(3))
//                .until(ExpectedConditions.visibilityOfElementLocated(By.id("logout2")));
//        boolean actualResult = logoutButton.isDisplayed();
    }
}
