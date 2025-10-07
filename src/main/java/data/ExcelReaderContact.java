package data;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import utils.LoggerUtil;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class ExcelReaderContact {
    private static final String FILE_PATH = "src\\main\\resources\\testdata.xlsx";

    public Object[][] getFilteredTestData(String filterType) {
        try (FileInputStream fis = new FileInputStream(FILE_PATH);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet("Contact");
            int rowCount = sheet.getPhysicalNumberOfRows();
            int colCount = sheet.getRow(0).getLastCellNum();

            List<Object[]> filteredData = new ArrayList<>();

            for (int i = 1; i < rowCount; i++) {
                Row row = sheet.getRow(i);
                Cell typeCell = row.getCell(4);
                String type = typeCell.getStringCellValue().trim();

                if (type.equalsIgnoreCase(filterType)) {
                    Object[] rowData = new Object[4];
                    rowData[0] = getCellValue(row.getCell(0));
                    rowData[1] = getCellValue(row.getCell(1));
                    rowData[2] = getCellValue(row.getCell(2));
                    rowData[3] = getCellValue(row.getCell(3));

                    filteredData.add(rowData);
                }
            }
            LoggerUtil.info("Loaded " + filteredData.size() + " rows for type: " + filterType);            return filteredData.toArray(new Object[0][]);
        } catch (IOException e) {
            LoggerUtil.error("Failed to read contact test data: " + e.getMessage());
            e.printStackTrace();
            return new Object[0][0];
        }
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
