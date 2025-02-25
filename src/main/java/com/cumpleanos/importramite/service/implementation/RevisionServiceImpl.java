package com.cumpleanos.importramite.service.implementation;

import com.cumpleanos.importramite.persistence.model.Contenedor;
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

import java.time.LocalDate;
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
        return repository.findByTramite_IdOrderBySecuenciaAsc(tramiteId);
    }

    /**
     * Metodo para validar los productos comparando con loa tabla Tramite
     * @param tramiteId el ID del tramite registrado
     * @return devuelve la lista de revision actualizada comparando las cantidades de tramite y de revision actualizando estados
     */
    @Override
    public List<Revision> updateRevisionWithTramiteQuantities(String tramiteId) {
        Tramite tramite = tramiteRepository.findById(tramiteId)
                .orElseThrow(() -> new DocumentNotFoundException("Tramite no encontrado"));
        List<Revision> revisions = repository.findByTramite_IdOrderBySecuenciaAsc(tramiteId);

        Map<String, Revision> revisionMap = revisions.stream()
                .collect(Collectors.toMap(Revision::getBarra, rev -> rev));

        // Mapa de productos en tramite con Id -> bultos
        Map<String, Producto> tramiteProductMap = tramite.getContenedor().stream()
                .flatMap(contenedor -> contenedor.getProductos().stream())
                .collect(Collectors.toMap(Producto::getId, prod -> prod));

        for (Contenedor contenedor : tramite.getContenedor()) {
            for (Producto producto : contenedor.getProductos()) {
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
                    revision.setTramite(tramite);
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
        }

        // Verificar revisiones que no esten en productos y actualizarlos
        for (Revision revision : revisions) {
            if (!tramiteProductMap.containsKey(revision.getBarra())){
                revision.setEstado("SIN REGISTRO");
                revision.setSecuencia(0);
                repository.save(revision);
            }
        }
        tramite.setEstado(true);
        tramiteRepository.save(tramite);
        return repository.findByTramite_IdOrderBySecuenciaAsc(tramiteId);
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
            revision.setFecha(LocalDate.now());
            revision.setBarra(barra);
            revision.setUsuario(usuario);
            revision.setCantidad(1);
            revision.setTramite(tramite);
        }else{
            revision.setCantidad(revision.getCantidad() + 1);
        }
        return repository.save(revision);
    }
}
