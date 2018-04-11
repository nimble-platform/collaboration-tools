package it.domina.nimble.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

public class ExcelUtils {

    private static int REQUIRED_EXCEL_COLUMNS = 10;

	public static boolean isAllRowEmpty(Row row) {
        boolean isAllEmpty = true;
        for (int i = 0; i < REQUIRED_EXCEL_COLUMNS; i++) {
            Cell c = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            if (CellType.BLANK != c.getCellTypeEnum()) {
                isAllEmpty = false;
            }
        }
        return isAllEmpty;
    }

	public static boolean excelHasAllValues(Row row) {
        return row.getCell(0).getStringCellValue().equals("UserFirstName") &&
                row.getCell(1).getStringCellValue().equals("UserLastName") &&
                row.getCell(2).getStringCellValue().equals("UserEmail") &&
                row.getCell(3).getStringCellValue().equals("UserPassword") &&
                row.getCell(4).getStringCellValue().equals("CompanyName") &&
                row.getCell(5).getStringCellValue().equals("Country") &&
                row.getCell(6).getStringCellValue().equals("City") &&
                row.getCell(7).getStringCellValue().equals("Street") &&
                row.getCell(8).getStringCellValue().equals("Building") &&
                row.getCell(9).getStringCellValue().equals("PostalCode");
    }

}
