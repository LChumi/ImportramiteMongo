package com.cumpleanos.importramite.service.interfaces.embarque;

import com.cumpleanos.importramite.persistence.model.embarques.OpcionMasBarataResponse;
import com.cumpleanos.importramite.persistence.model.embarques.SalidaBuque;
import com.cumpleanos.importramite.service.interfaces.IGenericService;

import java.util.List;

public interface ISalidaBuqueService extends IGenericService<SalidaBuque, String> {

    OpcionMasBarataResponse obtenerMejorOpcion(String procesoCotizacionId);

    List<SalidaBuque> getByProcesoCotizacionId(String procesoCotizacionId);

    SalidaBuque update(String id, SalidaBuque salidaBuque);
}
