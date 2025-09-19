package tests;

import data.ExcelReaderLogin;
import managers.ConfigManager;
import managers.DriverManager;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import pages.BasePage;
import pages.HomePage;
import pages.LoginPage;
import utils.LoggerUtil;
import utils.ScreenshotUtil;

import java.time.Duration;

public class LoginTests {
    private WebDriver driver;

    LoginPage loginPage;
    HomePage homePage;
    BasePage basePage;

    @BeforeMethod
    public void initialSetup() {
        LoggerUtil.info("Setting up test....");
        driver = DriverManager.getDriver();
        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();
        basePage = new BasePage();
        loginPage = new LoginPage();
        homePage = new HomePage();
    }

    @AfterMethod
    public void tearDown() {
        DriverManager.quitDriver();
        LoggerUtil.info("Finishing tests.");
    }
    @DataProvider(name = "validLoginData")
    public Object[][] validLoginData() {
//        return new ExcelReaderLogin().getLoginDataByScenario("valid");
        return new ExcelReaderLogin().getFilteredTestData("Login", "valid");
    }

    @Test(dataProvider = "validLoginData", description = "Verifying with all valid credentials")
    public void loginWithValidData(String username, String password, boolean expectedResult) {
        LoggerUtil.info("...TEST WITH VALID CREDENTIALS...");
        String testName = "loginWithValidData_" + username;


        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));

        loginPage.navigateToLoginPage();
        loginPage.login(username, password);

        BasePage basePage = new BasePage();

        if (basePage.isAlertPresent()) {
            String alertText = basePage.getAlertText();

            boolean isInvalidLogin = alertText.equals("Wrong password.") || alertText.equals("User does not exist.");
            Assert.assertTrue(isInvalidLogin, "Unexpected alert message: " + alertText);

//            ScreenshotUtil.captureFailScreenshot(testName);
            LoggerUtil.error("Testcase failed. Login failed for user: " + username);
            return;
        }
        WebElement logoutButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("logout2")));
        boolean actualResult = logoutButton.isDisplayed();

        Assert.assertEquals(actualResult, expectedResult, "Login result mismatch");

        LoggerUtil.info("Testcase passed. Login succeeded for user: " + username);
//        ScreenshotUtil.capturePassScreenshot(testName);
        homePage.logout();
    }

    @DataProvider(name = "invalidLoginData")
    public Object[][] invalidLoginData() {
        return new ExcelReaderLogin().getFilteredTestData("Login", "invalid");
    }
    @Test(dataProvider = "invalidLoginData", description = "Verifying with all invalid credentials")
    public void loginWithInvalidData(String username, String password, boolean expectedResult) {
        LoggerUtil.info("...TEST WITH INVALID CREDENTIALS...");
        String testName = "loginWithInvalidData_" + username;

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));

        loginPage.navigateToLoginPage();
        loginPage.login(username, password);

        BasePage basePage = new BasePage();

        if (basePage.isAlertPresent()) {
            String alertText = basePage.getAlertText();

            boolean isInvalidLogin = alertText.equals("Wrong password.") || alertText.equals("User does not exist.");
            Assert.assertTrue(isInvalidLogin, "Unexpected alert message: " + alertText);

//            ScreenshotUtil.capturePassScreenshot(testName);
            LoggerUtil.info("Testcase passed. Login correctly rejected for user: " + username);
            return;
        } else {
            WebElement logoutButton = driver.findElement(By.id("logout2"));
            boolean actualResult = basePage.isElementDisplayed(logoutButton, "Logout Button");

//            Assert.assertFalse(actualResult, "Login succeeded with invalid credentials"); //changed from false to not equals
            if (actualResult) {
//                ScreenshotUtil.captureFailScreenshot(testName);
                LoggerUtil.error("Testcase failed. Login unexpectedly succeeded for user: " + username);
                Assert.fail("Login succeeded with invalid credentials");
            }
        }
    }

    @DataProvider(name = "emptyFields")
    public Object[][] emptyLoginData() {
        return new ExcelReaderLogin().getFilteredTestData("Login", "empty");
    }
    @Test(dataProvider = "emptyFields", description = "Verifying with all empty credentials")
    public void loginWithEmptyFields(String username, String password, boolean expectedResult)
    {
        LoggerUtil.info("...TEST WITH EMPTY CREDENTIALS...");
        String testName = "loginWithEmptyFields_" + username;

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));


        loginPage.navigateToLoginPage();
        loginPage.login(username, password);

        if (basePage.isAlertPresent()) {
            String alertText = basePage.getAlertText();

            boolean isActualAlert = alertText.equals("Please fill out Username and Password.");
            Assert.assertTrue(isActualAlert, "Unexpected alert message: " + alertText);

            Assert.assertEquals(isActualAlert, expectedResult, "Expected login to fail but test data says TRUE");
//            ScreenshotUtil.capturePassScreenshot(testName);
            LoggerUtil.info("Testcase passed. Login correctly rejected for empty credentials.");
            return;
        } else {
            WebElement logoutButton = driver.findElement(By.id("logout2"));
            boolean actualResult = basePage.isElementDisplayed(logoutButton, "Logout Button");

            if (actualResult) {
//                ScreenshotUtil.captureFailScreenshot(testName);
                LoggerUtil.error("Login unexpectedly succeeded.");
                Assert.fail("Testcase failed. Login succeeded");
            } else {
                LoggerUtil.warn("Logout button not visible, but no alert was shown either.");
//                ScreenshotUtil.captureFailScreenshot(testName);
                Assert.fail("Testcase failed. No alert and no logout button—ambiguous login result");
            }
        }
    }

//    @DataProvider(name = "rememberLoginData")
//    public Object[][] rememberLoginData() {
//        return new ExcelReaderLogin().getFilteredTestData("Login", "remember");
//    }
//    @Test(dataProvider = "rememberLoginData", description = "Verifying Remember Me functionality")
//    public void loginWithRememberMe(String username, String password, boolean expectedResult) {
//      LoggerUtil.info("...TEST FOR REMEMBER ME...");
//        String testName = "loginWithRememberMe_" + username;
//
//
//
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
//
//
//        loginPage.navigateToLoginPage();
//        loginPage.login(username, password);
//
//        WebElement logoutButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("logout2")));
////        WebElement logoutButton = driver.findElement(By.id("logout2"));
//        boolean isLoggedIn = basePage.isElementDisplayed(logoutButton, "Logout Button");
//
//        Assert.assertTrue(isLoggedIn, "Login failed with Remember Me credentials");
//
//        DriverManager.quitDriver();
//        driver = DriverManager.getDriver(); // Reinitialize browser
//        driver.get(ConfigManager.getUrl("base_url"));

//        homePage = new HomePage();
//        basePage = new BasePage();
//
//        boolean isStillLoggedIn = basePage.isElementDisplayed(driver.findElement(By.id("logout2")), "Logout Button");
//
//        if (expectedResult) {
//            Assert.assertTrue(isStillLoggedIn, "User was not remembered after browser restart");
//            LoggerUtil.info("Session persisted as expected for user: " + username);
////            ScreenshotUtil.capturePassScreenshot(testName + "_sessionPersisted");
//        } else {
//            Assert.assertFalse(isStillLoggedIn, "User session persisted unexpectedly");
//            LoggerUtil.warn("Session did not expire as expected for user: " + username);
////            ScreenshotUtil.captureFailScreenshot(testName + "_sessionUnexpected");
//        }
//    }
}
