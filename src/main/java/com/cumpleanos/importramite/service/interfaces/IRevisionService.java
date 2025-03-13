package com.cumpleanos.importramite.service.interfaces;


import com.cumpleanos.importramite.persistence.model.Revision;

import java.util.List;

public interface IRevisionService extends IGenericService<Revision, String> {
    List<Revision> findByTramite_Id(String tramiteId);

    List<Revision> validateAndProcessTramite(String tramiteId, String contenedorId);

    Revision updateCantidadByBarra(String tramiteId, String barra, String usuario, Boolean status);

    List<Revision> updateRevisionWithTramiteQuantities(String tramiteId);
}
