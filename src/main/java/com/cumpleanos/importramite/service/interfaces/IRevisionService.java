package com.cumpleanos.importramite.service.interfaces;


import com.cumpleanos.importramite.persistence.model.Producto;
import com.cumpleanos.importramite.persistence.records.RevisionRequest;

import java.util.List;

public interface IRevisionService {

    List<Producto> validateAndProcessTramite(String tramiteId, String contenedorId);

    Producto updateCantidadByBarra(RevisionRequest request);

    List<Producto> updateRevisionWithTramiteQuantities(String tramiteId, String contenedorId);
}
