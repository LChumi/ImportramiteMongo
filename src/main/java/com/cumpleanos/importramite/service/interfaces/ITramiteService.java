package com.cumpleanos.importramite.service.interfaces;

import com.cumpleanos.importramite.persistence.model.Producto;
import com.cumpleanos.importramite.persistence.model.Tramite;

import java.util.List;

public interface ITramiteService extends IGenericService<Tramite, String> {
    List<Tramite> findByEstadoFalse();
    List<Producto> listByTramite(String tramite);
    Tramite findTramiteBloqueaContenedor(String tramite, String contenedor, String usr);
}
