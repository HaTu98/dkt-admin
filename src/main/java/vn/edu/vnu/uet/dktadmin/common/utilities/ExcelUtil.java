package vn.edu.vnu.uet.dktadmin.common.utilities;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;

public class ExcelUtil {

    public static String getValueInCell(Cell cell) {
        if (cell == null) return null;
        CellType type = cell.getCellType();
        if (type == CellType.STRING) {
            return cell.getStringCellValue();
        } else if (type == CellType.NUMERIC) {
            double cellData = cell.getNumericCellValue();
            return Double.toString(cellData);
        } else {
            return null;
        }
    }

    public static void copyRow(Row rowNew, Row rowOld, CellStyle cellStyle) {
        for (int i = 0; i < rowOld.getLastCellNum(); i++) {
            Cell cellOld = rowOld.getCell(i);
            Cell cellNew = rowNew.createCell(i);
            cellNew.setCellStyle(cellStyle);
            if (cellOld == null) {
                cellNew = null;
                continue;
            }
            copyCell(cellOld, cellNew);
        }
    }


    public static void copyCell (Cell cellOld, Cell cellNew){
        CellType cellType = cellOld.getCellType();
        if (cellType == CellType.STRING) {
            cellNew.setCellValue(cellOld.getStringCellValue());
        } else if (cellType == CellType.NUMERIC) {
            cellNew.setCellValue(cellOld.getNumericCellValue());
        } else if (cellType == CellType.BLANK){
            cellNew.setCellValue(cellOld.getStringCellValue());
        } else if(cellType == CellType.BOOLEAN) {
            cellNew.setCellValue(cellOld.getBooleanCellValue());
        } else if(cellType == CellType.ERROR) {
            cellNew.setCellValue(cellOld.getErrorCellValue());
        } else if (cellType == CellType.FORMULA) {
            cellNew.setCellValue(cellOld.getCellFormula());
        } else {
            cellNew.setCellValue("");
        }
    }

    public static CellStyle createDefaultCellStyle(Workbook workbook) {
        Font font = workbook.createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 11);

        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setFont(font);

        return cellStyle;
    }

    public static void setCellValueAndStyle(Cell cell, String value, CellStyle style) {
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }
}
