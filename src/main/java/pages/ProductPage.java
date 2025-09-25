package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import utils.LoggerUtil;

public class ProductPage extends BasePage{
    @FindBy(xpath = "//a[text()='Add to cart']")
    private WebElement addToCartButton;

    @FindBy(css = ".name")
    private WebElement productTitle;

    @FindBy(css = ".price-container")
    private WebElement productPrice;

    public ProductPage(WebDriver driver) {
        super(driver);
    }

    public void addToCart() {
        click(addToCartButton, "Add to Cart Button");
        acceptAlert();
    }

    public String getProductTitle() {
        return getText(productTitle, "Product Title");
    }

    public String getProductPrice() {
        return getText(productPrice, "Product Price");
    }

    public boolean isProductPageLoaded(String productName) {
        try {
            WebElement productTitleElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[@class='name']")));

            String actualTitle = productTitleElement.getText();
            LoggerUtil.info("Actual product title on page: " + actualTitle);
            LoggerUtil.info("Expected product name from test case: " + productName);

            return actualTitle.equals(productName);
        }
        catch (Exception e) {
            LoggerUtil.error("Product title element was not found on the page: " + e.getMessage());
            return false;
        }
    }
}
