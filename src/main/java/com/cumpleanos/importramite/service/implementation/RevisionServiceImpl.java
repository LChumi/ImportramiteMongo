package com.cumpleanos.importramite.service.implementation;

import com.cumpleanos.importramite.persistence.model.Contenedor;
import com.cumpleanos.importramite.persistence.model.Producto;
import com.cumpleanos.importramite.persistence.model.Revision;
import com.cumpleanos.importramite.persistence.model.Tramite;
import com.cumpleanos.importramite.persistence.records.RevisionRequest;
import com.cumpleanos.importramite.persistence.repository.ContenedorRepository;
import com.cumpleanos.importramite.persistence.repository.ProductoRepository;
import com.cumpleanos.importramite.persistence.repository.RevisionRepository;
import com.cumpleanos.importramite.persistence.repository.TramiteRepository;
import com.cumpleanos.importramite.service.exception.DocumentNotFoundException;
import com.cumpleanos.importramite.service.exception.ExcelNotCreateException;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class RevisionServiceImpl extends GenericServiceImpl<Revision, String> implements IRevisionService {

    private final RevisionRepository repository;
    private final ContenedorRepository contenedorRepository;
    private final TramiteRepository tramiteRepository;
    private final ProductoRepository productoRepository;

    @Override
    public CrudRepository<Revision, String> getRepository() {
        return repository;
    }

    @Override
    public List<Revision> findByTramite_Id(String tramiteId) {
        return repository.findByTramiteOrderBySecuenciaAsc(tramiteId);
    }

    /**
     * Metodo para validar los productos comparando con la tabla Trámite
     * validando primero todos los contenedores disponibles
     *
     * @param tramiteId el ID del trámite registrado
     * @return devuelve la lista de revision actualizada comparando las cantidades de trámite y de revision actualizando estados
     */
    @Override
    public List<Revision> validateAndProcessTramite(String tramiteId, String contenedorId) {
        Tramite tramite = tramiteRepository.findById(tramiteId)
                .orElseThrow(() -> new DocumentNotFoundException("Tramite " + tramiteId + " not found"));

        //Verificar si todos los contenedores estan finalizados
        List<Contenedor> contenedores = contenedorRepository.findByTramiteId(tramiteId).orElseThrow(() -> new DocumentNotFoundException("Tramite " + tramiteId + " not found"));
        boolean allCompleted = contenedores.stream()
                .allMatch(Contenedor::getFinalizado);

        if (!allCompleted) {
            for (Contenedor contenedor : contenedores) {
                if (contenedor.getId().equals(contenedorId)) {
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
        return repository.findByTramiteOrderBySecuenciaAsc(tramiteId);
    }

    @Override
    public List<Revision> updateRevisionWithTramiteQuantities(String tramiteId) {

        Tramite tramite = tramiteRepository.findById(tramiteId)
                .orElseThrow(() -> new DocumentNotFoundException("Tramite no encontrado"));

        List<Revision> revisions = repository.findByTramiteOrderBySecuenciaAsc(tramiteId);

        Map<String, Revision> revisionMap = revisions.stream()
                .collect(Collectors.toMap(Revision::getBarra, rev -> rev));

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
    public Revision updateCantidadByBarra(RevisionRequest request) {

        String tramiteId = StringUtils.trimWhitespace(request.tramiteId());
        String barra = StringUtils.trimWhitespace(request.barra());

        Tramite tramite = tramiteRepository.findById(tramiteId)
                .orElseThrow(() -> new DocumentNotFoundException("Tramite no encontrado"));

        Contenedor contenedor = contenedorRepository.findById(request.contenedor()).orElseThrow(() -> new DocumentNotFoundException("Contenedor no encontrado"));


        Revision revision = repository.findByBarraAndTramite(barra, tramiteId);

        List<Producto> productos = productoRepository.findByTramiteIdAndContenedorId(tramite.getId(), request.contenedor()).orElseThrow(() -> new ExcelNotCreateException("No se encontraron productos para el trámite: "+tramite.getId()+" y el contenedor: "+contenedor));

        Map<String, Producto> tramiteProductMap = MapUtils.listByContainer(productos);

        if (revision == null) {
            //Crear nueva revision
            revision = new Revision();
            revision.setFecha(LocalDate.now());
            revision.setHora(LocalTime.now());
            revision.setBarra(barra);
            revision.setUsuario(request.usuario());
            revision.setCantidad(1);
            revision.setTramite(tramite.getId());
            verifyExist(tramiteProductMap, revision);
        } else {
            if (request.status()) {
                revision.setCantidad(revision.getCantidad() + 1);
                verifyExist(tramiteProductMap, revision);
            } else {
                int nuevaCantidad = revision.getCantidad() - 1;
                if (nuevaCantidad >= 0) {
                    revision.setCantidad(nuevaCantidad);
                    verifyExist(tramiteProductMap, revision);
                } else {
                    throw new DocumentNotFoundException("Cantidad no puede ser menor que cero");
                }
            }
        }
        return repository.save(revision);
    }

    private void verifyExist(Map<String, Producto> tramiteProductMap, Revision revision) {
        for (Producto producto : tramiteProductMap.values()) {
            if (producto.getId().equals(revision.getBarra())) {
                revision.setEstado("REGISTRADO");
                break;
            } else {
                revision.setEstado("SIN REGISTRO");
            }
        }
    }
}
