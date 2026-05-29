package com.cumpleanos.importramite.persistence.repository.embarques;

import com.cumpleanos.importramite.persistence.model.embarques.ProcesoCotizacion;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ProcesoCotizacionRepository extends MongoRepository<ProcesoCotizacion, String> {

    Optional<ProcesoCotizacion> findByNumeroReferencia(String numeroReferencia);
}
