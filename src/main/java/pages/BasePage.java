package pages;

import managers.DriverManager;
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

    public BasePage() {
        this.driver = DriverManager.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(
                Integer.parseInt(
                        ConfigManager.getExplicitWait("explicit.wait"))));
        PageFactory.initElements(driver, this);
    }

    protected void click(WebElement element, String elementName) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element));
            element.click();
            LoggerUtil.info("Clicked element: " + elementName);
        } catch (Exception e) {
            LoggerUtil.error("Failed to click element: " + elementName);
            throw e;
        }
    }

    protected void type(WebElement element, String text, String elementName) {
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            element.clear();
            element.sendKeys(text);
            LoggerUtil.info("Typed value as: " + text + " to element " + elementName);
        } catch (Exception e) {
            LoggerUtil.error("Failed to type: " + text + " to element " + elementName);
            throw e;
        }
    }

    protected String getText(WebElement element, String elementName) {
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            String text = element.getText();
            LoggerUtil.info("Successfully read the value as: " + text + " to element " + elementName);
            return text;
        } catch (Exception e) {
            LoggerUtil.error("Failed to read the value from element: " + elementName);
            throw e;
        }
    }

    public boolean isElementDisplayed(WebElement element, String elementName) {
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            boolean isDisplayed = element.isDisplayed();
            LoggerUtil.info("Successfully displayed element: " + elementName);
            return isDisplayed;
        } catch (Exception e) {
            LoggerUtil.error("Element not displayed: " + elementName);
            throw e;
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
    public String getAlertText() {
        try {
            wait.until(ExpectedConditions.alertIsPresent());
            String alertMessage = driver.switchTo().alert().getText();
            LoggerUtil.warn("Alert message: " + alertMessage);
            driver.switchTo().alert().accept();
            return alertMessage;
        } catch (Exception e) {
            LoggerUtil.error("No alert present or failed to read alert.");
            throw e;
        }
    }
//    public String handleAlertIfPresent() {
//        try {
//            wait.until(ExpectedConditions.alertIsPresent());
//            Alert alert = driver.switchTo().alert();
//            String text = alert.getText();
//            LoggerUtil.warn("Alert: " + text);
//            alert.accept();
//            return text;
//        }
//        catch (TimeoutException e) {
//            return null;
//        }
//        catch (Exception e) {
//            LoggerUtil.error("Failed to handle alert: " + e.getMessage());
//            return null;
//        }
//    }
}
