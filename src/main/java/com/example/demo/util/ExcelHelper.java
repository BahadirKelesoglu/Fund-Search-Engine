package com.example.demo.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

import java.math.BigDecimal;

@Slf4j
@UtilityClass
public class ExcelHelper {

    //In case of cell may have another type
    public static String getStringValue(Cell cell) {
        if (cell == null) return null;
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf((int) cell.getNumericCellValue());
            default -> "";
        };
    }

    //In case of Numeric Value have another type
    public static BigDecimal getBigDecimalValue(Cell cell) {
        if (cell == null) return BigDecimal.ZERO;
        try {
            if (cell.getCellType() == CellType.NUMERIC) {
                return BigDecimal.valueOf(cell.getNumericCellValue());
            } else if (cell.getCellType() == CellType.STRING) {
                String val = cell.getStringCellValue().replace(",", ".").trim();
                return val.isEmpty() ? BigDecimal.ZERO : new BigDecimal(val);
            }
        } catch (Exception e) {
            log.warn("Numeric value is cannot read {}", cell);
        }
        return BigDecimal.ZERO;
    }
}
