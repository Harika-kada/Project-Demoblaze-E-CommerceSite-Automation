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

    private Map<String, String> getScenario(String userType, String address, String payment) {
        List<Map<String, String>> dataList = new ExcelReaderCheckout().getMappedData("Checkout");
        return dataList.stream()
                .filter(row -> row.get("User Type").equalsIgnoreCase(userType)
                        && row.get("Shipping Address").equals(address)
                        && row.get("Payment Method").equals(payment))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Scenario not found for: " + userType + ", " + address + ", " + payment));
    }

    private void runCheckoutTest(Map<String, String> data) {

        homePage.navigateToWebsite();
        String userType = data.get("User Type");

        checkoutPage.simulateUser(userType);

        homePage.selectProduct("Sony vaio i5");
        productPage.addToCart();
        cartPage.goToCart();

        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(3));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//td[text()='Sony vaio i5']")));

        String expectedMessage = data.get("Expected Message");

        String name = data.get("Name");
        String country = data.get("Country");
        String city = data.get("City");
        String card = data.get("Payment Method");
        String month = data.get("Month");
        String year = data.get("Year");

        checkoutPage.clickPlaceOrder();

        checkoutPage.fillCheckoutForm(name, country, city, card, month, year);
        checkoutPage.submitOrder();

        String actualMessage;

        if (basePage.isAlertPresent()) {
            actualMessage = basePage.handleAlertAndGetText();
        } else {
            actualMessage = checkoutPage.getConfirmationMessage();
        }

        Assert.assertEquals(actualMessage.trim(), expectedMessage.trim(), "Validation failed for user type: " + userType);
    }
    @Test
    public void testForGuestCheckout() {
        Map<String, String> data = getScenario("Guest", "123 Main St, NY", "Credit Card");
        runCheckoutTest(data);
    }
    @Test
    public void testForRegisteredCheckout() {
        Map<String, String> data = getScenario("Registered", "456 Elm St, CA", "PayPal");
        runCheckoutTest(data);
    }
    @Test
    public void testWithMissingShippingAddress() {
        Map<String, String> data = getScenario("Guest", "", "Credit Card");
        runCheckoutTest(data);
    }
    @Test
    public void testWithMissingPaymentMethod() {
        Map<String, String> data = getScenario("Registered", "789 Oak St, TX", "");
        runCheckoutTest(data);
    }
    @Test
    public void testGuestCheckoutWithDebitCard() {
        Map<String, String> data = getScenario("Guest", "321 Pine St, FL", "Debit Card");
        runCheckoutTest(data);
    }
}
