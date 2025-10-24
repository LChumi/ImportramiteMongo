package com.cumpleanos.importramite.service.interfaces;

import com.cumpleanos.importramite.persistence.model.Contenedor;
import com.cumpleanos.importramite.persistence.model.Producto;
import com.cumpleanos.importramite.persistence.model.Tramite;
import com.cumpleanos.importramite.persistence.records.StatusResponse;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ITramiteService extends IGenericService<Tramite, String> {
    List<Tramite> findByProceso(Short proceso);

    List<Producto> listByTramite(String tramite);

    StatusResponse findTramiteBloqueaContenedor(String tramite, String contenedor, String usr);

    List<Tramite> buscarTramites(String id, Short estado, LocalDate fechaInicio, LocalDate fechaFin);

    StatusResponse updateDateTramite(String id, LocalDate fechaLlegada, LocalTime horaLlegada);

    Contenedor findByTramiteAndId(String tramite, String id);

    List<Producto> findByTramiteAndContenedor(String tramite, String contenedor);

    List<Tramite> getTramitesOfTheWeek();

    List<Tramite> getTramitesOfTheMonth();

    Integer getTotal(String tramite, String contenedor);

    Double getPercentage(String tramite, String contenedor);
}
