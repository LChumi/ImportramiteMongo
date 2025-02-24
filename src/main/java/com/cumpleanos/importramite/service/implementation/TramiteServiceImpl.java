package com.cumpleanos.importramite.service.implementation;

import com.cumpleanos.importramite.persistence.model.Tramite;
import com.cumpleanos.importramite.persistence.repository.TramiteRepository;
import com.cumpleanos.importramite.service.interfaces.ITramiteService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ =  {@Autowired})
public class TramiteServiceImpl extends GenericServiceImpl<Tramite, String> implements ITramiteService {

    private final TramiteRepository repository;

    @Override
    public CrudRepository<Tramite, String> getRepository() {
        return repository;
    }

    @Override
    public List<Tramite> findByEstadoFalse() {
        return repository.findByEstadoFalse();
    }
}
