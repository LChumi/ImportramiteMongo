package com.cumpleanos.importramite.service.interfaces;

import com.cumpleanos.importramite.persistence.model.Muestra;

import java.util.List;

public interface IMuestraService extends IGenericService<Muestra,String> {
    List<Muestra> findByRevision_Tramite_Id(String tramiteId);
    Muestra saveAndCompare(String barra, String muestra, String tramite);
    List<Muestra> updateWithRevision(String tramite);
}