package com.cumpleanos.importramite.persistence.repository.dispositivos;

import com.cumpleanos.importramite.persistence.model.dispositivosinv.Dispositivo;
import com.cumpleanos.importramite.persistence.model.dispositivosinv.EstadoDispositivo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface DispositivoRepository extends MongoRepository<Dispositivo, String> {

    Optional<Dispositivo> findBySerial(String serial);

    boolean existsBySerial(String serial);

    List<Dispositivo> findByEstadoActualAndActivoTrue(EstadoDispositivo estado);

    List<Dispositivo> findByMarcaAndEstadoActualAndActivoTrue(String marca, EstadoDispositivo estado);

    List<Dispositivo> findByActivoTrue();
}