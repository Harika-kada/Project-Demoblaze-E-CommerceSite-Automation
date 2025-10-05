package tests;

import data.ExcelReaderRegistration;
import managers.ConfigManager;
import managers.DriverManager;

import org.openqa.selenium.*;
import org.testng.Assert;
import org.testng.annotations.*;

import pages.BasePage;
import pages.RegistrationPage;
import utils.LoggerUtil;

@Listeners(listeners.TestListener.class)
public class RegistrationTest {
    private WebDriver driver;
    BasePage basePage;
    RegistrationPage signUpPage;

    @Parameters("browser")
    @BeforeMethod
    public void initialSetup(@Optional("firefox") String browser) {
        LoggerUtil.info("Setting up test....");

        DriverManager.setBrowser(browser);

        driver = DriverManager.getDriver();
        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();

        basePage = new BasePage(driver);
        signUpPage = new RegistrationPage(driver);
        driver.get(ConfigManager.getUrl("base_url"));
    }

    @AfterMethod
    public void tearDown() {
        DriverManager.quitDriver();
        LoggerUtil.info("Finishing tests.");
    }

    @DataProvider(name = "validRegistrationData")
    public Object[][] validRegistrationData() {
        return new ExcelReaderRegistration().getFilteredTestData("valid");
    }

    @Test(dataProvider = "validRegistrationData", description = "Verifying signup with new valid credentials")
    public void signUpWithNewCredentials(String username, String password, boolean expectedResult) {
        LoggerUtil.info("...TEST SIGNUP FOR NEW USER: " + username + "...");

        signUpPage.navigateToSignUpPage();
        signUpPage.signUp(username, password);

        String alertText = basePage.handleAlertAndGetText();
        Assert.assertNotNull(alertText, "Expected an alert message after registration but none was shown.");

        boolean actualSuccess = alertText.equals("Sign up successful.");

        if (actualSuccess) {
            LoggerUtil.info("Testcase Passed. Registration successful for user: " + username);
        } else {
            LoggerUtil.error("Testcase Failed. Registration failed: " + alertText);
        }

        Assert.assertEquals(actualSuccess, expectedResult, "Registration failed. Alert received: " + alertText);
    }

    @DataProvider(name = "existingRegistrationData")
    public Object[][] invalidRegistrationData() {
        return new ExcelReaderRegistration().getFilteredTestData("invalid");
    }

    @Test(dataProvider = "existingRegistrationData", description = "Verifying signup with existing credentials")
    public void signUpWithExistingCredentials(String username, String password, boolean expectedResult) {
        LoggerUtil.info("...TEST SIGNUP FOR EXISTING USER: " + username + "...");

        signUpPage.navigateToSignUpPage();
        signUpPage.signUp(username, password);

        String alertText = basePage.handleAlertAndGetText();
        Assert.assertNotNull(alertText, "Expected an alert message after registration but none was shown.");

        boolean actualBlock = alertText.equals("This user already exist.");

        if (actualBlock) {
            LoggerUtil.warn("Testcase Passed. Registration correctly failed: User already exists - " + username);
        } else {
            LoggerUtil.error("Testcase Failed. Registration successful for existing user: " + alertText);
        }

        Assert.assertNotEquals(actualBlock, expectedResult,
                "Registration successful for existing user. Alert received: " + alertText);
    }
}