package data;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import utils.LoggerUtil;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelReaderCart {
    private static final String TESTDATA_PATH = "src/main/resources/testdata.xlsx";

    public Object[][] getTestData(String sheetName) {
        try (FileInputStream fis = new FileInputStream(TESTDATA_PATH);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new RuntimeException("Sheet not found: " + sheetName);
            }

            int rowCount = sheet.getPhysicalNumberOfRows();
            int colCount = sheet.getRow(0).getLastCellNum();

            Object[][] data = new Object[rowCount - 1][colCount];

            for (int i = 1; i < rowCount; i++) {
                Row row = sheet.getRow(i);
                for (int j = 0; j < colCount; j++) {
                    Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    data[i - 1][j] = getCellValue(cell);
                }
            }

            return data;

        } catch (IOException e) {
            LoggerUtil.error("Failed to read Excel file: " + TESTDATA_PATH);
            throw new RuntimeException("Error reading Excel file", e);
        }
    }

    public Object[][] getFilteredTestData(String sheetName, String actionType) {
        Object[][] allData = getTestData(sheetName);
        List<Object[]> filteredRows = new ArrayList<>();

        int actionColumnIndex = -1;
        try (FileInputStream fis = new FileInputStream(TESTDATA_PATH);
             Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheet(sheetName);
            Row headerRow = sheet.getRow(0);
            for (int j = 0; j < headerRow.getLastCellNum(); j++) {
                Cell cell = headerRow.getCell(j);
                if (cell != null && "Action".equalsIgnoreCase(cell.getStringCellValue())) {
                    actionColumnIndex = j;
                    break;
                }
            }
        } catch (IOException e) {
            LoggerUtil.error("Failed to read Excel file: " + TESTDATA_PATH);
            throw new RuntimeException("Error reading Excel file", e);
        }

        if (actionColumnIndex == -1) {
            LoggerUtil.error("Action column not found in Excel sheet.");
            throw new RuntimeException("Action column not found.");
        }

        for (Object[] row : allData) {
            if (row.length > actionColumnIndex && actionType.equalsIgnoreCase((String) row[actionColumnIndex])) {
                filteredRows.add(row);
            }
        }
        return filteredRows.toArray(new Object[0][0]);
    }

    private Object getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue();
                } else {
                    return cell.getNumericCellValue();
                }
            case BLANK:
                return "";
            default:
                return cell.toString().trim();
        }
    }
}