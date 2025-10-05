package data;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import utils.LoggerUtil;

public class ExcelReaderCheckout {
    private static final String TESTDATA_PATH = "src/main/resources/testdata.xlsx";

    public List<Map<String, String>> getMappedData(String sheetName) {
        List<Map<String, String>> dataList = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(TESTDATA_PATH);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            Row headerRow = sheet.getRow(0);
            int colCount = headerRow.getLastCellNum();

            for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                Row row = sheet.getRow(i);
                Map<String, String> rowData = new LinkedHashMap<>();

                for (int j = 0; j < colCount; j++) {
                    String key = headerRow.getCell(j).getStringCellValue();
                    Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    rowData.put(key, (String) getCellValue(cell));
                }

                dataList.add(rowData);
            }

        } catch (IOException e) {
            LoggerUtil.error("Failed to read Excel file: " + TESTDATA_PATH);
            throw new RuntimeException("Excel file read error", e);
        }

        return dataList;
    }

    private Object getCellValue(Cell cell) {
        DataFormatter formatter = new DataFormatter();

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue()).trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return formatter.formatCellValue(cell).trim();
                } else {
                    return formatter.formatCellValue(cell).trim();
                }
            case BLANK:
                return "";
            default:
                return cell.toString().trim();
        }
    }
}
