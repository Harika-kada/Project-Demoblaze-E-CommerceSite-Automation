package tests;

import data.ExcelReaderSearch;
import managers.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.RegistrationPage;
import utils.LoggerUtil;
import utils.ScreenshotUtil;

import java.time.Duration;

public class SearchTests {

    private WebDriver driver;
    private HomePage homePage;

    @BeforeMethod
    public void initialSetup() {
        LoggerUtil.info("Setting up test....");
        driver = DriverManager.getDriver();
        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();
        homePage = new HomePage();
    }
    @AfterMethod
    public void tearDown() {
        DriverManager.quitDriver();
        LoggerUtil.info("Finishing tests.");
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

        LoggerUtil.info("Navigated to homepage. Checking visibility of product: " + productName);

        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//a[text()='" + productName + "']")));

        if (!homePage.isProductVisible(productName)) {
            Assert.fail("TestCase failed. Product '" + productName + "' is not visible.");
            LoggerUtil.error("Product '" + productName + "' is not visible.");
            ScreenshotUtil.captureFailScreenshot(testName);
        }
        LoggerUtil.info("TestCase passed. Product '" + productName + "' is visible.");
//      ScreenshotUtil.capturePassScreenshot(testName);

    }

    @DataProvider(name = "searchByCategory")
    public Object[][] searchByCategory() {
        return new ExcelReaderSearch().getFilteredSearchData("category", "Monitors");
    }
    @Test(dataProvider = "searchByCategory", description = "Searching by category")
    public void testSearchByCategory(String productName, String category)
    {
        LoggerUtil.info("...SEARCH BY CATEGORY...");
        String testName = "testSearch_" + category + "_" + productName;

        homePage.navigateToWebsite();
        homePage.selectCategory(category);

        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(3));
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//a[text()='" + productName + "']")));
        if (homePage.isProductVisible(productName)) {
            LoggerUtil.info("TestCase passed. Product '" + productName + "' is visible under category '" + category + "'");
//            ScreenshotUtil.capturePassScreenshot(testName);
        } else {
            LoggerUtil.error("Product '" + productName + "' not found under category '" + category + "'");
//            ScreenshotUtil.captureFailScreenshot(testName);
            Assert.fail("TestCase failed. Product '" + productName + "' not found under category '" + category + "'");
        }
    }
}
