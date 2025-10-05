package tests;

import data.ExcelReaderSearch;
import managers.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import pages.HomePage;

import utils.LoggerUtil;

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
        return new ExcelReaderSearch().getFilteredSearchData("product", "MacBook");
    }

    @Test(dataProvider = "searchByProduct", description = "Verifying search functionality by product name")
    public void testSearchByProductName(String productName, String category) {
        LoggerUtil.info("...SEARCH BY PRODUCT NAME " + productName + "...");

        By productLocator = homePage.getProductTitleLocator(productName);

        WebElement productElement = null;
        boolean productFound = false;
        int currentPage = 1;

        do {
            LoggerUtil.info("Attempting to find '" + productName + "' on Page " + currentPage + "...");

            try {
                productElement = getWait().withTimeout(Duration.ofSeconds(5))
                        .until(ExpectedConditions.visibilityOfElementLocated(productLocator));
                productFound = true;
                LoggerUtil.info("Product '" + productName + "' FOUND on Page " + currentPage + ".");
                break;

            } catch (TimeoutException e) {
                LoggerUtil.info("Product '" + productName + "' NOT found on Page " + currentPage + ".");

                if (homePage.isNextButtonAvailable()) {
                    homePage.clickNextPage();
                    currentPage++;
                } else {
                    break;
                }
            }
        } while (!productFound);

        if (productFound) {
            Assert.assertNotNull(productElement, "Internal error: Product found, but element reference is null.");
            LoggerUtil.info("Testcase passed. Product '" + productName + "' is visible.");
        } else {
            Assert.fail("Testcase failed. Product '" + productName
                    + "' was not found after searching " + currentPage + " page(s) of the catalog.");
        }
    }

    @DataProvider(name = "searchByCategory")
    public Object[][] searchByCategory() {
        String category = System.getProperty("test.category", "Phones");
        return new ExcelReaderSearch().getFilteredSearchData("category", category);
    }

    @Test(dataProvider = "searchByCategory", description = "Verifying search functionality by category")
    public void testSearchByCategory(String productName, String category) {
        LoggerUtil.info("...SEARCH BY CATEGORY " + category + " FOR PRODUCT " + productName + "...");

        LoggerUtil.info("Selecting category: " + category);
        Assert.assertTrue(homePage.isValidCategory(category), "Invalid category: " + category);

        homePage.selectCategory(category);

        By productLocator = homePage.getProductTitleLocator(productName);
        WebElement productElement = getWait().until(ExpectedConditions.visibilityOfElementLocated(productLocator));
        Assert.assertNotNull(productElement, "TestCase failed. Expected product '" + productName
                + "' to be visible under category '" + category + "', but it was not found.");

        LoggerUtil.info("TestCase passed. Product '" + productName + "' is visible under category '" + category + "'");
    }

    @DataProvider(name = "searchWithEmptyResults")
    public Object[][] searchWithNoResults() {
        return new ExcelReaderSearch().getFilteredSearchData("Category", "Empty");
    }

    @Test(dataProvider = "searchWithEmptyResults", description = "Verifying search functionality with empty data")
    public void testSearchWithNoResults(String productName, String category) {
        LoggerUtil.info("...SEARCH WITH EMPTY RESULTS...");

        LoggerUtil.info("Selecting category: " + category);
        category.equalsIgnoreCase("Empty");
        boolean productIsVisible = homePage.isProductVisible(productName);
        Assert.assertFalse(productIsVisible,
                "TestCase failed. Found an unexpected product ('NonExistentProduct') after filtering.");

        LoggerUtil.info("TestCase passed. Product not found as expected.");
    }
}