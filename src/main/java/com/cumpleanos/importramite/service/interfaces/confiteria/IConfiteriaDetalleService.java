package com.cumpleanos.importramite.service.interfaces.confiteria;

import com.cumpleanos.importramite.persistence.model.confiteria.ConfiteriaDetalle;
import com.cumpleanos.importramite.persistence.model.confiteria.ReposicionConfiteria;
import com.cumpleanos.importramite.persistence.records.ReposicionRequest;
import com.cumpleanos.importramite.service.interfaces.IGenericService;

import java.time.LocalDate;
import java.util.List;

public interface IConfiteriaDetalleService extends IGenericService<ConfiteriaDetalle, String> {

    List<ConfiteriaDetalle> findByReposicionId(String reposisicionId);

    List<ConfiteriaDetalle> saveList(ReposicionRequest request);

    List<ReposicionConfiteria> findByFechaBetween(LocalDate fechaAfter, LocalDate fechaBefore);
}