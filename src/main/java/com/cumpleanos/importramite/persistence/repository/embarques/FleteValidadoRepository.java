package com.cumpleanos.importramite.persistence.repository.embarques;

import com.cumpleanos.importramite.persistence.model.embarques.FleteValidado;
import com.cumpleanos.importramite.utils.enums.EstadoFlete;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FleteValidadoRepository extends MongoRepository<FleteValidado, String> {

    List<FleteValidado> findBySalidaBuqueId(String salidaBuqueId);

    List<FleteValidado> findByEstado(EstadoFlete estado);
}
