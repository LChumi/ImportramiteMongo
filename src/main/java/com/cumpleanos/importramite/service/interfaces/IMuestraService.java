package com.cumpleanos.importramite.service.interfaces;

import com.cumpleanos.importramite.persistence.model.Producto;
import com.cumpleanos.importramite.persistence.records.MuestraRequest;

import java.util.List;

public interface IMuestraService {

    Producto saveAndCompare(MuestraRequest request);

    List<Producto> updateWithRevision(String tramite, String contenedor);
}