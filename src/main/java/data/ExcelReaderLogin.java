package data;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelReaderLogin
{
    private static final String FILE_PATH = "src\\main\\resources\\testdata.xlsx";

//    public Object[][] getTestData(String sheetName)
//    {
//        try (FileInputStream fis = new FileInputStream("src\\main\\resources\\testdata.xlsx");
//             Workbook workbook = new XSSFWorkbook(fis))
//        {
//            Sheet sheet = workbook.getSheet(sheetName);
//            int rows = sheet.getPhysicalNumberOfRows();
//            int cols = sheet.getRow(0).getPhysicalNumberOfCells();
//            Object[][] data = new Object[rows - 1][cols];
//
//            for (int i = 1; i < rows; i++)
//            {
//                for (int j = 0; j < cols; j++)
//                {
//                    data[i - 1][j] = sheet.getRow(i).getCell(j).toString();
//                }
//            }
//            return data;
//        }
//        catch (IOException e)
//        {
//            e.printStackTrace();
//            return null;
//        }
//    }
    public Object[][] getFilteredTestData(String sheetName, String filterType) {
        try (FileInputStream fis = new FileInputStream(FILE_PATH);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            int rowCount = sheet.getPhysicalNumberOfRows();
            int colCount = sheet.getRow(0).getLastCellNum();

            List<Object[]> filteredData = new ArrayList<>();

            for (int i = 1; i < rowCount; i++) {
                Row row = sheet.getRow(i);
                Cell typeCell = row.getCell(2);
                String type = typeCell.getStringCellValue().trim();

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
        switch (cell.getCellType()) {
            case STRING: return cell.getStringCellValue().trim();
            case BOOLEAN: return cell.getBooleanCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue();
                } else {
                    return cell.getNumericCellValue();
                }
            case BLANK: return "";
            default: return cell.toString().trim();
        }
    }
//    private static final String FILE_PATH = "src\\main\\resources\\testdata.xlsx";
//
//    public Object[][] getLoginDataByScenario(String scenario) {
//        List<Object[]> dataList = new ArrayList<>();
//
//        try (FileInputStream fis = new FileInputStream(FILE_PATH);
//             Workbook workbook = new XSSFWorkbook(fis)) {
//
//            Sheet sheet = workbook.getSheet("Login");
//            int totalRows = sheet.getLastRowNum();
//
//            for (int i = 1; i <= totalRows; i++) {
//                Row row = sheet.getRow(i);
//                if (row == null) continue;
//
//                switch (scenario.toLowerCase()) {
//                    case "valid":
//                        if (isCellFilled(row, 0) && isCellFilled(row, 1)) {
//                            dataList.add(new Object[]{
//                                    getCellValue(row.getCell(0)),
//                                    getCellValue(row.getCell(1)),
//                                    true
//                            });
//                        }
//                        break;
//
//                    case "invalid":
//                        if (isCellFilled(row, 3) && isCellFilled(row, 4)) {
//                            dataList.add(new Object[]{
//                                    getCellValue(row.getCell(3)),
//                                    getCellValue(row.getCell(4)),
//                                    false
//                            });
//                        }
//                        break;
//
//                    case "empty":
//                        if (row.getCell(6) != null || row.getCell(7) != null) {
//                            dataList.add(new Object[]{
//                                    getCellValue(row.getCell(6)),
//                                    getCellValue(row.getCell(7)),
//                                    false
//                            });
//                        }
//                        break;
//
//                    case "remember":
//                        if (isCellFilled(row, 9) && isCellFilled(row, 10)) {
//                            dataList.add(new Object[]{
//                                    getCellValue(row.getCell(9)),
//                                    getCellValue(row.getCell(10)),
//                                    true
//                            });
//                        }
//                        break;
//                }
//            }
//
//        } catch (Exception e) {
//            System.err.println("Error reading Excel file: " + e.getMessage());
//            e.printStackTrace();
//        }
//
//        return dataList.toArray(new Object[0][]);
//    }
//
//    private boolean isCellFilled(Row row, int cellIndex) {
//        Cell cell = row.getCell(cellIndex);
//        return cell != null && !getCellValue(cell).toString().trim().isEmpty();
//    }

//    private Object getCellValue(Cell cell) {
//        if (cell == null) return "";
//        switch (cell.getCellType()) {
//            case STRING:
//                return cell.getStringCellValue().trim();
//            case BOOLEAN:
//                return cell.getBooleanCellValue();
//            case NUMERIC:
//                if (DateUtil.isCellDateFormatted(cell))
//                    return cell.getDateCellValue();
//                else
//                    return String.valueOf((long) cell.getNumericCellValue());
//            case FORMULA:
//                return cell.getCellFormula();
//            default:
//                return "";
//        }
//    }
}
