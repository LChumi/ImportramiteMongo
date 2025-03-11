package com.cumpleanos.importramite.service.implementation;

import com.cumpleanos.importramite.persistence.api.ProductoApi;
import com.cumpleanos.importramite.persistence.model.Contenedor;
import com.cumpleanos.importramite.persistence.model.Producto;
import com.cumpleanos.importramite.persistence.model.Tramite;
import com.cumpleanos.importramite.persistence.repository.TramiteRepository;
import com.cumpleanos.importramite.utils.FileUtils;
import com.cumpleanos.importramite.utils.StringUtils;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class FileServiceImpl {

    private final TramiteRepository  tramiteRepository;
    private final ProductosClientServiceImpl productosClientService;

    public Tramite readExcelFile(MultipartFile file, String tramiteId,LocalDate fechaLlegada, String contenedorId){
        List<Producto> productoList = new ArrayList<>();
        try (InputStream inputStream = file.getInputStream()) {
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            productoList = mapRowsToProducts(sheet);

            Contenedor contenedor = Contenedor.builder()
                    .id(contenedorId)
                    .productos(productoList)
                    .usrBloquea("")
                    .finalizado(false)
                    .bloqueado(false)
                    .build();
            Tramite tramite = tramiteRepository.findById(tramiteId).orElse(new Tramite());

            // Si el trámite no existe, crea uno nuevo
            if (tramite.getId() == null) {
                tramite.setId(StringUtils.trimWhitespace(tramiteId));
                tramite.setFechaCarga(LocalDate.now());
                tramite.setFechaLlegada(fechaLlegada);
            } else {
                // Si el trámite ya existe, solo actualiza la fecha de llegada
                tramite.setFechaLlegada(fechaLlegada);
            }

            // Agrega el contenedor si la lista está vacía o si ya tiene contenedores
            if (tramite.getContenedores().isEmpty()) {
                tramite.getContenedores().add(contenedor);
            } else {
                boolean contenedorExistente = false;
                for (Contenedor cont : tramite.getContenedores()) {
                    if (cont.getId().equals(contenedor.getId())) {
                        contenedorExistente = true;
                        break;
                    }
                }
                if (!contenedorExistente) {
                    tramite.getContenedores().add(contenedor);
                }
            }

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
                getProducts(producto);
                productos.add(producto);
            } catch (ParseException e) {
                log.error("Error al procesar la fila:  {}", e.getMessage());
            }
        }
        return productos;
    }

    private void getProducts(Producto producto) {
        long bodega = 10000586L;
        ProductoApi api = productosClientService.getProduct(bodega,  producto.getId());
        if (api != null) {
            producto.setItemAlterno(api.pro_id1());
            producto.setPvp(api.pvp());
            producto.setCxbAnterior(api.cxb());
            producto.setUbicacionBulto(api.bulto());
            producto.setStockReal(api.stock_real());
            producto.setDescripcion(api.pro_nombre());
            producto.setBarraSistema(api.pro_id());
            if (!Objects.equals(producto.getCxb(), api.cxb())){
                producto.setDiferencia(producto.getCxb() - api.cxb());
            }
        } else {
            producto.setDescripcion("NUEVO PRODUCTO");
        }
    }
}
