package tests;

import data.ExcelReaderSearch;
import managers.DriverManager;
import org.openqa.selenium.WebDriver;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import pages.HomePage;

import utils.LoggerUtil;
import utils.ScreenshotUtil;

import java.time.Duration;

@Listeners(listeners.TestListener.class)
public class SearchTests {

    private WebDriver driver;
    private HomePage homePage;

    @Parameters("browser")
    @BeforeMethod
    public void initialSetup(@Optional("firefox") String browser) {
        LoggerUtil.info("Setting up test....");

        DriverManager.setBrowser(browser);
        driver = DriverManager.getDriver();
        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();

        homePage = new HomePage(driver);
        homePage.navigateToWebsite();
        Assert.assertTrue(homePage.isHomePageLoaded(), "Homepage did not load properly in setup.");
    }
    @AfterMethod
    public void tearDown() {
        DriverManager.quitDriver();
        LoggerUtil.info("Finishing tests.");
    }
    private WebDriverWait getWait() {
        return new WebDriverWait(driver, Duration.ofSeconds(5));
    }
    @DataProvider(name = "searchByProduct")
    public Object[][] searchByProduct() {
        return new ExcelReaderSearch().getFilteredSearchData("product", "Sony");
    }
    @Test(dataProvider = "searchByProduct", description = "Searching by product name")
    public void testSearchByProductName(String productName, String category)
    {
        LoggerUtil.info("...SEARCH BY PRODUCT NAME...");
        String testName = "testSearchByProduct_" + productName;

        homePage.navigateToWebsite();
        Assert.assertTrue(homePage.isHomePageLoaded(), "Homepage did not load properly");
        LoggerUtil.info("Checking visibility of product: " + productName);
        if (!homePage.isProductVisible(productName)) {
            ScreenshotUtil.captureFailScreenshot(testName);
            LoggerUtil.error("Product '" + productName + "' is not visible.");
            Assert.fail("TestCase failed. Product '" + productName + "' is not visible.");
        }

        LoggerUtil.info("TestCase passed. Product '" + productName + "' is visible.");
        ScreenshotUtil.capturePassScreenshot(testName);
    }

    @DataProvider(name = "searchByCategory")
    public Object[][] searchByCategory() {
        String category = System.getProperty("test.category", "Monitors");
        return new ExcelReaderSearch().getFilteredSearchData("category", category);
    }
    @Test(dataProvider = "searchByCategory", description = "Searching by category")
    public void testSearchByCategory(String productName, String category)
    {
        LoggerUtil.info("...SEARCH BY CATEGORY: " + category + " FOR PRODUCT: " + productName + "...");

        LoggerUtil.info("Selecting category: " + category);
        Assert.assertTrue(homePage.isValidCategory(category), "Invalid category: " + category);
        homePage.selectCategory(category);

        getWait().until(ExpectedConditions.visibilityOfAllElements(homePage.getProductTitles()));

        WebElement productElement = homePage.getProductElement(productName);
        Assert.assertNotNull(productElement,"TestCase failed. Expected product '" + productName + "' to be visible under category '" + category + "', but it was not found.");

        getWait().until(ExpectedConditions.visibilityOf(productElement));

        LoggerUtil.info("TestCase passed. Product '" + productName + "' is visible under category '" + category + "'");
    }
    @Test
    public void testSearchWithNoResults() {

        homePage.selectCategory("Phones");
        Assert.assertFalse(homePage.isProductVisible("NonExistentProduct"),"TestCase failed. Found an unexpected product ('NonExistentProduct') after filtering.");

        LoggerUtil.info("TestCase passed. Product not found as expected.");
    }

//    @Test
//    public void testSearchMultipleProducts() {
//        homePage.selectCategory("Laptops");
//        LoggerUtil.info("Searching for category: Laptops");
//        Assert.assertTrue(homePage.isProductVisible("Sony vaio i5"));
//        Assert.assertTrue(homePage.isProductVisible("MacBook air"));
//    }
}
