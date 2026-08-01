package com.cumpleanos.importramite.service.interfaces.dispositivos;

import com.cumpleanos.importramite.persistence.model.dispositivosinv.EstadoDispositivo;
import com.cumpleanos.importramite.persistence.model.dispositivosinv.Prestamo;
import com.cumpleanos.importramite.service.interfaces.IGenericService;

import java.util.List;
import java.util.Optional;

public interface IPrestamoService extends IGenericService<Prestamo, String> {

    Prestamo entregar(Prestamo prestamo); // fechaEsperadaDevolucion puede venir null

    Prestamo devolver(String prestamoId, EstadoDispositivo estadoFinalDispositivo, String observaciones, String username);

    Optional<Prestamo> ocupadoPor(String dispositivoId); // quién lo tiene ahora, si aplica

    List<Prestamo> historialPorSerial(String serial);

    List<Prestamo> prestamosActivos();
}