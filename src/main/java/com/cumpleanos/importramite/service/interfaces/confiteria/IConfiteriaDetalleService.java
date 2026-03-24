package com.cumpleanos.importramite.service.interfaces.confiteria;

import com.cumpleanos.importramite.persistence.model.confiteria.ConfiteriaDetalle;
import com.cumpleanos.importramite.service.interfaces.IGenericService;

import java.util.List;
import java.util.Optional;

public interface IConfiteriaDetalleService extends IGenericService<ConfiteriaDetalle, String> {

    Optional<List<ConfiteriaDetalle>> findByReposicionId(String reposisicionId);
}
