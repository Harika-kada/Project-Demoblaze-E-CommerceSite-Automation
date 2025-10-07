package tests;

import data.ExcelReaderContact;
import managers.DriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.BasePage;
import pages.ContactPage;
import pages.HomePage;
import utils.LoggerUtil;

import java.time.Duration;

@Listeners(listeners.TestListener.class)
public class ContactFormTests
{
    private WebDriver driver;

    ContactPage contactPage;
    HomePage homePage;
    BasePage basePage;

    @Parameters("browser")
    @BeforeMethod
    public void initialSetup(@Optional("firefox") String browser) {
        LoggerUtil.info("Setting up test....");

        DriverManager.setBrowser(browser);

        driver = DriverManager.getDriver();
        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();

        basePage = new BasePage(driver);
        contactPage = new ContactPage(driver);
        homePage = new HomePage(driver);

        homePage.navigateToWebsite();
    }

    @AfterMethod
    public void tearDown() {
        DriverManager.quitDriver();
        LoggerUtil.info("Finishing tests.");
    }
    @DataProvider(name = "validContactData")
    public Object[][] testWithValidData() {
        return new ExcelReaderContact().getFilteredTestData("valid");
    }
    @Test(dataProvider = "validContactData", description = "Verifying contact form with valid data")
    public void testWithValidData(String email, String name, String message, String expectedAlert) {
        LoggerUtil.info("...TEST WITH VALID DATA...");
        contactPage.navigateToContactForm();
        contactPage.fillContactForm(email, name, message);
        contactPage.submitContactForm();

        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.alertIsPresent());

        String actualAlert;
        if (contactPage.isAlertPresent()) {
            actualAlert = contactPage.handleAlertAndGetText();
        } else {
            actualAlert = "No alert shown";
        }
        LoggerUtil.info("Expected alert: " + expectedAlert);
        LoggerUtil.info("Actual alert: " + actualAlert);

        Assert.assertEquals(actualAlert.trim(), expectedAlert.trim(), "Testcase failed for all valid data");
        LoggerUtil.info("Testcase passed for the valid contact form details.");
    }
    @DataProvider(name = "missingEmailData")
    public Object[][] testWithMissingData() {
        return new ExcelReaderContact().getFilteredTestData("missing email");
    }
    @Test(dataProvider = "missingEmailData", description = "Verifying contact form with missing email")
    public void testWithMissingData(String email, String name, String message, String expectedAlert) {
        LoggerUtil.info("...TEST WITH MISSING EMAIL...");
        contactPage.navigateToContactForm();
        contactPage.fillContactForm(email, name, message);
        contactPage.submitContactForm();

        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.alertIsPresent());

        String actualAlert;
        if (contactPage.isAlertPresent()) {
            actualAlert = contactPage.handleAlertAndGetText();
        } else {
            actualAlert = "No alert shown";
        }
        LoggerUtil.info("Expected alert: " + expectedAlert);
        LoggerUtil.info("Actual alert: " + actualAlert);

        Assert.assertNotEquals(actualAlert.trim(), expectedAlert.trim(), "Testcase failed. Demoblaze incorrectly showed a success alert despite missing required data.");
        LoggerUtil.info("Testcase passed. Alert message did not match expected failure message — form submission was wrongly accepted.");
    }
    @DataProvider(name = "invalidEmailData")
    public Object[][] testWithInvalidMail() {
        return new ExcelReaderContact().getFilteredTestData("invalid email");
    }
    @Test(dataProvider = "invalidEmailData", description = "Verifying contact form with invalid mail id")
    public void testWithInvalidMail(String email, String name, String message, String expectedAlert) {
        LoggerUtil.info("...TEST WITH INVALID EMAIL...");
        contactPage.navigateToContactForm();
        contactPage.fillContactForm(email, name, message);
        contactPage.submitContactForm();

        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.alertIsPresent());

        String actualAlert;
        if (contactPage.isAlertPresent()) {
            actualAlert = contactPage.handleAlertAndGetText();
        } else {
            actualAlert = "No alert shown";
        }
        LoggerUtil.info("Expected alert: " + expectedAlert);
        LoggerUtil.info("Actual alert: " + actualAlert);

        Assert.assertNotEquals(actualAlert.trim(), expectedAlert.trim(), "Testcase failed. Demoblaze incorrectly showed a success alert despite invalid email.");
        LoggerUtil.info("Testcase passed. Alert message did not match expected failure message — form submission was wrongly accepted.");
    }
    @DataProvider(name = "emptyDetails")
    public Object[][] testWithEmptyData() {
        return new ExcelReaderContact().getFilteredTestData("empty fields");
    }
    @Test(dataProvider = "emptyDetails", description = "Verifying contact form with empty fields")
    public void testWithEmptyData(String email, String name, String message, String expectedAlert) {
        LoggerUtil.info("...TEST WITH EMPTY FIELDS...");
        contactPage.navigateToContactForm();
        contactPage.fillContactForm(email, name, message);
        contactPage.submitContactForm();

        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.alertIsPresent());

        String actualAlert;
        if (contactPage.isAlertPresent()) {
            actualAlert = contactPage.handleAlertAndGetText();
        } else {
            actualAlert = "No alert shown";
        }
        LoggerUtil.info("Expected alert: " + expectedAlert);
        LoggerUtil.info("Actual alert: " + actualAlert);

        Assert.assertNotEquals(actualAlert.trim(), expectedAlert.trim(), "Testcase failed. Demoblaze incorrectly showed a success alert despite empty fields.");
        LoggerUtil.info("Testcase passed. Alert message did not match expected failure message — form submission was wrongly accepted.");
    }
}
