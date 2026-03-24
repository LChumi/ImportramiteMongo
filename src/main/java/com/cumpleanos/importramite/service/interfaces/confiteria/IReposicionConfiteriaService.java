package com.cumpleanos.importramite.service.interfaces.confiteria;

import com.cumpleanos.importramite.persistence.model.confiteria.ReposicionConfiteria;
import com.cumpleanos.importramite.service.interfaces.IGenericService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IReposicionConfiteriaService extends IGenericService<ReposicionConfiteria, String> {

    Optional<List<ReposicionConfiteria>> findByFechaBetween(LocalDate fechaAfter, LocalDate fechaBefore);
}