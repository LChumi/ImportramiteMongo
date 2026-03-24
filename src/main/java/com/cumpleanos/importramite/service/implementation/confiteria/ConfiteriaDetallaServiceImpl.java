package com.cumpleanos.importramite.service.implementation.confiteria;

import com.cumpleanos.importramite.persistence.model.confiteria.ConfiteriaDetalle;
import com.cumpleanos.importramite.persistence.repository.confiteria.ConfiteriaDetalleRepository;
import com.cumpleanos.importramite.service.implementation.GenericServiceImpl;
import com.cumpleanos.importramite.service.interfaces.confiteria.IConfiteriaDetalleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConfiteriaDetallaServiceImpl extends GenericServiceImpl<ConfiteriaDetalle, String> implements IConfiteriaDetalleService {

    private final ConfiteriaDetalleRepository repository;


    @Override
    public CrudRepository<ConfiteriaDetalle, String> getRepository() {
        return repository;
    }

    @Override
    public Optional<List<ConfiteriaDetalle>> findByReposicionId(String reposisicionId) {
        return repository.findByReposicionId(reposisicionId);
    }
}