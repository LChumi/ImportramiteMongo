package com.cumpleanos.importramite.service.implementation;

import com.cumpleanos.importramite.persistence.model.Producto;
import com.cumpleanos.importramite.persistence.model.Tramite;
import com.cumpleanos.importramite.persistence.repository.TramiteRepository;
import com.cumpleanos.importramite.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class FileServiceImpl {

    private final TramiteRepository  tramiteRepository;

    public Tramite readExcelFile(MultipartFile file, String tramiteId, String observacion){
        List<Producto> productoList = new ArrayList<>();
        try (InputStream inputStream = file.getInputStream()) {
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            productoList = mapRowsToProducts(sheet);

            Tramite tramite = new Tramite();
            tramite.setId(tramiteId);
            tramite.setObservacion(observacion);
            tramite.setListProductos(productoList);
            tramiteRepository.save(tramite);
            return tramite;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Producto> mapRowsToProducts(Sheet sheet){
        List<Producto> productos = new ArrayList<>();
        Iterator<Row> rowIterator = sheet.iterator();

        //Leer encabezados
        Row headerRow = rowIterator.next();
        Row headerRow1 = rowIterator.next();
        Row headerRow2 = rowIterator.next();

        int counter = 0;
        while(rowIterator.hasNext()){
            Row row = rowIterator.next(); //Obtiene la siguiente fila

            if (FileUtils.isRowEmpty(row)) break;

            try {
                Producto producto = FileUtils.mapRowToProduct(row);
                counter++;
                producto.setSecuencia(counter);
                productos.add(producto);
            } catch (ParseException e) {
                log.error("Error al procesar la fila:  {}", e.getMessage());
            }
        }
        return productos;
    }
}
