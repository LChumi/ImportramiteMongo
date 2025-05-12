package com.cumpleanos.importramite.service.implementation;

import com.cumpleanos.importramite.persistence.model.Contenedor;
import com.cumpleanos.importramite.persistence.model.Producto;
import com.cumpleanos.importramite.persistence.model.Revision;
import com.cumpleanos.importramite.persistence.model.Tramite;
import com.cumpleanos.importramite.persistence.records.RevisionRequest;
import com.cumpleanos.importramite.persistence.repository.ContenedorRepository;
import com.cumpleanos.importramite.persistence.repository.ProductoRepository;
import com.cumpleanos.importramite.persistence.repository.TramiteRepository;
import com.cumpleanos.importramite.service.exception.DocumentNotFoundException;
import com.cumpleanos.importramite.service.interfaces.IRevisionService;
import com.cumpleanos.importramite.utils.MapUtils;
import com.cumpleanos.importramite.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.cumpleanos.importramite.utils.StringUtils.obtenerHora;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class RevisionServiceImpl extends GenericServiceImpl<Producto, String> implements IRevisionService {

    private final ContenedorRepository contenedorRepository;
    private final TramiteRepository tramiteRepository;
    private final ProductoRepository productoRepository;

    private static final String PRODUCTO_NOT_IN_LIST = "PRODUCTO NO ENCONTRADO EN LA LISTA";
    private static final String ADD = "AGREGADO";
    private static final String REMOVE = "ELIMINADO";
    private static final String NO_DATA = "SIN REGISTRO";

    @Override
    public CrudRepository<Producto, String> getRepository() {
        return productoRepository;
    }

    @Override
    public List<Producto> findByTramite_Id(String tramiteId) {
        return null;
    }

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
                if (contenedor.getId().equals(finalContenedorId)) {
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
        return productoRepository.findByTramiteIdAndContenedorIdOrderBySecuencia(tramiteId, finalContenedorId).orElseThrow(() -> new DocumentNotFoundException("No se encontraron productos para el trámite: "+tramiteId+" y el contenedor: "+contenedorId));
    }

    @Override
    public List<Producto> updateRevisionWithTramiteQuantities(String tramiteStr, String contenedorStr) {
        String tramiteId = tramiteStr.trim();
        String contenedorId = contenedorStr.trim();
        Tramite tramite = tramiteRepository.findById(tramiteId)
                .orElseThrow(() -> new DocumentNotFoundException("Tramite no encontrado"));

        List<Producto> productos = productoRepository.findByTramiteIdAndContenedorIdOrderBySecuencia(tramiteId, contenedorId).orElseThrow(() -> new DocumentNotFoundException("No se encontraron productos para el trámite: "+tramiteId+" y el contenedor: "+contenedorId));

        List<Contenedor> contenedores = contenedorRepository.findByTramiteId(tramiteId).orElseThrow(() -> new DocumentNotFoundException("Tramite " + tramiteId + " not found"));

        Map<String, Producto> tramiteProductMap = MapUtils.listByTramite(contenedores, productoRepository);

        for (Producto producto : tramiteProductMap.values()) {
            Revision revision = revisionMap.get(producto.getId());

            if (revision == null) {
                // Si no existe en revision, crear nuevo registro
                revision = new Revision();
                revision.setBarra(producto.getId());
                revision.setCantidad(0);
                revision.setCantidadPedida(producto.getBultos());
                revision.setCantidadDiferencia(producto.getBultos());
                revision.setEstado("NO LLEGO");
                revision.setSecuencia(producto.getSecuencia());
                revision.setTramite(tramite.getId());
            } else {
                int cantidadPedida = producto.getBultos();
                revision.setCantidadPedida(cantidadPedida);
                int diferencia = revision.getCantidad() - cantidadPedida;
                revision.setCantidadDiferencia(Math.abs(diferencia));
                revision.setSecuencia(producto.getSecuencia());
                if (diferencia == 0) {
                    revision.setEstado("COMPLETO");
                } else if (diferencia > 0) {
                    revision.setEstado("SOBRANTE");
                } else {
                    revision.setEstado("FALTANTE");
                }
            }
            repository.save(revision);
        }

        // Verificar revisiones que no esten en productos y actualizarlos
        for (Revision revision : revisions) {
            if (!tramiteProductMap.containsKey(revision.getBarra())) {
                revision.setEstado("SIN REGISTRO");
                revision.setSecuencia(0);
                repository.save(revision);
            }
        }
        tramite.setProceso((short) 3);
        tramiteRepository.save(tramite);
        return repository.findByTramiteOrderBySecuenciaAsc(tramiteId);
    }


    /**
     * Metodo para crear o actualizar datos de la tabal revision
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
                .orElseThrow(() -> new DocumentNotFoundException("Tramite no encontrado"));

        Contenedor contenedor = contenedorRepository.findById(contenedorId).orElseThrow(() -> new DocumentNotFoundException("Contenedor no encontrado"));

        if (contenedor.getStartDate() == null && contenedor.getStartHour() == null) {
            contenedor.setStartDate(LocalDate.now());
            contenedor.setStartHour(LocalTime.now());
            contenedorRepository.save(contenedor);
        }

        Producto revision = productoRepository.findByBarcodeAndTramiteIdAndContenedorId(barra, tramiteId, contenedorId).orElse(new Producto());

        if (revision.getBarcode() == null || revision.getBarcode().isEmpty()) {
            //Crear nueva revision
            revision.setBarcode(barra);
            revision.setContenedorId(contenedorId);
            revision.setTramiteId(tramiteId);
            revision.setNombre(PRODUCTO_NOT_IN_LIST);
            revision.setCantidadRevision(1);
            revision.setUsuarioRevision(request.usuario());
            revision.setEstadoRevision(NO_DATA);
            revision.setHistorialRevision(new ArrayList<>());
            revision.getHistorialRevision().add(historial(true));
        } else {
            if (request.status()) {
                revision.setCantidadRevision(revision.getCantidadRevision() + 1);
                revision.getHistorialRevision().add(historial(true));
                revision.setEstadoRevision(ADD);
            } else {
                int nuevaCantidad = revision.getCantidadRevision() - 1;
                if (nuevaCantidad >= 0) {
                    revision.setCantidadRevision(nuevaCantidad);
                    revision.getHistorialRevision().add(historial(false));
                    revision.setEstadoRevision(REMOVE);
                } else {
                    throw new DocumentNotFoundException("Cantidad no puede ser menor que cero");
                }
            }
        }
        return productoRepository.save(revision);
    }


    private static String historial(boolean status) {
        if (status) {
            return ADD + obtenerHora();
        }else {
            return REMOVE + obtenerHora();
        }
    }
}
