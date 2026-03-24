package com.cumpleanos.importramite.persistence.repository.confiteria;

import com.cumpleanos.importramite.persistence.model.confiteria.ConfiteriaDetalle;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ConfiteriaDetalleRepository extends MongoRepository<ConfiteriaDetalle, String> {

    Optional<List<ConfiteriaDetalle>> findByReposicionId(String reposicionId);
}