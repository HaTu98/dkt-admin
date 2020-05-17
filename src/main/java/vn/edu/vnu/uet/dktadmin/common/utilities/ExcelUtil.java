package vn.edu.vnu.uet.dktadmin.common.utilities;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

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

    public static void copyRow(Row rowNew, Row rowOld) {
        for (int i = 0; i < rowOld.getLastCellNum(); i++) {
            Cell cellOld = rowOld.getCell(i);
            Cell cellNew = rowNew.createCell(i);
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
}
