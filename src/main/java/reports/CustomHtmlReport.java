package reports;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CustomHtmlReport {
    public static void generateReport(List<TestResultModel> results) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("output/test-output/custom-report/CustomReport.html"))) {
            writer.write("<html><head><title>Test Summary</title></head><body>");
            writer.write("<h1>Demoblaze Test Summary</h1>");
            writer.write("<table border='1'><tr><th>Test Name</th><th>Status</th><th>Screenshot</th></tr>");
            for (TestResultModel result : results) {
                writer.write("<tr>");
                writer.write("<td>" + result.getTestName() + "</td>");
                writer.write("<td>" + result.getStatus() + "</td>");
                writer.write("<td><a href='../" + result.getScreenshotPath() + "'>View</a></td>");
                writer.write("</tr>");
            }
            writer.write("</table></body></html>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}