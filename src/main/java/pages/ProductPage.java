package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import utils.LoggerUtil;

public class ProductPage extends BasePage {
    @FindBy(xpath = "//a[text()='Add to cart']")
    private WebElement addToCartButton;

    @FindBy(css = ".name")
    private WebElement productTitle;

    @FindBy(xpath = "//h3[contains(text(), '$')]")
    private WebElement productPrice;

    public ProductPage(WebDriver driver) {
        super(driver);
    }

    public void addToCart() {
        click(addToCartButton, "Add to Cart Button");
        if (isAlertPresent()) {
            handleAlertAndGetText();
            LoggerUtil.info("Successfully dismissed 'Product added' alert.");
        } else {
            LoggerUtil.warn("Expected 'Product added' alert did not appear.");
        }
    }

    public String getProductTitle() {
        return getText(productTitle, "Product Title");
    }

    public double getProductPrice() {
//        return getText(productPrice, "Product Price");
        String rawPriceString = getText(productPrice, "Product Price");
        LoggerUtil.info("Successfully read the value as: " + rawPriceString + " from element Product Price");

        String cleanedPriceString = rawPriceString.replaceAll("[^\\d.]", "");

        if (cleanedPriceString.isEmpty() || cleanedPriceString.equals(".")) {
            LoggerUtil.error("Failed to parse price: Cleaned string was empty or invalid.");
            return 0.0;
        }
        return Double.parseDouble(cleanedPriceString);
    }

    public boolean isProductPageLoaded(String productName) {
        try {
            WebElement productTitleElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[@class='name']")));

            String actualTitle = productTitleElement.getText();
            LoggerUtil.info("Actual product title on page: " + actualTitle);
            LoggerUtil.info("Expected product name from test case: " + productName);

            return actualTitle.equals(productName);
        } catch (Exception e) {
            LoggerUtil.error("Product title element was not found on the page: " + e.getMessage());
            return false;
        }
    }
}