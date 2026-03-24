package com.cumpleanos.importramite.service.interfaces.confiteria;

import com.cumpleanos.importramite.persistence.model.confiteria.ReposicionConfiteria;
import com.cumpleanos.importramite.service.interfaces.IGenericService;

import java.time.LocalDate;
import java.util.List;

public interface IReposicionConfiteriaService extends IGenericService<ReposicionConfiteria, String> {

    List<ReposicionConfiteria> findByFechaBetween(LocalDate fechaAfter, LocalDate fechaBefore);
}