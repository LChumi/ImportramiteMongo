package com.cumpleanos.importramite.service.interfaces;


import com.cumpleanos.importramite.persistence.model.Contenedor;
import com.cumpleanos.importramite.persistence.model.Producto;
import com.cumpleanos.importramite.persistence.records.RevisionRequest;
import com.cumpleanos.importramite.persistence.records.StatusResponse;

import java.util.List;

public interface IRevisionService {

    Producto updateCantidadByBarra(RevisionRequest request);

    StatusResponse processTramiteCompletion(String tramiteId, String contenedorId);

    List<Producto> processProductRevision(String tramiteId, String contenedorId);

    List<Contenedor> listContenedoresByTramite(String tramiteId);

    List<Producto> findByTramiteId(String tramiteId, String contenedorId);

    StatusResponse getProducto(String tramite, String contenedor, String barcode);
}
