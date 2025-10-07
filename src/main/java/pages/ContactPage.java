package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import utils.LoggerUtil;

public class ContactPage extends BasePage {
    @FindBy(xpath  = "//a[text()='Contact']")
    private WebElement contactPageLink;

    @FindBy(id = "recipient-email")
    private WebElement emailField;

    @FindBy(id = "recipient-name")
    private WebElement nameField;

    @FindBy(id = "message-text")
    private WebElement messageField;

    @FindBy(xpath = "//button[text()='Send message']")
    private WebElement sendButton;

    public ContactPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    public void navigateToContactForm() {
        click(contactPageLink, "Clicked on contact form link");
        wait.until(ExpectedConditions.visibilityOf(emailField));
        LoggerUtil.info("Opened contact page.");
    }

    public void fillContactForm(String email, String name, String message) {
        type(emailField, email, "Email Field");
        type(nameField, name, "Name Field");
        type(messageField, message, "Message Field");
    }

    public void submitContactForm() {
        click(sendButton, "Send Button");
    }
}
