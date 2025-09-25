package listeners;

import com.aventstack.extentreports.ExtentTest;
import org.testng.ITestContext;
import reports.ExtentManager;
import utils.LoggerUtil;
import utils.ScreenshotUtil;
import org.testng.ITestListener;
import org.testng.ITestResult;

import reports.TestResultModel;
import reports.CustomHtmlReport;
import java.util.ArrayList;
import java.util.List;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class TestListener implements ITestListener {
//    @Override
//    public void onTestFailure(ITestResult result) {
//        TakesScreenshot ts = (TakesScreenshot) DriverManager.getDriver();
//        File src = ts.getScreenshotAs(OutputType.FILE);
//        try {
//            Files.copy(src.toPath(), Paths.get("output/screenshots/" + result.getName() + ".png"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    private static final List<TestResultModel> results = new ArrayList<>();

    @Override
    public void onStart(ITestContext context) {
        ExtentManager.createInstance();
        String browser = context.getCurrentXmlTest().getParameter("browser");
        ExtentManager.getExtent().setSystemInfo("Browser", browser);
    }
    @Override
    public void onTestStart(ITestResult result) {
        String className = result.getTestClass().getName();
        String methodName = result.getMethod().getMethodName();
        ExtentTest extentTest = ExtentManager.getExtent().createTest(className + " :: " + methodName);
        ExtentManager.getTest().set(extentTest);

        LoggerUtil.info("Starting test: " + methodName);
        extentTest.info("Test parameters: " + Arrays.toString(result.getParameters()));
    }
    @Override
    public void onTestSuccess(ITestResult result) {
        String screenshotPath = ScreenshotUtil.capturePassScreenshot(result.getName());

        ExtentManager.getTest().get().pass("Test passed");
        ExtentManager.getTest().get().addScreenCaptureFromPath(screenshotPath);

        String relativePathForCustomReport = screenshotPath.substring(
                screenshotPath.indexOf("output/"));
        results.add(new TestResultModel(result.getName(), "PASS", relativePathForCustomReport));
//        attachScreenshotToAllure(screenshotPath);
    }
    @Override
    public void onTestFailure(ITestResult result) {
        String screenshotPath = ScreenshotUtil.captureFailScreenshot(result.getName());

        ExtentManager.getTest().get().fail(result.getThrowable());
        ExtentManager.getTest().get().addScreenCaptureFromPath(screenshotPath);

        String relativePathForCustomReport = screenshotPath.substring(
                screenshotPath.indexOf("output/"));

        results.add(new TestResultModel(result.getName(), "FAIL", relativePathForCustomReport));
//        attachScreenshotToAllure(screenshotPath);
    }
    @Override
    public void onTestSkipped(ITestResult result) {
        LoggerUtil.warn("Test skipped: " + result.getName());
        ExtentManager.getTest().get().skip("Test skipped");
    }
    @Override
    public void onFinish(ITestContext context) {
        LoggerUtil.info("Test suite finished: " + context.getName());
        ExtentManager.getExtent().flush();
        CustomHtmlReport.generateReport(results);
    }
//    private void attachScreenshotToAllure(String path) {
//        try {
//            byte[] screenshotBytes = Files.readAllBytes(Paths.get(path));
//            Allure.getLifecycle().addAttachment("Screenshot", "image/png", "png", screenshotBytes);
//        } catch (IOException e) {
//            LoggerUtil.error("Failed to attach screenshot to Allure: " + e.getMessage());
//        }
//    }
}
