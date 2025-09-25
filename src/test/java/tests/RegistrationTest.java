package tests;

import data.ExcelReaderRegistration;
import managers.ConfigManager;
import managers.DriverManager;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import pages.BasePage;
import pages.RegistrationPage;
import utils.LoggerUtil;

import java.time.Duration;

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
		return new ExcelReaderRegistration().provideExcelData("valid");
	}

	@Test(dataProvider = "validRegistrationData", description = "Verifying with all valid credentials")
	public void testSignUp(String username, String password, boolean expectedResult) {
		LoggerUtil.info("...TEST SIGNUP FOR USER: " + username + "...");

		signUpPage.navigateToSignUpPage();
		signUpPage.signUp(username, password);

		String alertText = basePage.handleAlertAndGetText();
		Assert.assertNotNull(alertText, "Expected an alert message after registration but none was shown.");

		if (alertText.equals("Sign up successful.")) {
			Assert.assertTrue(expectedResult, "Test passed, but expectedResult was FALSE.");
			LoggerUtil.info("Testcase passed. Registration successful for user: " + username);

		} else if (alertText.equals("This user already exist.")) {
			LoggerUtil.warn("Registration blocked: User already exists - " + username);
			Assert.fail("Testcase failed. Registration blocked: User already exists.");

		} else {
			LoggerUtil.error("Unexpected alert message: " + alertText);
			Assert.fail("Testcase failed. Signup failed: Unexpected alert message: " + alertText);
		}
	}
}
