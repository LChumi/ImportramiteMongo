package com.cumpleanos.importramite.service.interfaces;

import com.cumpleanos.importramite.persistence.model.ProductoObservacion;
import com.cumpleanos.importramite.persistence.records.CorreccionRequest;

import java.util.List;

public interface IProductoObservacionService extends IGenericService<ProductoObservacion, String> {

    ProductoObservacion saveObservation(ProductoObservacion productoObservacion);

    List<ProductoObservacion> findByBodega(Long idBodega);

    ProductoObservacion addCorrection(CorreccionRequest request);
}