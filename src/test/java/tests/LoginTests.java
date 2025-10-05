package tests;

import data.ExcelReaderLogin;
import managers.DriverManager;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import pages.BasePage;
import pages.HomePage;
import pages.LoginPage;
import utils.LoggerUtil;

import java.time.Duration;

@Listeners(listeners.TestListener.class)
public class LoginTests {
    private WebDriver driver;

    LoginPage loginPage;
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
        loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);

        homePage.navigateToWebsite();
    }

    @AfterMethod
    public void tearDown() {
        DriverManager.quitDriver();
        LoggerUtil.info("Finishing tests.");
    }

    @DataProvider(name = "validLoginData")
    public Object[][] validLoginData() {
        return new ExcelReaderLogin().getFilteredTestData("valid");
    }

    @Test(dataProvider = "validLoginData", description = "Verifying with valid credentials")
    public void loginWithValidData(String username, String password, boolean expectedResult) {
        LoggerUtil.info("...TEST WITH VALID CREDENTIALS...");

        loginPage.navigateToLoginPage();
        loginPage.login(username, password);

        if (basePage.isAlertPresent()) {
            String alertText = basePage.handleAlertAndGetText();
            LoggerUtil.error("Testcase failed. An unexpected alert was shown: " + alertText);
            Assert.fail("Login failed for user: " + username + " with alert: " + alertText);
        }

        WebElement logoutButton = new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.visibilityOfElementLocated(By.id("logout2")));
        boolean actualResult = logoutButton.isDisplayed();

        Assert.assertEquals(actualResult, expectedResult, "Login result mismatch");

        LoggerUtil.info("Testcase passed. Login succeeded for user: " + username);
        homePage.logout();
    }

    @DataProvider(name = "invalidLoginData")
    public Object[][] invalidLoginData() {
        return new ExcelReaderLogin().getFilteredTestData("invalid");
    }

    @Test(dataProvider = "invalidLoginData", description = "Verifying with invalid credentials")
    public void loginWithInvalidData(String username, String password, boolean expectedResult) {
        LoggerUtil.info("...TEST WITH INVALID CREDENTIALS...");

        loginPage.navigateToLoginPage();
        loginPage.login(username, password);

        String alertText = basePage.handleAlertAndGetText();

        Assert.assertNotNull(alertText, "Expected an alert message but none was shown.");
        boolean isInvalidLogin = alertText.equals("Wrong password.") || alertText.equals("User does not exist.");
        Assert.assertTrue(isInvalidLogin, "Unexpected alert message: " + alertText);

        LoggerUtil.info("Testcase passed. Login correctly rejected for user: " + username);
    }

    @DataProvider(name = "emptyFields")
    public Object[][] emptyLoginData() {
        return new ExcelReaderLogin().getFilteredTestData("empty");
    }

    @Test(dataProvider = "emptyFields", description = "Verifying with empty credentials")
    public void loginWithEmptyFields(String username, String password, boolean expectedResult) {
        LoggerUtil.info("...TEST WITH EMPTY CREDENTIALS...");

        loginPage.navigateToLoginPage();
        loginPage.login(username, password);

        String alertText = basePage.handleAlertAndGetText();
        Assert.assertNotNull(alertText, "Expected an alert message but none was shown.");

        boolean isActualAlert = alertText.equals("Please fill out Username and Password.");
        Assert.assertTrue(isActualAlert, "Unexpected alert message: " + alertText);

        LoggerUtil.info("Testcase passed. Login correctly rejected for empty credentials.");
    }
    @DataProvider(name = "rememberLoginData")
    public Object[][] rememberLoginData() {
        return new ExcelReaderLogin().getFilteredTestData("remember");
    }
    @Test(dataProvider = "rememberLoginData", description = "Verifying if the login session is persistence after the browser restart")
    public void loginSessionPersistence(String username, String password, boolean expectedResult) {
        LoggerUtil.info("...TEST FOR SESSION PERSISTENCE...");

        loginPage.navigateToLoginPage();
        loginPage.login(username, password);

        WebElement logoutButton = new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.visibilityOfElementLocated(By.id("logout2")));

        boolean isLoggedIn = basePage.isElementDisplayed(logoutButton, "Logout Button");

        Assert.assertTrue(isLoggedIn, "Initial login failed for user: " + username);
        LoggerUtil.info("Initial login successful.");

        DriverManager.quitDriver();

        DriverManager.setBrowser("firefox");
        driver = DriverManager.getDriver();

        homePage = new HomePage(driver);
        homePage.navigateToWebsite();

        boolean isStillLoggedIn = basePage.isElementDisplayed(driver.findElement(By.id("logout2")), "Logout Button");

        if (!expectedResult) {
            LoggerUtil.warn("Testcase Passed: Session did not expire as expected");
            Assert.assertFalse(isStillLoggedIn, "User session persisted in the browser");
        } else {
            LoggerUtil.info("Testcase Failed: Session is not persistence as expected");
            Assert.assertTrue(isStillLoggedIn, "User was not remembered after browser restart");
        }
    }
}