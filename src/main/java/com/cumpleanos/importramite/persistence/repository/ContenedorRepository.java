package com.cumpleanos.importramite.persistence.repository;

import com.cumpleanos.importramite.persistence.model.Contenedor;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ContenedorRepository extends MongoRepository<Contenedor, String> {
}
