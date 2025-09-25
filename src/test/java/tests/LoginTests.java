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
