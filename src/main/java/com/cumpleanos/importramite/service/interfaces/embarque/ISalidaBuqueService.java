package com.cumpleanos.importramite.service.interfaces.embarque;

import com.cumpleanos.importramite.persistence.model.embarques.CotizacionConsignatario;
import com.cumpleanos.importramite.persistence.model.embarques.OpcionFlete;
import com.cumpleanos.importramite.persistence.model.embarques.OpcionMasBarataResponse;
import com.cumpleanos.importramite.persistence.model.embarques.SalidaBuque;
import com.cumpleanos.importramite.service.interfaces.IGenericService;

import java.util.List;

public interface ISalidaBuqueService extends IGenericService<SalidaBuque, String> {

    OpcionMasBarataResponse obtenerMejorOpcion(String procesoCotizacionId);

    List<SalidaBuque> getByProcesoCotizacionId(String procesoCotizacionId);

    SalidaBuque update(String id, SalidaBuque salidaBuque);

    SalidaBuque agregarCotizacion(String idBuque, CotizacionConsignatario cotizacion);

    SalidaBuque agregarOpcion(String idBuque, String cotizacionId, OpcionFlete opcion);

    SalidaBuque actualizarOpcion(String idBuque, String cotizacionId, String opcionId, OpcionFlete opcionActualizada);

    SalidaBuque eliminarOpcion(String idBuque, String cotizacionId, String opcionId);

    SalidaBuque eliminarCotizacion(String idBuque, String cotizacionId);
}