package managers;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigManager {

    private static final Properties properties = new Properties();
    private static final String CONFIG_FILE = "src/main/resources/config.properties";

    static {
        try (FileInputStream input = new FileInputStream(CONFIG_FILE)) {
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read config properties file: " + CONFIG_FILE, e);
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static String getUrl(String url) {
        return getProperty("base_url");
    }

    public static String getBrowser(String browser) {
        return getProperty("browser");
    }

    public static String getImplicitWait(String implicitwait) {
        return getProperty("implicit.wait");
    }

    public static String getExplicitWait(String explicitwait) {
        return getProperty("explicit.wait");
    }

//    public static void main(String[] args) {
//        System.out.println("URL: " + getUrl());
//        System.out.println("Browser: " + getBrowser());
//    }
}