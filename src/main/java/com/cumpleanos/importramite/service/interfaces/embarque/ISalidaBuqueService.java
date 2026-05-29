package com.cumpleanos.importramite.service.interfaces.embarque;

import com.cumpleanos.importramite.persistence.model.embarques.OpcionMasBarataResponse;
import com.cumpleanos.importramite.persistence.model.embarques.SalidaBuque;
import com.cumpleanos.importramite.service.interfaces.IGenericService;

public interface ISalidaBuqueService extends IGenericService<SalidaBuque, String> {

    OpcionMasBarataResponse obtenerMejorOpcion(String procesoCotizacionId);
}
