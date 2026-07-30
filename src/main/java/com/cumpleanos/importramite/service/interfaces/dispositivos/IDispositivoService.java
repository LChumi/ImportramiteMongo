package com.cumpleanos.importramite.service.interfaces.dispositivos;

import com.cumpleanos.importramite.persistence.model.dispositivosinv.Dispositivo;
import com.cumpleanos.importramite.persistence.model.dispositivosinv.EstadoDispositivo;
import com.cumpleanos.importramite.service.interfaces.IGenericService;

import java.util.List;

public interface IDispositivoService extends IGenericService<Dispositivo, String> {

    Dispositivo registrar(Dispositivo dispositivo); // alta manual, valida serial único

    List<Dispositivo> disponibles();

    List<Dispositivo> disponiblesPorMarca(String marca);

    Dispositivo cambiarEstado(String dispositivoId, EstadoDispositivo nuevoEstado);
}