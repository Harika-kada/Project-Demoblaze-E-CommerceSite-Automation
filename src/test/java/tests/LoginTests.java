package tests;

import data.ExcelReaderLogin;
import managers.ConfigManager;
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
import utils.ScreenshotUtil;

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

        driver.get(ConfigManager.getUrl("base_url"));
    }

    @AfterMethod
    public void tearDown() {
        DriverManager.quitDriver();
        LoggerUtil.info("Finishing tests.");
    }

    @DataProvider(name = "validLoginData")
    public Object[][] validLoginData() {
        return new ExcelReaderLogin().getFilteredTestData("Login", "valid");
    }

    @Test(dataProvider = "validLoginData", description = "Verifying with all valid credentials")
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
        return new ExcelReaderLogin().getFilteredTestData("Login", "invalid");
    }

    @Test(dataProvider = "invalidLoginData", description = "Verifying with all invalid credentials")
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
        return new ExcelReaderLogin().getFilteredTestData("Login", "empty");
    }

    @Test(dataProvider = "emptyFields", description = "Verifying with all empty credentials")
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
}

//@Listeners(listeners.TestListener.class)
//public class LoginTests {
//    private WebDriver driver;
//
//    LoginPage loginPage;
//    HomePage homePage;
//    BasePage basePage;
//
//    @Parameters("browser")
//    @BeforeMethod
//    public void initialSetup(@Optional("firefox") String browser) {
//        LoggerUtil.info("Setting up test....");
//
//        DriverManager.setBrowser(browser);
//
//        driver = DriverManager.getDriver();
//        driver.manage().window().maximize();
//        driver.manage().deleteAllCookies();
//
//        basePage = new BasePage();
//        loginPage = new LoginPage();
//        homePage = new HomePage();
//    }
//
//    @AfterMethod
//    public void tearDown() {
//        DriverManager.quitDriver();
//        LoggerUtil.info("Finishing tests.");
//    }
//
//    //    @DataProvider(name = "loginData")
////    public Object[][] getLoginData() {
////        return new ExcelReaderLogin().getTestData("Login");
////        return new Object[][]{
////                {"Validuser", "validPass123"},
////        };
////    }
////    @Test(dataProvider = "loginData")
////    public void testLogin(String username, String password) {
////        String testName = "testLogin";
////        LoginPage loginPage = new LoginPage();
////        loginPage.navigateToLoginPage();
////        loginPage.login(username, password);
////
////        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(3));
////        try {
////            wait.until(ExpectedConditions.alertIsPresent());
////
////            Alert alert = DriverManager.getDriver().switchTo().alert();
////            String alertText = alert.getText();
////            LoggerUtil.warn("Alert displayed: " + alertText);
////            alert.accept();
////
////            boolean isInvalidLogin = alertText.equals("Wrong password.") || alertText.equals("User does not exist.");
////            Assert.assertTrue(isInvalidLogin, "Unexpected alert message: " + alertText);
////
////            ScreenshotUtil.captureFailScreenshot(testName);
////            LoggerUtil.error("Login failed for user: " + username);
////        }
////        catch (TimeoutException | NoAlertPresentException e) {
////            WebElement logoutButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("logout2")));
////            Assert.assertEquals(logoutButton.getText(), "Log out", "Login failed: Log out not found");
////
////            ScreenshotUtil.capturePassScreenshot(testName);
////            LoggerUtil.info("Login successful for user: " + username);
////        }
////    }
//    @DataProvider(name = "validLoginData")
//    public Object[][] validLoginData() {
////        return new ExcelReaderLogin().getLoginDataByScenario("valid");
//        return new ExcelReaderLogin().getFilteredTestData("Login", "valid");
//    }
//
//    @Test(dataProvider = "validLoginData", description = "Verifying with all valid credentials")
//    public void loginWithValidData(String username, String password, boolean expectedResult) {
//        LoggerUtil.info("...TEST WITH VALID CREDENTIALS...");
//        String testName = "loginWithValidData_" + username;
//
//
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
//
//        loginPage.navigateToLoginPage();
//        loginPage.login(username, password);
//
//        BasePage basePage = new BasePage();
//
//        if (basePage.isAlertPresent()) {
//            String alertText = basePage.getAlertText();
//
//            boolean isInvalidLogin = alertText.equals("Wrong password.") || alertText.equals("User does not exist.");
//            Assert.assertTrue(isInvalidLogin, "Unexpected alert message: " + alertText);
//
////            ScreenshotUtil.captureFailScreenshot(testName);
//            LoggerUtil.error("Testcase failed. Login failed for user: " + username);
//            return;
//        }
//        WebElement logoutButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("logout2")));
//        boolean actualResult = logoutButton.isDisplayed();
//
//        Assert.assertEquals(actualResult, expectedResult, "Login result mismatch");
//
//        LoggerUtil.info("Testcase passed. Login succeeded for user: " + username);
////        ScreenshotUtil.capturePassScreenshot(testName);
//        homePage.logout();
////        String testName = "loginWithValidData_" + username;
////        LoginPage loginPage = new LoginPage();
////        HomePage homePage = new HomePage();
////
////        loginPage.navigateToLoginPage();
////        loginPage.login(username, password);
////
////        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(3));
////
////        try {
////            WebElement logoutButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("logout2")));
////            boolean actualResult = logoutButton.isDisplayed();
////
////            Assert.assertEquals(actualResult, expectedResult, "Login failed: Login result mismatch");
////            ScreenshotUtil.capturePassScreenshot(testName);
////            LoggerUtil.info("Login successful for user: " + username);
////            homePage.logout();
////
////        } catch (TimeoutException e) {
////            if (ExpectedConditions.alertIsPresent().apply(DriverManager.getDriver()) != null) {
////                Alert alert = wait.until(ExpectedConditions.alertIsPresent());
////                String alertText = alert.getText();
////                alert.accept();
////
////                LoggerUtil.warn("Alert displayed: " + alertText);
////                boolean isInvalidLogin = alertText.equals("Wrong password.") || alertText.equals("User does not exist.");
////                Assert.assertTrue(isInvalidLogin, "Unexpected alert message: " + alertText);
////                ScreenshotUtil.captureFailScreenshot(testName);
////                LoggerUtil.error("TestCase failed for user: " + username);
////            } else {
////                ScreenshotUtil.captureFailScreenshot(testName);
////                LoggerUtil.error("Login failed with no alert for user: " + username);
////                Assert.fail("Login failed and no alert was present");
////            }
////        }
//
////        loginPage.navigateToLoginPage();
////        loginPage.login(username, password);
////
////        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(3));
////        WebElement logoutButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("logout2"))); //
////        boolean actualResult = logoutButton.isDisplayed();
////        try {
////        Assert.assertEquals(actualResult, expectedResult, "Login failed: Login result mismatch");
////        ScreenshotUtil.capturePassScreenshot(testName);
////        LoggerUtil.info("Login successful for user: " + username);
////        homePage.logout();
////        }
////        catch (AssertionError e) {
////        if(ExpectedConditions.alertIsPresent().apply(DriverManager.getDriver()) != null)
////        {
////            wait.until(ExpectedConditions.alertIsPresent());
////            Alert alert = DriverManager.getDriver().switchTo().alert();
////            String alertText = alert.getText();
////            LoggerUtil.warn("Alert displayed: " + alertText);
////
////            alert.accept();
////            boolean isInvalidLogin = alertText.equals("Wrong password.") || alertText.equals("User does not exist.");
////            Assert.assertTrue(isInvalidLogin, "Unexpected alert message: " + alertText);
////            ScreenshotUtil.captureFailScreenshot(testName);
////            LoggerUtil.error("TestCase failed for user: " + username);
////            throw e;
////        }
//    }
//
//    @DataProvider(name = "invalidLoginData")
//    public Object[][] invalidLoginData() {
//        return new ExcelReaderLogin().getFilteredTestData("Login", "invalid");
//    }
//    @Test(dataProvider = "invalidLoginData", description = "Verifying with all invalid credentials")
//    public void loginWithInvalidData(String username, String password, boolean expectedResult) {
////        String testName = "loginWithInvalidData_" + username;
////        LoginPage loginPage = new LoginPage();
////        HomePage homePage = new HomePage();
////
////        loginPage.navigateToLoginPage();
////        loginPage.login(username, password);
////
////        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(3));
////
////        try {
////            wait.until(ExpectedConditions.alertIsPresent());
////            Alert alert = DriverManager.getDriver().switchTo().alert();
////            String alertText = alert.getText();
////            LoggerUtil.warn("Alert displayed: " + alertText);
////            alert.accept();
////
////            boolean isInvalidLogin = alertText.equals("Wrong password.") || alertText.equals("User does not exist.");
////            Assert.assertTrue(isInvalidLogin, "Unexpected alert message: " + alertText);
////
////            ScreenshotUtil.capturePassScreenshot(testName);
////            LoggerUtil.error("Login failed for user: " + username );
////        }
////        catch (TimeoutException e) {
////            WebElement logoutButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("logout2"))); //
////            boolean actualResult = logoutButton.isDisplayed();
////            Assert.assertEquals(actualResult, expectedResult, "Testcase failed");
////
////            ScreenshotUtil.captureFailScreenshot(testName);
////            LoggerUtil.info("Login successful for user: " + username + ". Error: " + e.getMessage());
////            homePage.logout();
////        }
////        catch (Exception e) {
////            ScreenshotUtil.captureFailScreenshot(testName);
////            LoggerUtil.error("Unexpected exception for user: " + username + ". Error: " + e.getMessage());
////            throw e;
////        }
//        LoggerUtil.info("...TEST WITH INVALID CREDENTIALS...");
//        String testName = "loginWithInvalidData_" + username;
//
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
//
//        loginPage.navigateToLoginPage();
//        loginPage.login(username, password);
//
//        BasePage basePage = new BasePage();
//
//        if (basePage.isAlertPresent()) {
//            String alertText = basePage.getAlertText();
//
//            boolean isInvalidLogin = alertText.equals("Wrong password.") || alertText.equals("User does not exist.");
//            Assert.assertTrue(isInvalidLogin, "Unexpected alert message: " + alertText);
//
////            ScreenshotUtil.capturePassScreenshot(testName);
//            LoggerUtil.info("Testcase passed. Login correctly rejected for user: " + username);
//            return;
//        } else {
//            WebElement logoutButton = driver.findElement(By.id("logout2"));
//            boolean actualResult = basePage.isElementDisplayed(logoutButton, "Logout Button");
//
////            Assert.assertFalse(actualResult, "Login succeeded with invalid credentials"); //changed from false to not equals
//            if (actualResult) {
////                ScreenshotUtil.captureFailScreenshot(testName);
//                LoggerUtil.error("Testcase failed. Login unexpectedly succeeded for user: " + username);
//                Assert.fail("Login succeeded with invalid credentials");
//            }
//        }
//    }
//
//    @DataProvider(name = "emptyFields")
//    public Object[][] emptyLoginData() {
//        return new ExcelReaderLogin().getFilteredTestData("Login", "empty");
//    }
//    @Test(dataProvider = "emptyFields", description = "Verifying with all empty credentials")
//    public void loginWithEmptyFields(String username, String password, boolean expectedResult)
//    {
//        LoggerUtil.info("...TEST WITH EMPTY CREDENTIALS...");
//        String testName = "loginWithEmptyFields_" + username;
//
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
//
//
//        loginPage.navigateToLoginPage();
//        loginPage.login(username, password);
//
//        if (basePage.isAlertPresent()) {
//            String alertText = basePage.getAlertText();
//
//            boolean isActualAlert = alertText.equals("Please fill out Username and Password.");
//            Assert.assertTrue(isActualAlert, "Unexpected alert message: " + alertText);
//
//            Assert.assertEquals(isActualAlert, expectedResult, "Expected login to fail but test data says TRUE");
////            ScreenshotUtil.capturePassScreenshot(testName);
//            LoggerUtil.info("Testcase passed. Login correctly rejected for empty credentials.");
//            return;
//        } else {
//            WebElement logoutButton = driver.findElement(By.id("logout2"));
//            boolean actualResult = basePage.isElementDisplayed(logoutButton, "Logout Button");
//
//            if (actualResult) {
////                ScreenshotUtil.captureFailScreenshot(testName);
//                LoggerUtil.error("Login unexpectedly succeeded.");
//                Assert.fail("Testcase failed. Login succeeded");
//            } else {
//                LoggerUtil.warn("Logout button not visible, but no alert was shown either.");
////                ScreenshotUtil.captureFailScreenshot(testName);
//                Assert.fail("Testcase failed. No alert and no logout button—ambiguous login result");
//            }
//        }
//    }
//
////    @DataProvider(name = "rememberLoginData")
////    public Object[][] rememberLoginData() {
////        return new ExcelReaderLogin().getFilteredTestData("Login", "remember");
////    }
////    @Test(dataProvider = "rememberLoginData", description = "Verifying Remember Me functionality")
////    public void loginWithRememberMe(String username, String password, boolean expectedResult) {
////      LoggerUtil.info("...TEST FOR REMEMBER ME...");
////        String testName = "loginWithRememberMe_" + username;
////
////
////
////        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
////
////
////        loginPage.navigateToLoginPage();
////        loginPage.login(username, password);
////
////        WebElement logoutButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("logout2")));
//////        WebElement logoutButton = driver.findElement(By.id("logout2"));
////        boolean isLoggedIn = basePage.isElementDisplayed(logoutButton, "Logout Button");
////
////        Assert.assertTrue(isLoggedIn, "Login failed with Remember Me credentials");
////
////        // Simulate browser close and reopen
////        DriverManager.quitDriver();
////        driver = DriverManager.getDriver(); // Reinitialize browser
////        driver.get(ConfigManager.getUrl("base_url"));
////
////        // Reinitialize page objects
////        homePage = new HomePage();
////        basePage = new BasePage();
////
////        // Check if user is still logged in
////        boolean isStillLoggedIn = basePage.isElementDisplayed(driver.findElement(By.id("logout2")), "Logout Button");
////
////        if (expectedResult) {
////            Assert.assertTrue(isStillLoggedIn, "User was not remembered after browser restart");
////            LoggerUtil.info("Session persisted as expected for user: " + username);
//////            ScreenshotUtil.capturePassScreenshot(testName + "_sessionPersisted");
////        } else {
////            Assert.assertFalse(isStillLoggedIn, "User session persisted unexpectedly");
////            LoggerUtil.warn("Session did not expire as expected for user: " + username);
//////            ScreenshotUtil.captureFailScreenshot(testName + "_sessionUnexpected");
////        }
////    }
//}
