package pages;

import managers.ConfigManager;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import utils.LoggerUtil;

import java.util.List;
import java.util.Map;

public class HomePage extends BasePage {
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

    @FindBy(xpath = "//a[@class='hrefch']")
    private List<WebElement> productLinks;

    @FindBy(xpath = "//a[text()='Cart']")
    private WebElement cartButton;

    @FindBy(xpath = "//button[@id='next2']")
    private WebElement nextButton;

    private Map<String, WebElement> categoryMap;

    public HomePage(WebDriver driver) {
        super(driver);

        this.categoryMap = Map.of(
                "phones", phonesCategory,
                "laptops", laptopsCategory,
                "monitors", monitorsCategory
        );
    }
    public List<WebElement> getProductTitles() {
        return productTitles;
    }
    public void navigateToWebsite() {
        String url = ConfigManager.getUrl("base_url");
        driver.get(url);
        LoggerUtil.info("Navigating to website url: " + url);
    }
    public boolean isHomePageLoaded() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@class='nav-link'][contains(text(), 'Home')]")));
            LoggerUtil.info("Home page loaded successfully.");
            return true;
        } catch (Exception e) {
            LoggerUtil.error("Home page failed to load: " + e.getMessage());
            return false;
        }
    }
    public void selectProduct(String productName) {
        try {
            String productLinkXPath = String.format("//a[@class='hrefch'][text()='%s']", productName);
            WebElement productLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(productLinkXPath)));
            click(productLink, "Product: " + productName);
            LoggerUtil.info("Successfully selected the product: " + productName);
        } catch (Exception e) {
            LoggerUtil.error("Failed to select product '" + productName + "'. Error: " + e.getMessage());
            throw new RuntimeException("Could not find and click product: " + productName, e);
        }
    }
    public By getProductTitleLocator(String productName) {
        return By.xpath(String.format("//h4[@class='card-title']/a[text()='%s']", productName));
    }
    public void selectCategory(String category) {
        WebElement element = categoryMap.get(category.toLowerCase());
        if (element == null) {
            throw new IllegalArgumentException("Invalid category: " + category);
        }
        click(element, category + " Category");
    }
    public boolean isValidCategory(String category) {
        return categoryMap.containsKey(category.toLowerCase());
    }
    public boolean isProductVisible(String productName) {
        return productTitles.stream().anyMatch(title -> title.getText().equalsIgnoreCase(productName));
    }
    public WebElement getProductElement(String productName) {
        return productTitles.stream()
                .filter(p -> p.getText().equalsIgnoreCase(productName))
                .findFirst()
                .orElse(null);
    }
    public boolean isNextButtonAvailable() {
        try
        {
            wait.until(ExpectedConditions.visibilityOf(nextButton));
            return nextButton.isDisplayed() && nextButton.isEnabled();
        }
        catch (Exception e)
        {
            return false;
        }
    }
    public void clickNextPage()
    {
        if (isNextButtonAvailable())
        {
            click(nextButton, "Navigator to next page button");
            wait.until(ExpectedConditions.invisibilityOf(nextButton));
            LoggerUtil.info("Navigated to the next page of products.");
        }
        else {
            LoggerUtil.info("Next button is not available. End of catalog reached.");
            throw new NoSuchElementException("Cannot click 'Next'. End of product catalog reached.");
        }
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