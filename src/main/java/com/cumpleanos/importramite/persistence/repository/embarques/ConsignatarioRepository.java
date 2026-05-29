package com.cumpleanos.importramite.persistence.repository.embarques;

import com.cumpleanos.importramite.persistence.model.embarques.Consignatario;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConsignatarioRepository extends MongoRepository<Consignatario, String> {
}
