package pages;

import managers.ConfigManager;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utils.LoggerUtil;

public class LoginPage extends BasePage {

    @FindBy(id = "login2")
    private WebElement loginModelButton;

    @FindBy(id = "loginusername")
    private WebElement usernameField;

    @FindBy(id = "loginpassword")
    private WebElement passwordField;

    @FindBy(xpath = "//button[contains(text(),'Log in')]")
    private WebElement loginButton;

    @FindBy(xpath = "//button[contains(text(),'Close')]")
    private WebElement closeButton;

    public LoginPage() {
        super();
        PageFactory.initElements(driver, this);
    }
    public void navigateToLoginPage() {
        driver.get(ConfigManager.getUrl("base_url"));
        LoggerUtil.info("Navigating to website url " + ConfigManager.getUrl("base_url"));
        click(loginModelButton, "Navigating to Login page");
    }
    public void login(String username, String password) {
//      type(usernameField, ConfigManager.getProperty("valid.username"), "UserName");
//		type(passwordField, ConfigManager.getProperty("valid.password"), "Password");
//		click(loginButton, "Login Button");

        type(usernameField, username, "UserName");
        type(passwordField, password, "Password");
        click(loginButton, "Login Button");
    }
}
