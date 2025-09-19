package tests;

import data.ExcelReaderLogin;
import data.ExcelReaderRegistration;
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

import pages.RegistrationPage;
import utils.LoggerUtil;

import java.time.Duration;

public class RegistrationTest
{
    @BeforeMethod
    public void initialSetup() {
        LoggerUtil.info("Setting up test....");
        DriverManager.getDriver().manage().window().maximize();
        DriverManager.getDriver().manage().deleteAllCookies();
    }
    @AfterMethod
    public void tearDown() {
        DriverManager.quitDriver();
        LoggerUtil.info("Finishing tests.");
    }
    @DataProvider(name = "validRegistrationData")
    public Object[][] validRegistrationData() {
        return new ExcelReaderRegistration().provideExcelData("valid");
    }
    @Test(dataProvider = "validRegistrationData", description = "Verifying with all valid credentials")
    public void testSignUp(String username, String password, boolean expectedResult) {
        String testName = "testSignUp";

        RegistrationPage signUpPage = new RegistrationPage();
        signUpPage.navigateToSignUpPage();
        signUpPage.signUp(username, password);

        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(3));
        try {
            wait.until(ExpectedConditions.alertIsPresent());

            Alert alert = DriverManager.getDriver().switchTo().alert();
            String alertText = alert.getText();
            alert.accept();
            LoggerUtil.info("Alert displayed: " + alertText);

            if (alertText.equals("Sign up successful.")) {
//                ScreenshotUtil.capturePassScreenshot(testName);
                LoggerUtil.info("Testcase passed. Registration successful for user: " + username);
            } else if (alertText.equals("This user already exist.")) {
//                ScreenshotUtil.captureFailScreenshot(testName);
                LoggerUtil.warn("Testcase failed. Registration failed: User already exists - " + username);
                Assert.fail("Testcase failed. Registration failed: Duplicate user.");
            } else {
//                ScreenshotUtil.captureFailScreenshot(testName);
                LoggerUtil.error("Unexpected alert message: " + alertText);
                Assert.fail("Testcase failed. Signup failed: Unexpected alert message.");
            }

//            boolean isInvalidSignIn = alertText.equals("This user already exist.");
//            Assert.assertTrue(isInvalidSignIn, "Unexpected alert message: " + alertText);
//            ScreenshotUtil.captureFailScreenshot(testName);
//            LoggerUtil.error("SignUp failed for user: " + ConfigManager.getProperty("valid.username"));
        }
        catch (TimeoutException | NoAlertPresentException e) {
//            ScreenshotUtil.capturePassScreenshot(testName);
//            LoggerUtil.info("Signup successful for user: " + ConfigManager.getProperty("valid.username"));
//            ScreenshotUtil.captureFailScreenshot(testName);
            LoggerUtil.error("Testcase failed. Signup failed: No alert was displayed.");
            Assert.fail("Testcase failed. Signup failed: No alert was displayed.");
        }
    }
}
