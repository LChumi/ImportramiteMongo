package com.cumpleanos.importramite.service.implementation;

import com.cumpleanos.importramite.persistence.model.Muestra;
import com.cumpleanos.importramite.persistence.model.Revision;
import com.cumpleanos.importramite.persistence.repository.MuestraRepository;
import com.cumpleanos.importramite.persistence.repository.RevisionRepository;
import com.cumpleanos.importramite.service.exception.DocumentNotFoundException;
import com.cumpleanos.importramite.service.interfaces.IMuestraService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ =  {@Autowired})
public class MuestraServiceImpl extends  GenericServiceImpl<Muestra, String> implements IMuestraService {

    private final MuestraRepository repository;
    private final RevisionRepository revisionRepository;

    @Override
    public CrudRepository<Muestra, String> getRepository() {
        return repository;
    }

    @Override
    public List<Muestra> findByRevision_Tramite_Id(String tramiteId) {
        return repository.findByRevision_Tramite_Id(tramiteId);
    }

    @Override
    public Muestra saveAndCompare(String barra, String muestra, String tramite) {
        Revision rev = revisionRepository.findByBarraAndTramite_Id(barra, tramite);
        if(rev == null) {
            throw new DocumentNotFoundException("Muestra no encontrada ");
        }
        Muestra mr = repository.findById(barra).orElse(null);
        if (mr == null) {
            mr = new Muestra();
            mr.setId(barra);
            mr.setBarraMuestra(muestra);
            mr.setRevision(rev);
            mr.setStatus(validateMuestra(mr));
            mr.setCantidad(1);
        } else {
            mr.setCantidad(mr.getCantidad()+1);
            mr.setStatus(validateMuestra(mr));
        }
        repository.save(mr);
        return mr;
    }

    private static boolean validateMuestra(Muestra muestra){
        return muestra.getId().equals(muestra.getBarraMuestra());
    }
}
