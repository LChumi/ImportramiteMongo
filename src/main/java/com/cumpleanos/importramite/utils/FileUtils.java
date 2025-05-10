package com.cumpleanos.importramite.utils;

import com.cumpleanos.importramite.persistence.model.Producto;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;

@Slf4j
public class FileUtils {

    public static boolean isRowEmpty(Row row) {
        if (row == null) {
            return true;
        }
        for (int i = 0; i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }

    public static Producto mapRowToProduct(Row row, FormulaEvaluator evaluator) throws ParseException {

        return Producto.builder()
                .id(getCellValueClean(row.getCell(0)))
                .id1(getCellValueSafely(row.getCell(1)))
                .nombre(getCellValueSafely(row.getCell(2)))
                .bultos(parseIntegerSafely(row.getCell(3), evaluator))
                .cxb(parseIntegerSafely(row.getCell(4), evaluator))
                .build();
    }

    //Obtener valor seguro de una celda
    private static String getCellValueSafely(Cell cell) {
        if (cell == null) {
            return "";
        }
        DataFormatter formatter = new DataFormatter();
        return formatter.formatCellValue(cell).trim();
    }

    // Conversión segura de String a Integer
    private static int parseIntegerSafely(String value) {
        try {
            return value.isEmpty() ? 0 : Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    // Conversión segura de String a Double
    private static double parseDoubleSafely(String value) {
        if (value == null || value.isEmpty()) {
            return 0.0;
        }
        try {
            // Elimina cualquier símbolo de moneda y caracteres no numéricos
            value = value.replaceAll("[^\\d,.-]", "");

            // Usa NumberFormat para analizar el valor
            NumberFormat format = NumberFormat.getInstance();
            return format.parse(value).doubleValue();
        } catch (Exception e) {
            System.err.println("Error al convertir valor a Double: " + value);
            return 0.0; // Valor predeterminado
        }
    }

    private static String getCellValueClean(Cell cell) {
        if (cell == null) {
            return "";
        }
        DataFormatter formatter = new DataFormatter();
        String rawValue = formatter.formatCellValue(cell).trim();

        return rawValue.replaceAll("[^0-9]", "");
    }

    private static int parseIntegerSafely(Cell cell, FormulaEvaluator evaluator) {
        if (cell == null) {
            return 0;
        }

        try {
            if (cell.getCellType() == CellType.FORMULA) {
                // Evaluar la celda si contiene una fórmula
                CellValue cellValue = evaluator.evaluate(cell);
                if (cellValue != null && cellValue.getCellType() == CellType.NUMERIC) {
                    return (int) cellValue.getNumberValue();
                }
            } else {
                // Si no es fórmula, procesar normalmente
                String value = new DataFormatter().formatCellValue(cell).trim();
                return value.isEmpty() ? 0 : Integer.parseInt(value);
            }
        } catch (NumberFormatException e) {
            return 0;
        }
        return 0;
    }

    public static MultipartFile converFileToMultipartFile(byte[] fileBytes, String nombreAdjunto) throws IOException {
        return new CustomMultipartFile(fileBytes, nombreAdjunto,
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }

}
