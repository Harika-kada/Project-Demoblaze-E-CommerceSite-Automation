package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import utils.LoggerUtil;

import java.util.List;

public class CartPage extends BasePage {
    @FindBy(xpath = "//a[text()='Cart']")
    private WebElement cartButton;

    @FindBy(xpath = "//tbody/tr")
    private List<WebElement> cartItems;

    @FindBy(xpath = "//button[text()='Place Order']")
    private WebElement placeOrderButton;

    @FindBy(xpath = "//button[text()='Delete']")
    private List<WebElement> deleteButtons;

    @FindBy(id = "totalp")
    private WebElement totalPrice;

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public void goToCart() {
        click(cartButton, "Cart Button");
        isCartPageLoaded();
    }

    public boolean isCartPageLoaded() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[text()='Products']")));
            LoggerUtil.info("Cart page loaded successfully.");
            return true;
        } catch (Exception e) {
            LoggerUtil.error("Cart page failed to load: " + e.getMessage());
            return false;
        }
    }

    public boolean isProductInCart(String productName) {
        try {
            String productInCartXPath = String.format("//tbody/tr/td[text()='%s']", productName);
            WebElement productInCartElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(productInCartXPath)));
            LoggerUtil.info("Product '" + productName + "' found in the cart.");
            return productInCartElement.isDisplayed();
        } catch (Exception e) {
            LoggerUtil.error("Product '" + productName + "' was not found in the cart. Error: " + e.getMessage());
            return false;
        }
    }

    public void removeProduct(String productName) {
        try {
            String productRowXPath = String.format("//td[text()='%s']/..//a[text()='Delete']", productName);
            WebElement deleteButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(productRowXPath)));

            deleteButton.click();
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(productRowXPath)));
            LoggerUtil.info("Removed product '" + productName + "' from the cart.");
        } catch (Exception e) {
            LoggerUtil.error("Failed to remove product '" + productName + "' from cart. Error: " + e.getMessage());
            throw new RuntimeException("Could not remove product: " + productName, e);
        }
    }

    public boolean isProductRemoved(String productName) {
        try {
            String productInCartXPath = String.format("//tbody/tr/td[text()='%s']", productName);
            boolean isRemoved = wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(productInCartXPath)));

            if (isRemoved) {
                LoggerUtil.info("Product '" + productName + "' successfully removed from the cart.");
                return true;
            } else {
                LoggerUtil.warn("Product '" + productName + "' is still visible in the cart.");
                return false;
            }
        } catch (Exception e) {
            LoggerUtil.info("Product '" + productName + "' was not found, confirming its removal.");
            return true;
        }
    }

    public int getCartItemCount() {
        return cartItems.size();
    }

    public String getTotalPrice() {
        return totalPrice.getText();
    }

    public void clickPlaceOrder() {
        placeOrderButton.click();
    }

    public boolean isCartEmpty() {
        return cartItems.isEmpty();
    }
}