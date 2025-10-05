package utils;

import managers.DriverManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.io.FileHandler;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.awt.Robot;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class ScreenshotUtil {
    public static String capturePassScreenshot(String testName) {
        return captureScreenshot(testName, "pass");
    }

    public static String captureFailScreenshot(String testName) {
        return captureScreenshot(testName, "fail");
    }

    private static String captureScreenshot(String testName, String status) {
        try {
            File src = ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.FILE);
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

            String folderPath = System.getProperty("user.dir") + "/output/screenshots/" + status;
            new File(folderPath).mkdirs();
            String filePath = folderPath + "/" + testName + "_" + timestamp + ".png";

            FileHandler.copy(src, new File(filePath));
            LoggerUtil.info("Screenshot saved: " + filePath);
            return filePath;
        } catch (Exception e) {
            LoggerUtil.error("Failed to capture screenshot: " + e.getMessage());
            return null;
        }
    }
//
//    public static void captureFailScreenshot(String testName) {
//        try {
//            Robot robot = new Robot();
//            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
//            BufferedImage screenCapture = robot.createScreenCapture(screenRect);
//            ImageIO.write(screenCapture, "png", new File("alert_screenshot.png"));
//
//            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//
//            // Ensure folder exists
//            String folderPath = "C:\\Users\\kadahari\\Desktop\\Harika\\C\\Demoblaze\\output\\screenshots\\fail";
//
//            // Capture browser screenshot
//            File browserSrc = ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.FILE);
//            String browserPath = folderPath + "\\" + testName + "_browser_" + timestamp + ".png";
//            FileHandler.copy(browserSrc, new File(browserPath));
//            LoggerUtil.info("Browser screenshot saved: " + browserPath);
//
//            // Capture full desktop screenshot
//            Robot robot = new Robot();
//            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
//            BufferedImage screenCapture = robot.createScreenCapture(screenRect);
//            String desktopPath = folderPath + "\\" + testName + "_desktop_" + timestamp + ".png";
//            ImageIO.write(screenCapture, "png", new File(desktopPath));
//            LoggerUtil.info("Desktop screenshot saved: " + desktopPath);
//        } catch (Exception e) {
//            LoggerUtil.error("Failed to capture screenshot: " + e.getMessage());
//        }
//    }
}