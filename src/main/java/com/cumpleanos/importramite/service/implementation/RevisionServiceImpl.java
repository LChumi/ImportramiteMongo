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

import java.util.List;
import java.util.Map;
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

    @Override
    public List<Revision> updateRevisionWithTramiteQuantities(String tramiteId) {
        Tramite tramite = tramiteRepository.findById(tramiteId).orElseThrow(() -> new DocumentNotFoundException("Tramite not found"));
        List<Revision> revisions = repository.findByTramite_Id(tramiteId);

        //Logica actualizacion de cantidades pedidas
        Map<String, Long> tramiteProductMap = tramite.getListProductos().stream()
                .collect(Collectors.toMap(Producto::getId, Producto::getBultos));

        for (Revision revision : revisions) {
            Long catidadPedida = tramiteProductMap.get(revision.getBarra());
            if (catidadPedida.equals(revision.getCantidad())){
                revision.setEstado(true);
            }
            revision.setCantidadPedida(catidadPedida);
            repository.save(revision);
        }

        return revisions;
    }


}
