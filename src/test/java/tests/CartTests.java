package tests;

import data.ExcelReaderCart;
import managers.ConfigManager;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;
import org.openqa.selenium.WebDriver;
import pages.HomePage;
import pages.ProductPage;
import pages.CartPage;
import managers.DriverManager;

import org.testng.Assert;
import utils.LoggerUtil;

import java.time.Duration;

@Listeners(listeners.TestListener.class)
public class CartTests {

    private WebDriver driver;
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

    @Test(dataProvider = "addTocart", description = "Verifying add to cart functionality")
    public void testAddProductToCart(String productName, String action, double expectedCount, double expectedPrice) {
        LoggerUtil.info("...TESTING ADD TO CART FUNCTIONALITY...");

        homePage.isHomePageLoaded();
        homePage.selectProduct(productName);
        productPage.isProductPageLoaded(productName);
        productPage.addToCart();
        cartPage.goToCart();

        Assert.assertTrue(cartPage.isProductInCart(productName),
                "The product " + productName + " was not found in the cart.");

        int actualCount = cartPage.getCartItemCount();
        double actualPrice = Double.parseDouble(cartPage.getTotalPrice());

        Assert.assertEquals(actualCount, (int) expectedCount, "Cart item count mismatch");
        Assert.assertEquals(actualPrice, expectedPrice, "Cart price mismatch");

        LoggerUtil.info("TestCase passed. Product '" + productName + "' found in the cart.");
    }

    @DataProvider(name = "removeFromcart")
    public Object[][] removeFromCartData() {
        return new ExcelReaderCart().getFilteredTestData("Cart", "Remove");
    }

    @Test(dataProvider = "removeFromcart", description = "Verifying if the product is removed from the cart")
    public void testRemoveProductFromCart(String productName, String action, double expectedCount,
                                          double expectedPrice) {
        LoggerUtil.info("...TESTING REMOVING FROM CART FUNCTIONALITY FOR: " + productName + "...");

        homePage.isHomePageLoaded();
        homePage.selectProduct(productName);
        productPage.isProductPageLoaded(productName);
        productPage.addToCart();

        cartPage.goToCart();
        Assert.assertTrue(cartPage.isProductInCart(productName),
                "The product to be removed was not found in the cart.");

        cartPage.removeProduct(productName);
        Assert.assertFalse(cartPage.isProductInCart(productName),
                "Testcase failed. The product " + productName + " was NOT successfully removed from the cart.");

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
    }
    @DataProvider(name = "multipleAdd")
    public Object[][] multipleAddData() {
        return new ExcelReaderCart().getFilteredTestData("Cart", "MultipleAdd");
    }

    @Test(dataProvider = "multipleAdd", description = "Verifying cart quantity updating the same product multiple times.")
    public void testUpdateQuantity(String productName, String action, double expectedCount, double expectedPrice) {
        LoggerUtil.info("...TESTING QUANTITY UPDATE BY MULTIPLE ADDITION for: " + productName + "...");

        homePage.selectProduct(productName);
        double singlePrice = productPage.getProductPrice();
        productPage.addToCart();
        cartPage.goToCart();

        Assert.assertTrue(cartPage.isProductInCart(productName),
                "The product " + productName + " was not found in the cart.");

        LoggerUtil.info("Added first unit of " + productName + " at price: " + singlePrice);

        homePage.navigateToWebsite();
        homePage.selectProduct(productName);
        productPage.addToCart();
        cartPage.goToCart();

        Assert.assertTrue(cartPage.isProductInCart(productName),
                "The product " + productName + " was not found in the cart.");

        String rawActualPrice = cartPage.getTotalPrice();
        double actualPrice = Double.parseDouble(rawActualPrice);

        Assert.assertEquals(actualPrice, expectedPrice,
                "Testcase failed. Total price mismatch. Expected " + expectedPrice + ", but got " + actualPrice);

        LoggerUtil.info("TestCase passed. Quantity update verified. Total price: " + actualPrice);
    }

    @Test(description = "Verifying cart persistence after browser restart")
    public void testCartPersistence() {
        LoggerUtil.info("...TESTING CART PERSISTENCE ACROSS SESSIONS...");

        String productName = ConfigManager.getProperty("product.name");
        homePage.selectProduct(productName);
        productPage.addToCart();

        DriverManager.quitDriver();

        DriverManager.setBrowser("firefox");
        driver = DriverManager.getDriver();

        homePage = new HomePage(driver);
        cartPage = new CartPage(driver);
        productPage = new ProductPage(driver);

        homePage.navigateToWebsite();
        cartPage.goToCart();

        Assert.assertTrue(cartPage.isProductInCart(productName),
                "Testcase failed: Product was not found after restarting session.");
        Assert.assertTrue(cartPage.getCartItemCount() > 0, "Testcase failed: Cart should persist across sessions");
        LoggerUtil.info("Testcase passed. Product found in the cart after browser restart");
    }

    @Test(description = "Verifying price calculation of products in cart")
    public void testPriceCalculationVerification() {
        LoggerUtil.info("...TESTING PRICE CALCULATION IN CART...");

        String productName = ConfigManager.getProperty("product.name");
        homePage.selectProduct(productName);

        double originalPrice = productPage.getProductPrice();

        productPage.addToCart();

        cartPage.goToCart();

        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(3));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//td[text()='" + productName + "']")));

        String totalPriceInCartString = cartPage.getTotalPrice();
        double totalPriceInCart = Double.parseDouble(totalPriceInCartString);


        if (totalPriceInCartString != null && !totalPriceInCartString.trim().isEmpty()) {
            LoggerUtil.info("Testcase passed. Expected total price shown in the cart.");
        } else {
            totalPriceInCart = 0.0;
            LoggerUtil.error("Total cart price element was empty, which is unexpected for a successful add-to-cart.");
            Assert.assertNull(totalPriceInCartString, "Total price element was empty, cannot verify price.");
        }
        Assert.assertEquals(totalPriceInCart, originalPrice,
                "The price in the cart (" + totalPriceInCart + ") does not match the product's original price (" + originalPrice + ").");
    }
}