package com.cumpleanos.importramite.persistence.repository.embarques;

import com.cumpleanos.importramite.persistence.model.embarques.SalidaBuque;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SalidaBuqueRepository extends MongoRepository<SalidaBuque, String>{

    List<SalidaBuque> findByProcesoCotizacionId(String procesoCotizacionId);
}
