package data;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelReaderSearch {
    private static final String FILE_PATH = "src\\main\\resources\\testdata.xlsx";

    public Object[][] getFilteredSearchData(String filterType, String filterValue) {
        try (FileInputStream fis = new FileInputStream(FILE_PATH);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet("Search");
            if (sheet == null) throw new IllegalArgumentException("Sheet 'Search' not found in Excel");

            int rowCount = sheet.getPhysicalNumberOfRows();

            List<Object[]> filteredData = new ArrayList<>();

            for (int i = 1; i < rowCount; i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Cell productCell = row.getCell(0);
                Cell categoryCell = row.getCell(1);

                String product = productCell != null ? productCell.getStringCellValue().trim() : "";
                String category = categoryCell != null ? categoryCell.getStringCellValue().trim() : "";

                boolean match = false;
                if (filterType.equalsIgnoreCase("product") && product.toLowerCase().contains(filterValue.toLowerCase())) {
                    match = true;
                } else if (filterType.equalsIgnoreCase("category") && category.equalsIgnoreCase(filterValue)) {
                    match = true;
                }

                if (match) {
                    filteredData.add(new Object[]{product, category});
                }
            }
            return filteredData.toArray(new Object[0][]);
        } catch (IOException e) {
            e.printStackTrace();
            return new Object[0][0];
        }
    }
}