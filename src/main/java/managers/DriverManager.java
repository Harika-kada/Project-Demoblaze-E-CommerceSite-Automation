package managers;

import io.github.bonigarcia.wdm.WebDriverManager;
import utils.LoggerUtil;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class DriverManager {
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static ThreadLocal<String> browserName = new ThreadLocal<>();

    private DriverManager() {
    }

    public static void setBrowser(String browser) {
        browserName.set(browser);
    }

    public static WebDriver getDriver() {
        if (driver.get() == null) {
            String browser = browserName.get();
            if (browser == null) {
                throw new RuntimeException("Browser name not set. Call setBrowser() before getDriver().");
            }

            switch (browser.toLowerCase()) {
                case "chrome":
                    LoggerUtil.info("Running Chrome browser");
                    WebDriverManager.chromedriver().setup();
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.addArguments("--disable-notifications", "--start-maximized");
                    driver.set(new ChromeDriver(chromeOptions));
                    break;
                case "firefox":
                    LoggerUtil.info("Running firefox browser");
                    WebDriverManager.firefoxdriver().setup();
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    firefoxOptions.setCapability("unhandledPromptBehavior", "ignore");
                    firefoxOptions.addArguments("--disable-notifications", "--start-maximized");
                    driver.set(new FirefoxDriver(firefoxOptions));
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported Browser: " + browser);
            }
        }
        return driver.get();
    }

    public static void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
            browserName.remove();
        }
    }
}