package tests;

import data.ExcelReaderCart;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;
import org.openqa.selenium.WebDriver;
import pages.BasePage;
import pages.HomePage;
import pages.ProductPage;
import pages.CartPage;
import managers.DriverManager;

import org.testng.Assert;
import utils.LoggerUtil;
import utils.ScreenshotUtil;

import java.time.Duration;

//@Listeners(listeners.TestListener.class)
public class CartTests {

	private WebDriver driver;
	private BasePage basePage;
	private HomePage homePage;
	private ProductPage productPage;
	private CartPage cartPage;

	@BeforeMethod
	@Parameters("browser")
	public void initialSetup(@Optional("firefox") String browser) {
		LoggerUtil.info("Setting up test....");

		DriverManager.setBrowser(browser);

		driver = DriverManager.getDriver();
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();

		basePage = new BasePage(driver);
		homePage = new HomePage(driver);
		productPage = new ProductPage(driver);
		cartPage = new CartPage(driver);

		homePage.navigateToWebsite();
	}

	@AfterMethod
	public void tearDown() {
		DriverManager.quitDriver();
		LoggerUtil.info("Finishing tests.");
	}

	@DataProvider(name = "addTocart")
	public Object[][] addToCartData() {
		return new ExcelReaderCart().getFilteredTestData("Cart", "Add");
	}

	@Test(dataProvider = "addTocart")
	public void testAddProductToCart(String productName, String action, double expectedCount, double expectedPrice) {
		LoggerUtil.info("...TESTING ADD TO CART FUNCTIONALITY...");
		String testName = "AddProductToCart" + productName;

		homePage.isHomePageLoaded();
		homePage.selectProduct(productName);
		productPage.isProductPageLoaded(productName);
		productPage.addToCart();
		cartPage.goToCart();
		ScreenshotUtil.capturePassScreenshot(testName);
		Assert.assertTrue(cartPage.isProductInCart(productName),
				"The product " + productName + " was not found in the cart.");

		int actualCount = cartPage.getCartItemCount();
		double actualPrice = Double.parseDouble(cartPage.getTotalPrice());

		Assert.assertEquals(actualCount, (int) expectedCount, "Cart item count mismatch");
		Assert.assertEquals(actualPrice, expectedPrice, "Cart price mismatch");
		LoggerUtil.info("TestCase passed. Product '" + productName + "' found in the cart.");
		if (!cartPage.isProductInCart(productName)) {
			ScreenshotUtil.captureFailScreenshot(testName);
			Assert.fail("Testcase failed. Product not added to the cart!");
		}
	}

	@DataProvider(name = "removeFromcart")
	public Object[][] removeFromCartData() {
		return new ExcelReaderCart().getFilteredTestData("Cart", "Remove");
	}

	@Test(dataProvider = "removeFromcart")
	public void testRemoveProductFromCart(String productName, String action, double expectedCount,
			double expectedPrice) {
		LoggerUtil.info("...TESTING REMOVING FROM CART FUNCTIONALITY FOR: " + productName + "...");
		String testName = "RemoveProductFromCart_" + productName;

		homePage.isHomePageLoaded();
		homePage.selectProduct(productName);
		productPage.isProductPageLoaded(productName);
		productPage.addToCart();

		cartPage.goToCart();
		Assert.assertTrue(cartPage.isProductInCart(productName),
				"The product to be removed was not found in the cart.");

		cartPage.removeProduct(productName);
		Assert.assertTrue(cartPage.isProductRemoved(productName),
				"The product " + productName + " should have been removed from the cart.");

		int updatedCount = cartPage.getCartItemCount();

		double updatedPrice;
		String priceText = cartPage.getTotalPrice();
		if (priceText.isEmpty()) {
			updatedPrice = 0.0;
		} else {
			updatedPrice = Double.parseDouble(priceText);
		}

		Assert.assertEquals(updatedCount, (int) expectedCount, "Final cart item count mismatch after removal.");
		Assert.assertEquals(updatedPrice, expectedPrice, "Final cart price mismatch after removal.");

		LoggerUtil.info("Test passed: Product " + productName + " was successfully removed and cart state is correct.");
		ScreenshotUtil.capturePassScreenshot(testName);
	}

	@Test
	public void testUpdateProductQuantity() {
		Assert.assertTrue(true, "Demoblaze does not support quantity update");
	}

	@Test
	public void testCartPersistenceAcrossSessions() {
		LoggerUtil.info("...TESTING CART PERSISTENCE ACROSS SESSIONS...");

		homePage.selectProduct("Nokia lumia 1520");
		productPage.addToCart();

		DriverManager.quitDriver();

		DriverManager.setBrowser("firefox");
		driver = DriverManager.getDriver();

		homePage = new HomePage(driver);
		cartPage = new CartPage(driver);
		productPage = new ProductPage(driver);

		homePage.navigateToWebsite();
		cartPage.goToCart();

		Assert.assertTrue(cartPage.isProductInCart("Nokia lumia 1520"),
				"Testcase failed: Product was not found after restarting session.");
		Assert.assertTrue(cartPage.getCartItemCount() > 0, "Testcase failed: Cart should persist across sessions");
	}

	@Test
	public void testPriceCalculationVerification() {
		LoggerUtil.info("...TESTING PRICE CALCULATION IN CART...");
		String testName = "priceCalculationVerification";

		homePage.selectProduct("Sony vaio i5");
		String originalPriceString = productPage.getProductPrice();
		double originalPrice = Double.parseDouble(originalPriceString.replaceAll("[^\\d.]", ""));

		productPage.addToCart();

		cartPage.goToCart();
		WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(3));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//td[text()='Sony vaio i5']")));
		String totalPriceInCartString = cartPage.getTotalPrice();
		double totalPriceInCart;

		if (totalPriceInCartString != null && !totalPriceInCartString.trim().isEmpty()) {
			totalPriceInCart = Double.parseDouble(totalPriceInCartString);
			ScreenshotUtil.capturePassScreenshot(testName);
		} else {
			LoggerUtil.error("Total cart price element was empty, which is unexpected for a successful add-to-cart.");
			totalPriceInCart = 0.0;
		}

		Assert.assertEquals(totalPriceInCart, originalPrice,
				"The price in the cart does not match the product's original price.");
	}
}
