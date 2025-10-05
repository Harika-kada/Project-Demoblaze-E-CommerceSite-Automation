package pages;

import org.openqa.selenium.Alert;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import managers.ConfigManager;
import utils.LoggerUtil;

import java.time.Duration;

public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(
                Integer.parseInt(
                        ConfigManager.getExplicitWait("explicit.wait"))));
        PageFactory.initElements(driver, this);
    }

    protected void click(WebElement element, String elementName) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element)).click();
            LoggerUtil.info("Clicked element: " + elementName);
        } catch (Exception e) {
            LoggerUtil.error("Failed to click element: " + elementName + ". Error: " + e.getMessage());
            throw new RuntimeException("Click failed on " + elementName, e);
        }
    }

    protected void type(WebElement element, String text, String elementName) {
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            element.clear();
            element.sendKeys(text);
            LoggerUtil.info("Typed value as: " + text + " to element " + elementName);
        } catch (Exception e) {
            LoggerUtil.error("Failed to type: " + text + " to element " + elementName + ". Error: " + e.getMessage());
            throw new RuntimeException("Type failed on " + elementName, e);
        }
    }

    protected String getText(WebElement element, String elementName) {
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            String text = element.getText();
            LoggerUtil.info("Successfully read the value as: " + text + " from element " + elementName);
            return text;
        } catch (Exception e) {
            LoggerUtil.error("Failed to read the value from element: " + elementName + ". Error: " + e.getMessage());
            throw new RuntimeException("Get text failed on " + elementName, e);
        }
    }

    public boolean isElementDisplayed(WebElement element, String elementName) {
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            LoggerUtil.info("Successfully displayed element: " + elementName);
            return element.isDisplayed();
        } catch (Exception e) {
            LoggerUtil.error("Element not displayed: " + elementName);
            return false;
        }
    }

    public boolean isAlertPresent() {
        try {
            wait.until(ExpectedConditions.alertIsPresent());
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public String handleAlertAndGetText() {
        try {
            wait.until(ExpectedConditions.alertIsPresent());
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            alert.accept();
            return alertText;
        } catch (TimeoutException e) {
            return null;
        }
    }

    public void acceptAlert() {
        try {
            wait.until(ExpectedConditions.alertIsPresent()).accept();
            LoggerUtil.info("Alert accepted.");
        } catch (Exception e) {
            LoggerUtil.warn("No alert was present to accept.");
        }
    }
}