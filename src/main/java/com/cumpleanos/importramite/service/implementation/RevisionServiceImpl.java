package com.cumpleanos.importramite.service.implementation;

import com.cumpleanos.importramite.persistence.model.Producto;
import com.cumpleanos.importramite.persistence.model.Revision;
import com.cumpleanos.importramite.persistence.model.Tramite;
import com.cumpleanos.importramite.persistence.repository.RevisionRepository;
import com.cumpleanos.importramite.persistence.repository.TramiteRepository;
import com.cumpleanos.importramite.service.exception.DocumentNotFoundException;
import com.cumpleanos.importramite.service.interfaces.IRevisionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ =  {@Autowired})
public class RevisionServiceImpl extends GenericServiceImpl<Revision, String> implements IRevisionService {

    private final RevisionRepository repository;
    private final TramiteRepository tramiteRepository;

    @Override
    public CrudRepository<Revision, String> getRepository() {
        return repository;
    }

    @Override
    public List<Revision> findByTramite_Id(String tramiteId) {
        return repository.findByTramite_Id(tramiteId);
    }

    /**
     * Metodo para validar los productos comparando con loa tabla Tramite
     * @param tramiteId el ID del tramite registrado
     * @return devuelve la lista de revision actualizada comparando las cantidades de tramite y de revision actualizando estados
     */
    @Override
    public List<Revision> updateRevisionWithTramiteQuantities(String tramiteId) {
        Tramite tramite = tramiteRepository.findById(tramiteId).orElseThrow(() -> new DocumentNotFoundException("Tramite no encontrado"));
        List<Revision> revisions = repository.findByTramite_Id(tramiteId);

        //Logica actualizacion de cantidades pedidas
        Map<String, Long> tramiteProductMap = tramite.getListProductos().stream()
                .collect(Collectors.toMap(Producto::getId, Producto::getBultos));

        for (Revision revision : revisions) {
            Long cantidadPedida = tramiteProductMap.get(revision.getBarra());

            if (cantidadPedida != null) {
                if (cantidadPedida.equals(revision.getCantidad())) {
                    revision.setEstado("COMPLETO");
                }
                revision.setCantidadPedida(cantidadPedida);
                revision.setCantidadDiferencia(Math.abs(revision.getCantidad() - cantidadPedida));
                revision.setEstado("DIFERENCIAS");
            } else {
                revision.setCantidadPedida(0L);
                revision.setCantidadDiferencia(revision.getCantidad());
                revision.setEstado("NOVEDAD");
            }
            repository.save(revision);
        }
        return revisions;
    }

    /**
     * Metodo para crear o actualizar datos de la tabal revision
     * @param tramiteId el id de tramite
     * @param barra la barra del producto
     * @param usuario el usurario q registra al producto
     * @return nueva revision si no existe si existe va sumando la cantidad ++
     */
    @Transactional
    @Override
    public Revision updateCantidadByBarra(String tramiteId, String barra, String usuario) {
        Tramite tramite = tramiteRepository.findById(tramiteId)
                .orElseThrow(() -> new DocumentNotFoundException("Tramite no encontrado"));

        Revision revision = repository.findByBarraAndTramite_Id(barra, tramiteId);

        if (revision == null) {
            //Crear nueva revision
            revision = new Revision();
            revision.setId(UUID.randomUUID().toString());
            revision.setBarra(barra);
            revision.setUsuario(usuario);
            revision.setCantidad(1L);
            revision.setTramite(tramite);
        }else{
            revision.setCantidad(revision.getCantidad() + 1);
        }
        return repository.save(revision);
    }
}
