package pages;


import managers.ConfigManager;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.LoggerUtil;

import java.time.Duration;

public class RegistrationPage extends BasePage
{
    @FindBy(id = "signin2")
    private WebElement openSignupPage;

    @FindBy(id = "sign-username")
    private WebElement usernameField;

    @FindBy(id = "sign-password")
    private WebElement passwordField;

    @FindBy(xpath = "//button[contains(text(),'Sign up')]")
    private WebElement signUpButton;

    @FindBy(xpath = "//button[contains(text(),'Close')]")
    private WebElement closeButton;

    public RegistrationPage()
    {
        super();
        PageFactory.initElements(driver, this);
    }
    public void navigateToSignUpPage()
    {
        driver.get(ConfigManager.getUrl("base_url"));
        LoggerUtil.info("Navigating to website url " + ConfigManager.getUrl("base_url"));

        click(openSignupPage, "Navigating to SignUp page");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(usernameField));
    }
    public void signUp(String username, String password)
    {
//        type(usernameField, ConfigManager.getProperty("valid.username"), "UserName");
//        type(passwordField, ConfigManager.getProperty("valid.password"), "Password");
//        click(signUpButton, "Login Button");

//        String alertMessage = getAlertText();
//        System.out.println("Alert message: " + alertMessage);
//
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//        wait.until(ExpectedConditions.visibilityOf(usernameField));
        type(usernameField, username, "UserName");
        type(passwordField, password, "Password");
        click(signUpButton, "SignUp Button");
    }
    public void closeSignUpModal() {
        click(closeButton, "Close Sign Up Modal");
    }
}
