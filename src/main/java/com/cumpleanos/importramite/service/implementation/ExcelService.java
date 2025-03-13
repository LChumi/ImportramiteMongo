package com.cumpleanos.importramite.service.implementation;

import com.cumpleanos.importramite.persistence.model.Producto;
import com.cumpleanos.importramite.persistence.model.Tramite;
import com.cumpleanos.importramite.utils.MapUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

@Service
public class ExcelService {

    public byte[] generarExcel(Tramite tramite) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Tramite");

        Row headerRow = sheet.createRow(0);
        String[] columnas = {
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

        for (int i = 0; i < columnas.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columnas[i]);
            cell.setCellStyle(getHeaderCellStyle(workbook));
        }

        //Llenar los datos
        int rowNum = 1;
        Map<String, Producto> tramiteProductMap = MapUtils.listByTramite(tramite);
        for (Producto producto : tramiteProductMap.values()) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(producto.getId());
            row.createCell(1).setCellValue(producto.getId1());
            row.createCell(2).setCellValue(producto.getNombre());
            row.createCell(3).setCellValue(producto.getBultos());
            row.createCell(4).setCellValue(producto.getCxb());
            row.createCell(5).setCellValue(producto.getItemAlterno() != null ? producto.getItemAlterno() : "");
            row.createCell(6).setCellValue(producto.getPvp() != null ? producto.getPvp() : 0.0);
            row.createCell(7).setCellValue(producto.getCxbAnterior() != null ? producto.getCxbAnterior() : 0);
            row.createCell(8).setCellValue(producto.getUbicacionBulto() != null ? producto.getUbicacionBulto() : "");
            row.createCell(9).setCellValue(producto.getUbicacionUnidad() != null ? producto.getUbicacionUnidad() : "");
            row.createCell(10).setCellValue(producto.getStockZhucay() != null ? producto.getStockZhucay() : 0);
            row.createCell(11).setCellValue(producto.getStockNarancay() != null ? producto.getStockNarancay() : 0);
            row.createCell(12).setCellValue(producto.getDescripcion() != null ? producto.getDescripcion() : "");
            row.createCell(13).setCellValue(producto.getBarraSistema() != null ? producto.getBarraSistema() : "");
            row.createCell(14).setCellValue(producto.getDiferencia() != null ? producto.getDiferencia() : 0);
        }

        for (int i = 0; i < columnas.length; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        return out.toByteArray();
    }

    private CellStyle getHeaderCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }
}
