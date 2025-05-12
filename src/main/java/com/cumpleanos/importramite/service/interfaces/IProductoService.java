package com.cumpleanos.importramite.service.interfaces;

import com.cumpleanos.importramite.persistence.model.Producto;

import java.util.List;

public interface IProductoService extends IGenericService<Producto, String> {

    List<Producto> findByTramiteIdAndContenedorId(String tramiteId, String contenedorId);

    Producto findByBarcodeAndTramiteIdAndContenedorId(String barcode, String tramiteId, String contenedorId);
}
