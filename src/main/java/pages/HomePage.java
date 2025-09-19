package pages;

import managers.ConfigManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import utils.LoggerUtil;

import java.util.List;

public class HomePage extends BasePage
{
    @FindBy(id = "logout2")
    private WebElement logoutButton;

    @FindBy(linkText = "Phones")
    private WebElement phonesCategory;

    @FindBy(linkText = "Laptops")
    private WebElement laptopsCategory;

    @FindBy(linkText = "Monitors")
    private WebElement monitorsCategory;

    @FindBy(css = ".card-title")
    private List<WebElement> productTitles;

    public HomePage() {
        super();
        PageFactory.initElements(driver, this);
    }
    public void navigateToWebsite() {
        driver.get(ConfigManager.getUrl("base_url"));
        LoggerUtil.info("Navigating to website url " + ConfigManager.getUrl("base_url"));
    }
    public void selectCategory(String category) {
        switch (category.toLowerCase()) {
            case "phones":
                phonesCategory.click();
                break;
            case "laptops":
                laptopsCategory.click();
                break;
            case "monitors":
                monitorsCategory.click();
                break;
            default:
                throw new IllegalArgumentException("Invalid category: " + category);
        }
    }
    public boolean isProductVisible(String productName) {
        return productTitles.stream()
                .anyMatch(title -> title.getText().equalsIgnoreCase(productName));
    }
    public void logout() {
        try {
            click(logoutButton, "Logout Button");
            wait.until(ExpectedConditions.invisibilityOf(logoutButton));
            LoggerUtil.info("Successfully logged out");
        } catch (Exception e) {
            LoggerUtil.error("Failed to logout: " + e.getMessage());
            throw e;
        }
    }
}
