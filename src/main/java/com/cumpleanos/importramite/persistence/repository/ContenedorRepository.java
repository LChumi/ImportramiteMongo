package com.cumpleanos.importramite.persistence.repository;

import com.cumpleanos.importramite.persistence.model.Contenedor;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ContenedorRepository extends MongoRepository<Contenedor, String> {

    Optional<List<Contenedor>> findByTramiteId(String tramiteId);
}
