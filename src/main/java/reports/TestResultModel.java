package reports;

public class TestResultModel {
    private String testName;
    private String status;
    private String screenshotPath;

    public TestResultModel(String testName, String status, String screenshotPath) {
        this.testName = testName;
        this.status = status;
        this.screenshotPath = screenshotPath;
    }

    public String getTestName() {
        return testName;
    }

    public String getStatus() {
        return status;
    }

    public String getScreenshotPath() {
        return screenshotPath;
    }
}