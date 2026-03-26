package com.cumpleanos.importramite.service.implementation.confiteria;

import com.cumpleanos.importramite.persistence.model.confiteria.ConfiteriaDetalle;
import com.cumpleanos.importramite.persistence.records.ReposicionRequest;
import com.cumpleanos.importramite.service.exception.ExcelNotCreateException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.cumpleanos.importramite.utils.ExcelUtils.createHeaderStyle;

@Slf4j
@Service
public class ExelConfiteriaService {

    private static final String EMPRESA = "Importadora Cumpleaños";
    private static final String TITULO ="Pedido";

    private static final String[] COLUMNAS = {
            "CODIGO_BARRA",
            "ITEM",
            "DESCRIPCION",
            "PEDIDO"
    };

    public byte[] generarExcelConfiteria(ReposicionRequest request){
        try (Workbook workbook = new XSSFWorkbook()) {
            String proveedor = request.repo().getProveedor();
            String sheetName = ("Pedido " + proveedor).toUpperCase();
            Sheet sheet = workbook.createSheet(sheetName);

            int rowNum = 0;
            int totalCols = COLUMNAS.length; // 4 Columnas -> A:D

            // --- PROVEEDOR (fila 0) ------------------------------------------
            rowNum = crearFilaEncabezado(sheet, workbook, rowNum, totalCols,
                    proveedor.toUpperCase(),
                    estiloProveedorStyle(workbook),
                    18);

            // --- EMPRESA (fila 1) ---------------------------------------------
            rowNum = crearFilaEncabezado(sheet, workbook, rowNum, totalCols,
                    EMPRESA,
                    estiloEmpresaStyle(workbook),
                    13);

            // --- TÍTULO (fila 2) ----------------------------------------------
            rowNum = crearFilaEncabezado(sheet, workbook, rowNum, totalCols,
                    TITULO,
                    estiloTituloStyle(workbook),
                    14);

            // --- FECHA (fila 3) -----------------------------------------------
            String fecha = "Fecha: " + LocalDate.now()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            rowNum = crearFilaEncabezado(sheet, workbook, rowNum, totalCols,
                    fecha,
                    estiloFechaStyle(workbook),
                    11);

            // Fila vacía de separación
            sheet.createRow(rowNum++);

            // --- ENCABEZADOS DE COLUMNAS (fila 5) ----------------------------
            Row headerRow = sheet.createRow(rowNum++);
            CellStyle headerStyle = createHeaderStyle(workbook);
            for (int i = 0; i < COLUMNAS.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(COLUMNAS[i]);
                cell.setCellStyle(headerStyle);
            }

            // --- DETALLE DE PRODUCTOS ----------------------------------------
            CellStyle dataStyle  = estiloDataCell(workbook);
            CellStyle wrapStyle  = estiloWrapCell(workbook);

            for (ConfiteriaDetalle detalle : request.detalles()) {
                Row row = sheet.createRow(rowNum++);

                setCellValue(row, 0, detalle.getBarra(), dataStyle);
                setCellValue(row, 1, detalle.getItem(),        dataStyle);
                setCellValue(row, 2, detalle.getProNombre(), wrapStyle);   // col larga
                setCellValue(row, 3, String.valueOf(detalle.getPedido()),    dataStyle);
            }

            // --- AJUSTE DE COLUMNAS ------------------------------------------
            for (int i = 0; i < COLUMNAS.length; i++) {
                sheet.autoSizeColumn(i);
                // Ancho mínimo de 12 caracteres para columnas pequeñas
                if (sheet.getColumnWidth(i) < 12 * 256) {
                    sheet.setColumnWidth(i, 12 * 256);
                }
            }

            // --- SERIALIZAR --------------------------------------------------
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();

        } catch (IOException e) {
            log.error("Error al generar el excel de confitería", e);
            throw new ExcelNotCreateException("Error al generar el excel de confitería", e);
        }

    }

    private int crearFilaEncabezado(Sheet sheet, Workbook workbook,
                                    int rowNum, int totalCol,
                                    String text, CellStyle style , int altoPuntos) {

        Row row = sheet.createRow(rowNum++);
        row.setHeightInPoints(altoPuntos + 6);

        Cell cell = row.createCell(0);
                cell.setCellValue(text);
                cell.setCellStyle(style);

                //Merge desde col 0 hasta la ultima columna
        if (totalCol > 1) {
            sheet.addMergedRegion(
                    new CellRangeAddress(rowNum, rowNum, 0, totalCol-1)
            );
        }
        return rowNum +1 ;
    }

    /** Asigna valor de texto a una celda con su estilo. */
    private void setCellValue(Row row, int col, String value, CellStyle style) {
        Cell cell = row.createCell(col);
        cell.setCellValue(value != null ? value : "");
        cell.setCellStyle(style);
    }


    /** Proveedor: fondo azul oscuro, texto blanco, bold, centrado. */
    private CellStyle estiloProveedorStyle(Workbook wb) {
        CellStyle s = wb.createCellStyle();
        Font f = wb.createFont();
        f.setBold(true);
        f.setColor(IndexedColors.WHITE.getIndex());
        f.setFontHeightInPoints((short) 18);
        s.setFont(f);
        s.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        s.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        s.setAlignment(HorizontalAlignment.CENTER);
        s.setVerticalAlignment(VerticalAlignment.CENTER);
        return s;
    }

    /** Empresa: fondo azul claro, texto oscuro, bold, centrado. */
    private CellStyle estiloEmpresaStyle(Workbook wb) {
        CellStyle s = wb.createCellStyle();
        Font f = wb.createFont();
        f.setBold(true);
        f.setColor(IndexedColors.DARK_BLUE.getIndex());
        f.setFontHeightInPoints((short) 13);
        s.setFont(f);
        s.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
        s.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        s.setAlignment(HorizontalAlignment.CENTER);
        s.setVerticalAlignment(VerticalAlignment.CENTER);
        return s;
    }

    /** Título: sin fondo, bold, grande, centrado. */
    private CellStyle estiloTituloStyle(Workbook wb) {
        CellStyle s = wb.createCellStyle();
        Font f = wb.createFont();
        f.setBold(true);
        f.setFontHeightInPoints((short) 14);
        s.setFont(f);
        s.setAlignment(HorizontalAlignment.CENTER);
        s.setVerticalAlignment(VerticalAlignment.CENTER);
        return s;
    }

    /** Fecha: itálica, alineada a la derecha. */
    private CellStyle estiloFechaStyle(Workbook wb) {
        CellStyle s = wb.createCellStyle();
        Font f = wb.createFont();
        f.setItalic(true);
        f.setFontHeightInPoints((short) 11);
        s.setFont(f);
        s.setAlignment(HorizontalAlignment.RIGHT);
        s.setVerticalAlignment(VerticalAlignment.CENTER);
        return s;
    }


    /** Celda de dato normal con bordes. */
    private CellStyle estiloDataCell(Workbook wb) {
        CellStyle s = wb.createCellStyle();
        aplicarBordes(s);
        return s;
    }

    /** Celda de dato con wrap (para descripciones largas). */
    private CellStyle estiloWrapCell(Workbook wb) {
        CellStyle s = wb.createCellStyle();
        s.setWrapText(true);
        aplicarBordes(s);
        return s;
    }

    private void aplicarBordes(CellStyle s) {
        s.setBorderBottom(BorderStyle.THIN);
        s.setBorderTop(BorderStyle.THIN);
        s.setBorderLeft(BorderStyle.THIN);
        s.setBorderRight(BorderStyle.THIN);
    }
}