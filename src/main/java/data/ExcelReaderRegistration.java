package data;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelReaderRegistration {
    private static final String FILE_PATH = "src\\main\\resources\\testdata.xlsx";

    public Object[][] provideExcelData(String filterType) {
        try (FileInputStream fis = new FileInputStream(FILE_PATH);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet("Registration");
            int rowCount = sheet.getPhysicalNumberOfRows();
            int colCount = sheet.getRow(0).getLastCellNum();

            List<Object[]> filteredData = new ArrayList<>();

            for (int i = 1; i < rowCount; i++) {
                Row row = sheet.getRow(i);
                if (row == null || row.getCell(2) == null) continue;

                String type = row.getCell(2).getStringCellValue().trim();
                if (type.equalsIgnoreCase(filterType)) {
                    Object[] rowData = new Object[colCount - 1];
                    rowData[0] = getCellValue(row.getCell(0));
                    rowData[1] = getCellValue(row.getCell(1));
                    rowData[2] = getCellValue(row.getCell(3));
                    filteredData.add(rowData);
                }
            }

            return filteredData.toArray(new Object[0][]);

        } catch (IOException e) {
            e.printStackTrace();
            return new Object[0][0];
        }
    }

    private Object getCellValue(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING: return cell.getStringCellValue().trim();
            case BOOLEAN: return cell.getBooleanCellValue();
            case NUMERIC:
                return DateUtil.isCellDateFormatted(cell) ? cell.getDateCellValue() : cell.getNumericCellValue();
            case BLANK: return "";
            default: return cell.toString().trim();
        }
    }

//    public Object[][] provideExcelData() throws IOException {
//        String excelFilePath = "src\\main\\resources\\testdata.xlsx";
//        try (FileInputStream fis = new FileInputStream(excelFilePath);
//             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {
//
//            XSSFSheet sheet = workbook.getSheet("Registration");
//            int rowCount = sheet.getPhysicalNumberOfRows();
//            int colCount = sheet.getRow(0).getLastCellNum();
//            Object[][] data = new Object[rowCount - 1][colCount];
//
//            for (int i = 1; i < rowCount; i++) {
//                XSSFRow row = sheet.getRow(i);
//                for (int j = 0; j < colCount; j++) {
//                    Cell cell = row.getCell(j);
//                    data[i - 1][j] = (cell == null) ? "" : cell.toString().trim();
//                }
//            }
//            return data;
//        }
//    }
}
