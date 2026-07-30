package com.cumpleanos.importramite.persistence.repository.dispositivos;

import com.cumpleanos.importramite.persistence.model.dispositivosinv.EstadoPrestamo;
import com.cumpleanos.importramite.persistence.model.dispositivosinv.Prestamo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PrestamoRepository extends MongoRepository<Prestamo, String> {

    // El préstamo "abierto" de un dispositivo es el que no tiene fechaDevolucion
    Optional<Prestamo> findByDispositivoIdAndFechaDevolucionIsNull(String dispositivoId);

    List<Prestamo> findBySerialDispositivoOrderByFechaEntregaDesc(String serialDispositivo);

    List<Prestamo> findByEstado(EstadoPrestamo estado);

    List<Prestamo> findByResponsableAndEstado(String responsable, EstadoPrestamo estado);
}