package com.cumpleanos.importramite.service.implementation.embarque;

import com.cumpleanos.importramite.persistence.model.embarques.PuertoEmbarque;
import com.cumpleanos.importramite.persistence.repository.embarques.PuertoEmbarqueRepository;
import com.cumpleanos.importramite.service.implementation.GenericServiceImpl;
import com.cumpleanos.importramite.service.interfaces.embarque.IPuertoEmbarqueService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PuertoEmbarqueServiceImpl extends GenericServiceImpl<PuertoEmbarque, String> implements IPuertoEmbarqueService {

    private final PuertoEmbarqueRepository repository;

    @Override
    public CrudRepository<PuertoEmbarque, String> getRepository() {
        return repository;
    }

    @Override
    public List<PuertoEmbarque> findAll() {
        return repository.findAllByOrderByNombre();
    }
}
