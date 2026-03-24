package com.cumpleanos.importramite.service.implementation.confiteria;

import com.cumpleanos.importramite.persistence.model.confiteria.ReposicionConfiteria;
import com.cumpleanos.importramite.persistence.repository.confiteria.ReposicionConfiteriaRepository;
import com.cumpleanos.importramite.service.implementation.GenericServiceImpl;
import com.cumpleanos.importramite.service.interfaces.confiteria.IReposicionConfiteriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReposicionConfiteriaServiceImpl extends GenericServiceImpl<ReposicionConfiteria, String> implements IReposicionConfiteriaService {

    private final ReposicionConfiteriaRepository repository;

    @Override
    public CrudRepository<ReposicionConfiteria, String> getRepository() {
        return repository;
    }

    @Override
    public Optional<List<ReposicionConfiteria>> findByFechaBetween(LocalDate fechaAfter, LocalDate fechaBefore) {
        return repository.findByFechaBetween(fechaAfter, fechaBefore);
    }
}
