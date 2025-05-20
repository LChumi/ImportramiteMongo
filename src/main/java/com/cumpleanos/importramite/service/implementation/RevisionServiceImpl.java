package com.cumpleanos.importramite.service.implementation;

import com.cumpleanos.importramite.persistence.model.Contenedor;
import com.cumpleanos.importramite.persistence.model.Producto;
import com.cumpleanos.importramite.persistence.model.Tramite;
import com.cumpleanos.importramite.persistence.records.RevisionRequest;
import com.cumpleanos.importramite.persistence.repository.ContenedorRepository;
import com.cumpleanos.importramite.persistence.repository.TramiteRepository;
import com.cumpleanos.importramite.service.exception.DocumentNotFoundException;
import com.cumpleanos.importramite.service.interfaces.IProductoService;
import com.cumpleanos.importramite.service.interfaces.IRevisionService;
import com.cumpleanos.importramite.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.cumpleanos.importramite.utils.ProductoStatus.*;
import static com.cumpleanos.importramite.utils.StringUtils.historial;

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
     * @return devuelve la lista de revision actualizada comparando las cantidades de trámite y de revision actualizando estados
     */
    @Override
    public List<Producto> validateAndProcessTramite(String tramiteId, String contenedorId) {
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
                    contenedor.setEndHour(LocalTime.now());
                    contenedor.setFinalizado(true);
                }
            }
            tramiteRepository.save(tramite);

            //Verificar Nuevamente los contenedores
            allCompleted = contenedores.stream()
                    .allMatch(Contenedor::getFinalizado);
        }

        if (allCompleted) {
            tramite.setProceso((short) 3);
            tramiteRepository.save(tramite);
        }
        return productoService.findByTramiteIdAndContenedorId(finalTramiteId, finalContenedorId);
    }

    /**
     * Metodo para validar la Revision con la informacion de las cantidades solicitadas en el Tramite
     *
     * @param tramiteStr    id del tramite
     * @param contenedorStr id del contenedor
     * @return lista de productos con sus respectivas actualizaciones
     */
    @Override
    public List<Producto> updateRevisionWithTramiteQuantities(String tramiteStr, String contenedorStr) {
        Tramite tramite = tramiteRepository.findById(tramiteStr.trim())
                .orElseThrow(() -> new DocumentNotFoundException("Tramite no encontrado"));

        List<Producto> productos = productoService.findByTramiteIdAndContenedorId(tramiteStr, contenedorStr);


        for (Producto producto : productos) {

            if (producto.getCantidadRevision() == null || producto.getCantidadRevision() == 0) {
                producto.setEstadoRevision(NO_LLEGO.name());
                producto.setCantidadRevision(0);
                producto.setCantidadDiferenciaRevision(producto.getBultos());
                producto.setHistorialRevision(new ArrayList<>());
                producto.getHistorialRevision().add(historial(true));
                producto.setSecuencia(0);
            } else {
                int cantidadPedida = producto.getBultos();
                int diferencia = producto.getBultos() - producto.getCantidadRevision();
                producto.setCantidadDiferenciaRevision(Math.abs(diferencia));
                producto.setCantidadRevision(producto.getCantidadRevision() + cantidadPedida);
                if (diferencia == 0) {
                    producto.setEstadoRevision(COMPLETO.name());
                } else if (diferencia > 0) {
                    producto.setEstadoRevision(SOBRANTE.name());
                } else {
                    producto.setEstadoRevision(FALTANTE.name());
                }
            }

            productoService.save(producto);
        }

        tramite.setProceso((short) 3);
        tramiteRepository.save(tramite);
        return productoService.findByTramiteIdAndContenedorId(tramiteStr, contenedorStr);
    }

    @Override
    public List<Contenedor> listContenedoresByTramite(String tramiteId) {
        return contenedorRepository.findByTramiteId(tramiteId).orElseThrow(() -> new DocumentNotFoundException("Tramite " + tramiteId + " not found"));
    }

    @Override
    public List<Producto> findByTramiteId(String tramiteId, String contenedorId) {
        List<Producto> lista =  productoService.findByTramiteIdAndContenedorId(tramiteId, contenedorId);
        return lista.stream()
                .filter(p -> p.getCantidadRevision() != null &&
                        p.getEstadoRevision() != null &&
                        p.getUsuarioRevision() != null &&
                        p.getHistorialRevision() != null)
                        .collect(Collectors.toList());
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
            // Crear nueva revisión
            revision.setBarcode(barra);
            revision.setContenedorId(contenedorId);
            revision.setTramiteId(tramiteId);
            revision.setNombre(PRODUCTO_SIN_REGISTRO.name());
            revision.setCantidadRevision(0);
            revision.setUsuarioRevision(request.usuario());
            revision.setEstadoRevision(SIN_REGISTRO.name());
            revision.setHistorialRevision(new ArrayList<>());
            revision.generateId();
        }

        revision.setUsuarioRevision(request.usuario());
        // Asegurar inicialización segura antes de operar
        if (revision.getCantidadRevision() == null) {
            revision.setCantidadRevision(0);
        }
        if (revision.getHistorialRevision() == null) {
            revision.setHistorialRevision(new ArrayList<>());
        }

        if (request.status()) {
            // Sumar cantidad
            revision.setCantidadRevision(revision.getCantidadRevision() + 1);
            revision.getHistorialRevision().add(historial(true));
            if (revision.getEstadoRevision() != null && revision.getEstadoRevision().equalsIgnoreCase(SIN_REGISTRO.name())) {
                revision.setEstadoRevision(SIN_REGISTRO.name());
            }
            revision.setEstadoRevision(AGREGADO.name());
        } else {
            // Restar cantidad
            int nuevaCantidad = revision.getCantidadRevision() - 1;
            if (nuevaCantidad >= 0) {
                revision.setCantidadRevision(nuevaCantidad);
                revision.getHistorialRevision().add(historial(false));
                revision.setEstadoRevision(ELIMINADO.name());
            } else {
                throw new DocumentNotFoundException("Cantidad no puede ser menor que cero");
            }
        }

        return productoService.save(revision);
    }

}