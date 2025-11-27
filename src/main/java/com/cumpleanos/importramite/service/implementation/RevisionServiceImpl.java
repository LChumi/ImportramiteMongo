package com.cumpleanos.importramite.service.implementation;

import com.cumpleanos.importramite.persistence.model.Contenedor;
import com.cumpleanos.importramite.persistence.model.ProductoCantidades;
import com.cumpleanos.importramite.persistence.model.Producto;
import com.cumpleanos.importramite.persistence.model.Tramite;
import com.cumpleanos.importramite.persistence.records.ProductValidateRequest;
import com.cumpleanos.importramite.persistence.records.RevisionRequest;
import com.cumpleanos.importramite.persistence.records.StatusResponse;
import com.cumpleanos.importramite.persistence.repository.ContenedorRepository;
import com.cumpleanos.importramite.persistence.repository.TramiteRepository;
import com.cumpleanos.importramite.service.exception.DocumentNotFoundException;
import com.cumpleanos.importramite.service.interfaces.IProductoService;
import com.cumpleanos.importramite.service.interfaces.IRevisionService;
import com.cumpleanos.importramite.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.cumpleanos.importramite.utils.ProductoStatus.*;
import static com.cumpleanos.importramite.utils.StringUtils.historial;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class RevisionServiceImpl implements IRevisionService {

    private final ContenedorRepository contenedorRepository;
    private final TramiteRepository tramiteRepository;
    private final IProductoService productoService;

    /**
     * Metodo para validar los productos comparando con la tabla Trámite
     * validando primero todos los contenedores disponibles
     *
     * @param tramiteId el ID del trámite registrado
     */
    public StatusResponse processTramiteCompletion(String tramiteId, String contenedorId) {
        String finalTramiteId = tramiteId.trim();
        String finalContenedorId = contenedorId.trim();
        Tramite tramite = tramiteRepository.findById(finalTramiteId)
                .orElseThrow(() -> new DocumentNotFoundException("Tramite " + finalTramiteId + " not found"));

        //Verificar si todos los contenedores estan finalizados
        List<Contenedor> contenedores = contenedorRepository.findByTramiteId(finalTramiteId).orElseThrow(() -> new DocumentNotFoundException("Tramite " + tramiteId + " not found"));
        boolean allCompleted = contenedores.stream()
                .allMatch(Contenedor::getFinalizado);

        if (!allCompleted) {
            for (Contenedor contenedor : contenedores) {
                if (contenedor.getContenedorId().equals(finalContenedorId)) {
                    if (contenedor.getEndHour() == null) {
                        contenedor.setEndHour(LocalTime.now());
                    }

                    contenedor.setBloqueado(true);
                    contenedor.setFinalizado(true);
                    contenedorRepository.save(contenedor);
                }
            }

            //Verificar Nuevamente los contenedores
            allCompleted = contenedores.stream()
                    .allMatch(Contenedor::getFinalizado);
        }

        if (allCompleted) {
            tramite.setProceso((short) 3);
            tramiteRepository.save(tramite);
            return new StatusResponse("Todos los contenedores fueron finalizados", true);
        }
        return new StatusResponse("Contenedor " + contenedorId + " finalizado, pero aún hay otros pendientes.", false);
    }

    /**
     * Metodo para validar la Revision con la informacion de las cantidades solicitadas en el Tramite
     *
     * @param tramiteStr    id del tramite
     * @param contenedorStr id del contenedor
     * @return lista de productos con sus respectivas actualizaciones
     */
    @Override
    public List<Producto> processProductRevision(String tramiteStr, String contenedorStr) {
        List<Producto> productos = productoService.findByTramiteIdAndContenedorId(tramiteStr, contenedorStr);

        for (Producto producto : productos) {

            Integer cantidadRevision = producto.getCantidadRevision();
            Integer bultos = producto.getBultos();

            if (cantidadRevision == null || cantidadRevision == 0) {
                producto.setEstadoRevision(NO_LLEGO.name());
                producto.setCantidadRevision(0);
                producto.setCantidadDiferenciaRevision(bultos != null ? bultos : 0);
                producto.setHistorialRevision(new ArrayList<>());
                producto.getHistorialRevision().add(historial(true));
                producto.setSecuencia(0);
            } else {
                if (bultos == null) {
                    producto.setBultos(0);
                    producto.setEstadoRevision(SIN_REGISTRO.name());
                    producto.setCantidadDiferenciaRevision(cantidadRevision);
                } else if (!producto.getEstadoRevision().equalsIgnoreCase(SIN_REGISTRO.name())) {
                    getStatusByCant(cantidadRevision, producto, bultos);
                }
            }
            productoService.save(producto);
        }

        processTramiteCompletion(tramiteStr, contenedorStr);
        return productoService.findByTramiteIdAndContenedorId(tramiteStr, contenedorStr);
    }

    @Override
    public List<Contenedor> listContenedoresByTramite(String tramiteId) {
        return contenedorRepository.findByTramiteId(tramiteId).orElseThrow(() -> new DocumentNotFoundException("Tramite " + tramiteId + " not found"));
    }

    @Override
    public List<Producto> findByTramiteId(String tramiteId, String contenedorId) {
        List<Producto> lista = productoService.findByTramiteIdAndContenedorId(tramiteId, contenedorId);
        return lista.stream()
                .filter(p -> p.getCantidadRevision() != null &&
                        p.getEstadoRevision() != null &&
                        p.getUsuarioRevision() != null &&
                        p.getHistorialRevision() != null)
                .collect(Collectors.toList());
    }

    @Override
    public StatusResponse getProducto(String tramite, String contenedor, String barcode) {
        String idProducto = tramite + "_" + contenedor + "_" + barcode;
        Producto p = productoService.findById(idProducto);
        if (p == null) {
            return new StatusResponse("Producto no encontrado", false);
        } else {
            return new StatusResponse("Producto encontrado", true);
        }
    }

    @Override
    public Producto updateProdcutoById(ProductValidateRequest request) {
        Producto pr = productoService.findById(request.productId());
        if (pr == null) {
            throw new DocumentNotFoundException("El producto no existe");
        }

        Integer nuevaCantidad = request.cantidad();
        if (nuevaCantidad == null || nuevaCantidad == 0) {
            log.warn("Cantidad vacía, no se registra.");
        } else {
            pr.getHistorialRevision().add("CANTIDAD MODIFICADA ANTERIOR: "+ pr.getCantidadRevision() + " NUEVA: " + nuevaCantidad);
            pr.setCantidadRevision(nuevaCantidad);
            Integer cantidadExistente = pr.getBultos();
            if (cantidadExistente == null) {
                pr.setEstadoRevision(SOBRANTE.name());
            } else {
                getStatusByCant(nuevaCantidad, pr, cantidadExistente);
            }
        }

        if (request.novedad() == null && (pr.getNovedad() == null || pr.getNovedad().isEmpty())) {
            log.info("Producto sin novedad");
        } else if (request.novedad() != null) {
            List<String> novedades = Optional.ofNullable(pr.getNovedad()).orElse(new ArrayList<>());
            novedades.add(request.novedad());
            pr.setNovedad(novedades);
        }

        pr.setUsrValida(request.usuario());

        return productoService.save(pr);
    }

    @Override
    public List<ProductoCantidades> getCantidades(String tramite, String contenedor, String barcode) {
        String idProducto = tramite + "_" + contenedor + "_" + barcode;
        Producto p = productoService.findById(idProducto);
        if (p == null) {
            throw new DocumentNotFoundException("El producto no existe");
        }

        List<ProductoCantidades> cantidades = p.getCantidades();
        if (cantidades != null && !cantidades.isEmpty()) {
            return cantidades;
        }
        return Collections.emptyList(); // mejor que devolver null
    }


    private void getStatusByCant(Integer cantidadValidada, Producto pr, Integer cantidad) {
        int diferencia = cantidad - cantidadValidada;

        pr.setCantidadDiferenciaRevision(Math.abs(diferencia));
        if (diferencia == 0) {
            pr.setEstadoRevision(COMPLETO.name());
        } else if (diferencia < 0) {
            pr.setEstadoRevision(SOBRANTE.name());
        } else {
            pr.setEstadoRevision(INCOMPLETO.name());
        }
    }

    /**
     * Metodo para crear o actualizar datos de la tabal revision
     *
     * @param request Cuerpo de solicitud tramite contendor, usuario, barra, status
     * @return nueva revision si no existe si existe va sumando la cantidad ++ o restando -- dependiendo el status
     */
    @Transactional
    @Override
    public Producto updateCantidadByBarra(RevisionRequest request) {
        String tramiteId = StringUtils.trimWhitespace(request.tramiteId());
        String barra = StringUtils.trimWhitespace(request.barra());
        String contenedorId = StringUtils.trimWhitespace(request.contenedor());

        Tramite tramite = tramiteRepository.findById(tramiteId)
                .orElseThrow(() -> new DocumentNotFoundException("Trámite no encontrado"));

        if (tramite.getProceso() == 1) {
            tramite.setProceso((short) 2);
            tramiteRepository.save(tramite);
        }

        Contenedor contenedor = contenedorRepository.findByContenedorId(contenedorId)
                .orElseThrow(() -> new DocumentNotFoundException("Contenedor no encontrado: " + contenedorId));

        if (contenedor.getStartDate() == null && contenedor.getStartHour() == null) {
            contenedor.setStartDate(LocalDate.now());
            contenedor.setStartHour(LocalTime.now());
            contenedorRepository.save(contenedor);
        }

        Producto revision = productoService.findByBarcodeAndTramiteIdAndContenedorId(barra, tramiteId, contenedorId);

        boolean esNuevo = revision.getBarcode() == null || revision.getBarcode().isEmpty();

        if (esNuevo) {
            inicializarNuevoProducto(revision, barra, tramiteId, contenedorId, request.usuario());
        } else {
            revision.setUsuarioRevision(request.usuario());
            revision.setCantidadRevision(Optional.ofNullable(revision.getCantidadRevision()).orElse(0));
            revision.setHistorialRevision(Optional.ofNullable(revision.getHistorialRevision()).orElse(new ArrayList<>()));

            aplicarIncrementoGlobal(revision, request.status());
            updateCantidades(revision, request);
        }

        return productoService.save(revision);
    }

    private void inicializarNuevoProducto(Producto revision, String barra, String tramiteId, String contenedorId, String usuario) {
        revision.setBarcode(barra);
        revision.setContenedorId(contenedorId);
        revision.setTramiteId(tramiteId);
        revision.setNombre(PRODUCTO_SIN_REGISTRO.name());
        revision.setCantidadRevision(1);
        revision.setUsuarioRevision(usuario);
        revision.setEstadoRevision(SIN_REGISTRO.name());
        revision.setHistorialRevision(new ArrayList<>());
        revision.getHistorialRevision().add(historial(true));
        revision.generateId();
    }

    private void aplicarIncrementoGlobal(Producto revision, boolean status) {
        if (status) {
            revision.setCantidadRevision(revision.getCantidadRevision() + 1);
            revision.getHistorialRevision().add(historial(true));
            if (SIN_REGISTRO.name().equalsIgnoreCase(revision.getEstadoRevision())) {
                revision.setEstadoRevision(SIN_REGISTRO.name());
            } else {
                getStatusByCant(revision.getCantidadRevision(), revision, revision.getBultos());
            }
        } else {
            int nuevaCantidad = revision.getCantidadRevision() - 1;
            if (nuevaCantidad >= 0) {
                revision.setCantidadRevision(nuevaCantidad);
                revision.getHistorialRevision().add(historial(false));
                revision.setEstadoRevision(RETIRADO.name());
            } else {
                throw new DocumentNotFoundException("Cantidad no puede ser menor que cero");
            }
        }
    }

    private void updateCantidades(Producto p, RevisionRequest r) {
        if (r.cantidad() != null && r.cxb() != null && p.getCantidades() != null && !p.getCantidades().isEmpty()) {

            Optional<ProductoCantidades> optCant = p.getCantidades().stream()
                    .filter(c -> c.getCantidad() == r.cantidad() && c.getCxb() == r.cxb())
                    .findFirst();

            if (optCant.isPresent()) {
                ProductoCantidades cant = optCant.get();

                int revisionActual = Optional.of(cant.getCantRevision()).orElse(0);

                if (r.status()) {
                    cant.setCantidad(revisionActual + 1);
                } else {
                    if (revisionActual > 0) {
                        cant.setCantidad(revisionActual - 1);
                    } else {
                        throw new DocumentNotFoundException("La revision no puede ser menor que cero");
                    }
                }

                if (r.obsCxb() != null && !r.obsCxb().isEmpty()) {
                    String obs = Optional.ofNullable(cant.getObservacion()).orElse("");
                    cant.setObservacion(
                            obs.isEmpty()
                            ? r.obsCxb()
                                    : obs + " | " +r.obsCxb()
                    );
                }
                if (r.cxbRevision() > 0) {
                    if (cant.getCxbRevision() != 0) {
                        String obs = Optional.ofNullable(cant.getObservacion()).orElse("");
                        cant.setObservacion(
                                obs.isEmpty()
                                        ? "CXB modificado anterior " + cant.getCantRevision() + " nuevo " + r.cxbRevision()
                                        : obs + " | " + "CXB modificado anterior " + cant.getCantRevision() + " nuevo " + r.cxbRevision()
                        );
                        cant.setCxbRevision(r.cxbRevision());
                    }
                }

            } else {
                if (r.status() && r.cxbNov() > 0) {
                    ProductoCantidades nueva = ProductoCantidades.builder()
                            .cantidad(0)
                            .cxb(r.cxbNov())
                            .observacion("Cxb nuevo registrado " + r.cxbNov())
                            .build();
                    p.getCantidades().add(nueva);
                }
            }

        }else if (p.getCantidades() == null || p.getCantidades().isEmpty()){
            if (r.status() && r.cxbNov() > 0) {
                ProductoCantidades nueva = ProductoCantidades.builder()
                        .cantidad(0)
                        .cxb(r.cxbNov())
                        .observacion("Cxb nuevo registrado " + r.cxbNov())
                        .build();
                p.getCantidades().add(nueva);
            }
        }
    }
}