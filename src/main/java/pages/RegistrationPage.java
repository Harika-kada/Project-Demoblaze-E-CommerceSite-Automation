package pages;


import managers.ConfigManager;
import org.openqa.selenium.WebDriver;
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

    @FindBy(xpath = "//div[@id='signInModal']//button[contains(text(),'Close')]")
    private WebElement closeButton;

    public RegistrationPage(WebDriver driver)
    {
        super(driver);
        PageFactory.initElements(driver, this);
    }
    public void navigateToSignUpPage()
    {
        click(openSignupPage, "Clicked on signup link");
        wait.until(ExpectedConditions.visibilityOf(usernameField));
        LoggerUtil.info("Opened signup page.");
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
