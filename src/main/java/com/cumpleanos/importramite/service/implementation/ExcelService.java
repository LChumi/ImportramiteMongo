package com.cumpleanos.importramite.service.implementation;

import com.cumpleanos.importramite.persistence.model.Contenedor;
import com.cumpleanos.importramite.persistence.model.Producto;
import com.cumpleanos.importramite.persistence.model.Tramite;
import com.cumpleanos.importramite.service.exception.ExcelNotCreateException;
import com.cumpleanos.importramite.utils.MapUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

@Slf4j
@Service
public class ExcelService {

    private static final String[] columnas = {
            "COD.BARRA",
            "ITEM",
            "NUEVA_DESCRIPCION",
            "CANT",
            "CXB",
            "ITEM_ALTERNO",
            "PVP",
            "CXB_ANT",
            "UBICACION_BULTO",
            "UBICACION_UNIDAD",
            "STOCK_ZHUCAY",
            "STOCK_NARANCAY",
            "DESCRIPCION",
            "BARRA_SISTEMA",
            "DIFERENCIAS"
    };

    public byte[] generarExcel(Tramite tramite) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Tramite");

        Row headerRow = sheet.createRow(0);

        for (int i = 0; i < columnas.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columnas[i]);
            cell.setCellStyle(getHeaderCellStyle(workbook));
        }

        //Llenar los datos
        int rowNum = 1;
        Map<String, Producto> tramiteProductMap = MapUtils.listByTramite(tramite);
        for (Producto producto : tramiteProductMap.values()) {
            rowNum = getRowProduct(sheet, rowNum, producto);
        }

        for (int i = 0; i < columnas.length; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        return out.toByteArray();
    }

    public byte[] generarExcelPorContenedores (Tramite tramite){
        try(Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Tramite"+ tramite.getId());
            int rowNum = 0;//Control de filas

            Row tramiteRow = sheet.createRow(rowNum++);
            Cell tramiteCell = tramiteRow.createCell(0);
            tramiteCell.setCellValue("Trámite: "+tramite.getId());
            tramiteCell.setCellStyle(getHeaderCellStyle(workbook));

            for (Contenedor contenedor: tramite.getContenedores()) {
                Row contenedorRow = sheet.createRow(rowNum++);
                Cell contenedorCell = contenedorRow.createCell(1);
                contenedorCell.setCellValue("Contenedor: "+contenedor.getId());
                contenedorCell.setCellStyle(getHeaderCellStyle(workbook));

                // Agregar encabezados de columnas
                Row headerRow = sheet.createRow(rowNum++);
                for (int i = 0; i < columnas.length; i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(columnas[i]);
                    cell.setCellStyle(getHeaderCellStyle(workbook));
                }

                // Agregar productos del contenedor
                for (Producto producto: contenedor.getProductos()) {
                    rowNum = getRowProduct(sheet, rowNum, producto);
                }

                rowNum++; // Espacio extra entre contenedores
            }

            //Ajustar el tamaño de las columnas
            for (int i = 0; i < columnas.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            workbook.close();
            return out.toByteArray();

        } catch (IOException e){
            log.error("Error al generar el excel de tramite", e);
            throw new ExcelNotCreateException("Error al generar el excel de tramite"+ tramite.getId(), e);
        }
    }

    private int getRowProduct(Sheet sheet, int rowNum, Producto producto) {
        Row row = sheet.createRow(rowNum++);
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);

        // Estilo para celdas con texto largo
        CellStyle wrapStyle = sheet.getWorkbook().createCellStyle();
        wrapStyle.cloneStyleFrom(cellStyle); // Aplicar bordes al estilo de texto
        wrapStyle.setWrapText(true);

        String[] valores = {
                producto.getId(),
                producto.getId1(),
                producto.getNombre(),
                String.valueOf(producto.getBultos()),
                String.valueOf(producto.getCxb()),
                producto.getItemAlterno() != null ? producto.getItemAlterno() : "",
                String.valueOf(producto.getPvp() != null ? producto.getPvp() : 0.0),
                String.valueOf(producto.getCxbAnterior() != null ? producto.getCxbAnterior() : 0),
                producto.getUbicacionBulto() != null ? producto.getUbicacionBulto() : "",
                producto.getUbicacionUnidad() != null ? producto.getUbicacionUnidad() : "",
                String.valueOf(producto.getStockZhucay() != null ? producto.getStockZhucay() : 0),
                String.valueOf(producto.getStockNarancay() != null ? producto.getStockNarancay() : 0),
                producto.getDescripcion() != null ? producto.getDescripcion() : "",
                producto.getBarraSistema() != null ? producto.getBarraSistema() : "",
                String.valueOf(producto.getDiferencia() != null ? producto.getDiferencia() : 0)
        };

        for (int i = 0; i < valores.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(valores[i]);
            cell.setCellStyle((i == 8 || i == 9 || i == 12) ? wrapStyle : cellStyle); // Aplicar wrap solo en texto largo
        }

        return rowNum;
    }

    private CellStyle getHeaderCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex()); // Texto en blanco
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex()); // Fondo azul
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

}
