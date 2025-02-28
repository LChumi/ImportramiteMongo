package com.cumpleanos.importramite.service.interfaces;

import com.cumpleanos.importramite.persistence.model.Producto;
import com.cumpleanos.importramite.persistence.model.Tramite;
import com.cumpleanos.importramite.persistence.records.StatusResponse;

import java.util.List;

public interface ITramiteService extends IGenericService<Tramite, String> {
    List<Tramite> findByEstadoFalse();
    List<Tramite> findByEstadoTrue();
    List<Producto> listByTramite(String tramite);
    StatusResponse findTramiteBloqueaContenedor(String tramite, String contenedor, String usr);
}
