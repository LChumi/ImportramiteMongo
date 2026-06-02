package com.cumpleanos.importramite.persistence.repository.embarques;

import com.cumpleanos.importramite.persistence.model.embarques.Consignatario;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ConsignatarioRepository extends MongoRepository<Consignatario, String> {

    List<Consignatario> findAllByOrderByNombre();
}