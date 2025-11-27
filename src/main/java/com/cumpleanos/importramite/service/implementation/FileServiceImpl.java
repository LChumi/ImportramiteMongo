package com.cumpleanos.importramite.service.implementation;

import com.cumpleanos.importramite.persistence.api.EmailRecord;
import com.cumpleanos.importramite.persistence.api.ProductoApi;
import com.cumpleanos.importramite.persistence.model.*;
import com.cumpleanos.importramite.persistence.repository.ContenedorRepository;
import com.cumpleanos.importramite.persistence.repository.EmailRepository;
import com.cumpleanos.importramite.persistence.repository.ProductoRepository;
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
import java.util.*;

import static com.cumpleanos.importramite.utils.MessageUtil.MENSAJE_LLEGADA_BODEGA;
import static com.cumpleanos.importramite.utils.MessageUtil.MENSAJE_TRAMITE;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class FileServiceImpl {

    private final TramiteRepository tramiteRepository;
    private final ProductoRepository productoRepository;
    private final ContenedorRepository contenedorRepository;

    private final ProductosClientServiceImpl productosClientService;
    private final ExcelService excelService;
    private final EmailClientServiceImpl emailClientService;
    private final EmailRepository emailRepository;

    public Tramite readExcelFile(MultipartFile file, String tramiteId, LocalDate fechaLlegada, String contenedorId) {
        List<String> productoList = new ArrayList<>();
        try (InputStream inputStream = file.getInputStream()) {
            Workbook workbook = WorkbookFactory.create(inputStream);
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();//
            Sheet sheet = workbook.getSheetAt(0);
            productoList = mapRowsToProducts(sheet, evaluator, tramiteId, contenedorId);

            Contenedor contenedor = Contenedor.builder()
                    .contenedorId(contenedorId)
                    .productIds(productoList)
                    .finalizado(false)
                    .bloqueado(false)
                    .tramiteId(tramiteId)
                    .build();
            Contenedor c = contenedorRepository.save(contenedor);
            if (c.getId() == null) {
                throw new RuntimeException("Error al guardar el contenedor: " + contenedor);
            }
            // Busca el trámite en la base de datos o crea uno nuevo si no existe
            Tramite tramite = tramiteRepository.findById(tramiteId).orElse(new Tramite());

            // Si el trámite no existe, crea uno nuevo
            if (tramite.getId() == null) {
                tramite.setId(StringUtils.trimWhitespace(tramiteId));
                tramite.setFechaCarga(LocalDate.now());
                tramite.setFechaLlegada(fechaLlegada);
                tramite.setProceso((short) 1);
            } else {
                // Si el trámite ya existe, solo actualiza la fecha de llegada
                tramite.setFechaLlegada(fechaLlegada);
            }

            // Agrega el contenedor si la lista está vacía o si ya tiene contenedores
            if (tramite.getContenedoresIds() == null || tramite.getContenedoresIds().isEmpty()) {
                tramite.setContenedoresIds(new ArrayList<>());
                tramite.getContenedoresIds().add(c.getContenedorId());
            } else {
                boolean contenedorExistente = false;
                for (String cont : tramite.getContenedoresIds()) {
                    if (cont.equals(c.getContenedorId())) {
                        contenedorExistente = true;
                        break;
                    }
                }
                if (!contenedorExistente) {
                    tramite.getContenedoresIds().add(c.getContenedorId());
                }
            }

            tramiteRepository.save(tramite);
            return tramite;
        } catch (IOException e) {
            throw new RuntimeException(e);
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

    private List<String> mapRowsToProducts(Sheet sheet, FormulaEvaluator evaluator, String tramiteId, String contenedorId) {
        List<String> productos = new ArrayList<>();
        Iterator<Row> rowIterator = sheet.iterator();

        //Leer encabezados
        Row headerRow = rowIterator.next();

        int counter = 0;
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next(); //Obtiene la siguiente fila

            if (FileUtils.isRowEmpty(row)) break;
            try {
                Producto producto = FileUtils.mapRowToProduct(row, evaluator);
                if (producto.getBarcode() == null || producto.getBarcode().isEmpty()) {
                    log.error("Producto sin datos");
                } else {
                    counter++;
                    producto.setTramiteId(tramiteId);
                    producto.setContenedorId(contenedorId);
                    producto.setSecuencia(counter);
                    producto.recalcularTotalPrincipal();
                    producto.generateId();
                    getProducts(producto);

                    Optional<Producto> foundOpt = productoRepository.findByBarcodeAndTramiteIdAndContenedorId(
                            producto.getBarcode(), tramiteId, contenedorId
                    );

                    if (foundOpt.isEmpty()) {
                        Producto p = productoRepository.save(producto);
                        if (p.getId() == null) {
                            throw new RuntimeException("Error al guardar el producto: " + producto);
                        }
                        productos.add(p.getBarcode());
                        log.info("Producto guardado: {}", p);
                    } else {
                        Producto found = foundOpt.get();
                        log.info("Producto existente, se actualizarán los campos");
                        int cxbNuevo = producto.getCxb();
                        int bultosNuevos = producto.getBultos();

                        if (found.getCantidades() == null || found.getCantidades().isEmpty()) {
                            if (Objects.equals(found.getCxb(), cxbNuevo)) {
                                found.sumarBultosPrincipal(bultosNuevos);
                            } else {
                                found.setCantidades(new ArrayList<>());

                                ProductoCantidades cantPrincipal = ProductoCantidades.builder()
                                        .cantidad(found.getBultos() != null ? found.getBultos() : 0)
                                        .cxb(found.getCxb() != null ? found.getCxb() : 0)
                                        .cantRevision(0)
                                        .build();

                                found.getCantidades().add(cantPrincipal);

                                ProductoCantidades cantNueva = ProductoCantidades.builder()
                                        .cantidad(bultosNuevos)
                                        .cxb(cxbNuevo)
                                        .cantRevision(0)
                                        .build();

                                found.getCantidades().add(cantNueva);

                                found.recalcularTotalDesdeCantidades();
                            }
                        } else {
                            ProductoCantidades existente = found.getCantidades().stream()
                                    .filter(c -> c.getCxb() == cxbNuevo)
                                    .findFirst()
                                    .orElse(null);

                            if (existente != null){
                                existente.setCantidad(existente.getCantidad() + bultosNuevos);
                            } else {
                                ProductoCantidades cantNueva = ProductoCantidades.builder()
                                        .cantidad(bultosNuevos)
                                        .cxb(cxbNuevo)
                                        .cantRevision(0)
                                        .build();
                                found.getCantidades().add(cantNueva);
                            }

                            found.recalcularTotalDesdeCantidades();
                        }

                        Producto p = productoRepository.save(found);
                        if (p.getId() == null) {
                            throw new RuntimeException("Error al actualizar el producto: " + found);
                        }
                        log.info("Producto actualizado: {}", p);
                    }
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
        ProductoApi api = productosClientService.getProduct(bodega, producto.getBarcode());
        ProductoApi apiNarancay = productosClientService.getProduct(bodegaNarancay, producto.getBarcode());
        String novedad = productosClientService.getMatches(bodega, producto.getBarcode(), producto.getId1());
        String emp2 = productosClientService.exitInCompany(2L ,  producto.getBarcode(), producto.getId1());
        String emp3 = productosClientService.exitInCompany(3L , producto.getBarcode(), producto.getId1());
        String emp4 = productosClientService.exitInCompany(4L, producto.getBarcode(), producto.getId1());
        producto.setObservacion(novedad);
        producto.setIPC(emp2);
        producto.setPNCE(emp3);
        producto.setIEPNC(emp4);
        if (api != null) {
            producto.setItemAlterno(api.proId1());
            producto.setPvp(api.pvp());
            producto.setCxbAnterior(api.cxb());
            producto.setUbicacionBulto(api.bulto());
            producto.setUbicacionUnidad(api.unidad());
            producto.setStockZhucay(api.stockDisponible());
            producto.setDescripcion(api.nombre());
            producto.setBarraSistema(api.proId());
            if (!Objects.equals(producto.getCxb(), api.cxb())) {
                producto.setDiferencia(producto.getCxb() - api.cxb());
            }
            if (apiNarancay != null) {
                producto.setStockNarancay(apiNarancay.stockDisponible());
            }
        } else {
            producto.setDescripcion("NUEVO PRODUCTO");
        }
    }

    public String sendTramiteFinal(String tramiteId) {
        try {
            Tramite tramite = tramiteRepository.findById(tramiteId).orElseThrow(() -> new DocumentNotFoundException("Tramite no registrado en el sistema"));
            String asunto = "Trámite " + tramite.getId().toUpperCase() + " -Registro de arribo a bodega";
            String mensaje = MENSAJE_LLEGADA_BODEGA(
                    tramite.getId(),
                    String.valueOf(tramite.getFechaArribo()),
                    String.valueOf(tramite.getHoraArribo()),
                    String.valueOf(tramite.getContenedoresIds().size())
            );

            getAndSendExcel(tramite, asunto, mensaje);

            return "Ok";
        } catch (Exception e) {
            throw new RuntimeException("Error sending email", e);
        }
    }

    public String sendTramiteEmail(String tramiteId) {
        try {
            Tramite tramite = tramiteRepository.findById(tramiteId).orElseThrow(() -> new RuntimeException(tramiteId + " no encontrado"));

            String asunto = "Llegada del Tramite " + tramite.getId().toUpperCase() + " - al puerto de Guayaquil";
            String mensaje = MENSAJE_TRAMITE(
                    tramite.getId(),
                    String.valueOf(tramite.getFechaLlegada()),
                    String.valueOf(tramite.getContenedoresIds().size())
            );

            getAndSendExcel(tramite, asunto, mensaje);


            return "ok";
        } catch (Exception e) {
            throw new RuntimeException("Error sending email", e);
        }
    }

    private void getAndSendExcel(Tramite tramite, String asunto, String mensaje) throws IOException {
        byte[] excelByte = excelService.generarExcelPorContenedores(tramite);
        String nombreAdjunto = "Tramite-" + tramite.getId() + ".xlsx";

        MultipartFile fileExcel = FileUtils.converFileToMultipartFile(excelByte, nombreAdjunto);
        MultipartFile emailFile = getEmailMultipartFile(asunto.toUpperCase(), mensaje);

        List<MultipartFile> files = List.of(fileExcel);
        ObjectMapper mapper = new ObjectMapper();
        byte[] nombresJsonBytes = mapper.writeValueAsBytes(List.of(nombreAdjunto));
        MultipartFile nombresFile = FileUtils.converFileToMultipartFile(nombresJsonBytes, "nombres.json");

        emailClientService.enviarConAdjuntos(emailFile, files, nombresFile);
    }
}