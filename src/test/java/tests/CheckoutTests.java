package tests;

import managers.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.*;
import data.ExcelReaderCheckout;
import utils.LoggerUtil;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Listeners(listeners.TestListener.class)
public class CheckoutTests {
    private WebDriver driver;
    private CheckoutPage checkoutPage;
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

        homePage = new HomePage(driver);
        productPage = new ProductPage(driver);
        cartPage = new CartPage(driver);
        checkoutPage = new CheckoutPage(driver);
        basePage = new BasePage(driver);
    }

    @AfterMethod
    public void tearDown() {
        DriverManager.quitDriver();
        LoggerUtil.info("Finishing tests.");
    }

    @DataProvider(name = "checkoutData")
    public Object[][] getCheckoutData() {
        List<Map<String, String>> dataList = new ExcelReaderCheckout().getMappedData("Checkout");
        Object[][] dataArray = new Object[dataList.size()][1];
        for (int i = 0; i < dataList.size(); i++) {
            dataArray[i][0] = dataList.get(i);
        }
        return dataArray;
    }

    private Map<String, String> getScenario(String userType, String name) {
        List<Map<String, String>> dataList = new ExcelReaderCheckout().getMappedData("Checkout");
        return dataList.stream()
                .filter(row -> row.get("User Type").equalsIgnoreCase(userType)
                        && row.get("Name").equals(name))
                .findFirst().orElseThrow(() -> new RuntimeException(
                        "Scenario not found for: " + userType + ", " + name));
    }

    private String runCheckoutTest(Map<String, String> data) {
        homePage.navigateToWebsite();
        String userType = data.get("User Type");

        checkoutPage.simulateUser(userType);

        homePage.selectProduct("Nokia lumia 1520");
        productPage.addToCart();
        cartPage.goToCart();

        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//td[text()='Nokia lumia 1520']")));

        String expectedMessage = data.get("Expected Message");

        String name = data.get("Name");
        String country = data.get("Country");
        String city = data.get("City");
        String card = data.get("Card");
        String month = data.get("Month");
        String year = data.get("Year");

        checkoutPage.clickPlaceOrder();

        checkoutPage.fillCheckoutForm(name, country, city, card, month, year);
        checkoutPage.submitOrder();

        return expectedMessage;
    }

    private String getActualMessage() {
        if (basePage.isAlertPresent()) {
            return basePage.handleAlertAndGetText();
        } else {
            return checkoutPage.getConfirmationMessage();
        }
    }

    @Test(description = "Verifying for Guest checkout")
    public void testForGuestCheckout() {
        LoggerUtil.info("...TEST FOR GUEST USER CHECKOUT...");

        Map<String, String> data = getScenario("Guest", "GuestUser1");

        String expectedMessage = runCheckoutTest(data);
        String actualMessage = getActualMessage();

        Assert.assertEquals(actualMessage.trim(), expectedMessage.trim(),
                "Validation failed for scenario: " + data.get("Name"));
    }

    @Test(description = "Verifying for Registered user checkout")
    public void testForRegisteredCheckout() {
        LoggerUtil.info("...TEST FOR REGISTERED USER CHECKOUT...");

        Map<String, String> data = getScenario("Registered", "RegisteredUser1");

        String expectedMessage = runCheckoutTest(data);
        String actualMessage = getActualMessage();

        Assert.assertEquals(actualMessage.trim(), expectedMessage.trim(),
                "Validation failed for scenario: " + data.get("Name"));
    }

    @Test(description = "Verifying checkout failure due to missing Shipping Address")
    public void testWithMissingShippingAddress() {
        LoggerUtil.info("...TEST FOR CHECKOUT WITHOUT SHIPPING ADDRESS...");

        Map<String, String> data = getScenario("Guest", "GuestUser2");

        String expectedMessage = runCheckoutTest(data);
        String actualMessage = getActualMessage();

        Assert.assertNotEquals(actualMessage.trim(), expectedMessage.trim(),
                "Validation failed for scenario: " + data.get("Name"));

    }

    @Test(description = "Verifying checkout failure due to missing Payment Method")
    public void testWithMissingPaymentMethod() {
        LoggerUtil.info("...TEST FOR CHECKOUT WITHOUT PAYMENT METHOD...");

        Map<String, String> data = getScenario("Registered", "RegisteredUser2");

        String expectedMessage = runCheckoutTest(data);
        String actualMessage = getActualMessage();

        Assert.assertEquals(actualMessage.trim(), expectedMessage.trim(),
                "Validation failed for scenario: " + data.get("Name"));
    }
}