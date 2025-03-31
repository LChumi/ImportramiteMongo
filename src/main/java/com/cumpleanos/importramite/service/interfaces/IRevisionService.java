package com.cumpleanos.importramite.service.interfaces;


import com.cumpleanos.importramite.persistence.model.Revision;
import com.cumpleanos.importramite.persistence.records.RevisionRequest;

import java.util.List;

public interface IRevisionService extends IGenericService<Revision, String> {
    List<Revision> findByTramite_Id(String tramiteId);

    List<Revision> validateAndProcessTramite(String tramiteId, String contenedorId);

    Revision updateCantidadByBarra(RevisionRequest request);

    List<Revision> updateRevisionWithTramiteQuantities(String tramiteId);
}
