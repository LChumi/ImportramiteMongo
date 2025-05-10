package com.cumpleanos.importramite.service.implementation;

import com.cumpleanos.importramite.persistence.api.EmailRecord;
import com.cumpleanos.importramite.persistence.api.ProductoApi;
import com.cumpleanos.importramite.persistence.model.Contenedor;
import com.cumpleanos.importramite.persistence.model.Emails;
import com.cumpleanos.importramite.persistence.model.Producto;
import com.cumpleanos.importramite.persistence.model.Tramite;
import com.cumpleanos.importramite.persistence.repository.EmailRepository;
import com.cumpleanos.importramite.persistence.repository.TramiteRepository;
import com.cumpleanos.importramite.service.exception.DocumentNotFoundException;
import com.cumpleanos.importramite.utils.CustomMultipartFile;
import com.cumpleanos.importramite.utils.EmailsUtils;
import com.cumpleanos.importramite.utils.FileUtils;
import com.cumpleanos.importramite.utils.StringUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
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

import static com.cumpleanos.importramite.utils.MessageUtil.MENSAJE_LLEGADA_BODEGA;
import static com.cumpleanos.importramite.utils.MessageUtil.MENSAJE_TRAMITE;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class FileServiceImpl {

    private final TramiteRepository tramiteRepository;
    private final ProductosClientServiceImpl productosClientService;
    private final ExcelService excelService;
    private final EmailClientServiceImpl emailClientService;
    private final EmailRepository emailRepository;

    public Tramite readExcelFile(MultipartFile file, String tramiteId, LocalDate fechaLlegada, String contenedorId) {
        List<Producto> productoList = new ArrayList<>();
        try (InputStream inputStream = file.getInputStream()) {
            Workbook workbook = WorkbookFactory.create(inputStream);
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();//
            Sheet sheet = workbook.getSheetAt(0);
            productoList = mapRowsToProducts(sheet, evaluator);

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

    public String sendTramiteEmail(String tramiteId){
        try {
            Tramite tramite = tramiteRepository.findById(tramiteId).orElseThrow(() -> new RuntimeException(tramiteId + " no encontrado"));
            String asunto = "Tramite " + tramite.getId().toUpperCase() + " - Confirmación de llegada al puerto";
            String mensaje = MENSAJE_TRAMITE(tramite.getId(), String.valueOf(tramite.getFechaLlegada()), String.valueOf(tramite.getContenedores().size()));
            byte[] excelByte = excelService.generarExcelPorContenedores(tramite);
            String nombreAdjunto = "Tramite-" + tramite.getId() + ".xlsx";
            MultipartFile fileExcel = FileUtils.converFileToMultipartFile(excelByte, nombreAdjunto);
            MultipartFile emailFile = getEmailMultipartFile(asunto.toUpperCase(), mensaje);
            emailClientService.sendEmailAdjutno(emailFile, fileExcel, nombreAdjunto);
            return "ok";
        } catch (Exception e){
            throw new RuntimeException("Error sending email", e);
        }
    }

    public String sendTramiteFinal(String tramiteId){
        try{
            Tramite tramite= tramiteRepository.findById(tramiteId).orElseThrow(() -> new DocumentNotFoundException("Tramite no registrado en el sistema"));
            String asunto = "Trámite " + tramite.getId().toUpperCase() + " -Registro de arribo a bodega";
            String mensaje = MENSAJE_LLEGADA_BODEGA(
                    tramite.getId(),
                    String.valueOf(tramite.getFechaArribo()),
                    String.valueOf(tramite.getHoraArribo()),
                    String.valueOf(tramite.getContenedores().size())
            );
            byte[] excelByte = excelService.generarExcelPorContenedores(tramite);
            String nombreAdjunto = "Tramite-" + tramite.getId() + ".xlsx";
            MultipartFile fileExcel = FileUtils.converFileToMultipartFile(excelByte, nombreAdjunto);
            MultipartFile emailFile = getEmailMultipartFile(asunto.toUpperCase(), mensaje);
            emailClientService.sendEmailAdjutno(emailFile, fileExcel, nombreAdjunto);
            return "Ok";
        } catch (Exception e){
            throw new RuntimeException("Error sending email", e);
        }
    }

    private MultipartFile getEmailMultipartFile(String asunto, String mensaje) throws JsonProcessingException {
        Emails e = emailRepository.findByTipo(1L).orElseThrow(() -> new DocumentNotFoundException("Email no encontrado"));
        String[] emails = EmailsUtils.addresseeToArray(e.getDestinatarios());
        EmailRecord email = new EmailRecord(
                emails,
                asunto,
                mensaje
        );
        // Serializa el objeto EmailRecord a JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String emailJson = objectMapper.writeValueAsString(email);
        return new CustomMultipartFile(emailJson.getBytes(), "email.json", "application/json");
    }

    private List<Producto> mapRowsToProducts(Sheet sheet,  FormulaEvaluator evaluator) {
        List<Producto> productos = new ArrayList<>();
        Iterator<Row> rowIterator = sheet.iterator();

        //Leer encabezados
        Row headerRow = rowIterator.next();

        int counter = 0;
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next(); //Obtiene la siguiente fila

            if (FileUtils.isRowEmpty(row)) break;
            try {
                Producto producto = FileUtils.mapRowToProduct(row, evaluator);
                if (producto.getId() == null || producto.getId().isEmpty()) {
                    log.error("Producto sin datos");
                } else {
                    counter++;
                    producto.setSecuencia(counter);
                    producto.calcularTotal();
                    getProducts(producto);
                    productos.add(producto);
                }
            } catch (ParseException e) {
                log.error("Error al procesar la fila:  {}", e.getMessage());
            }
        }
        return productos;
    }

    private void getProducts(Producto producto) {
        long bodega = 10000586L;
        long bodegaNarancay = 10000601L;
        ProductoApi api = productosClientService.getProduct(bodega, producto.getId());
        ProductoApi apiNarancay = productosClientService.getProduct(bodegaNarancay, producto.getId());
        if (api != null) {
            producto.setItemAlterno(api.pro_id1());
            producto.setPvp(api.pvp());
            producto.setCxbAnterior(api.cxb());
            producto.setUbicacionBulto(api.bulto());
            producto.setUbicacionUnidad(api.unidad());
            producto.setStockZhucay(api.stock_disponible());
            producto.setDescripcion(api.pro_nombre());
            producto.setBarraSistema(api.pro_id());
            if (!Objects.equals(producto.getCxb(), api.cxb())) {
                producto.setDiferencia(producto.getCxb() - api.cxb());
            }
            if (apiNarancay != null) {
                producto.setStockNarancay(apiNarancay.stock_disponible());
            }
        } else {
            producto.setDescripcion("NUEVO PRODUCTO");
        }
    }
}
