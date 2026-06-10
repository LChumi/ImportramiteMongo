package com.cumpleanos.importramite.service.interfaces.embarque;

import com.cumpleanos.importramite.persistence.model.embarques.ProcesoCotizacion;
import com.cumpleanos.importramite.service.interfaces.IGenericService;

public interface IProcesoCotizacionService extends IGenericService<ProcesoCotizacion, String> {
    ProcesoCotizacion update(String id, ProcesoCotizacion procesoCotizacion);
}