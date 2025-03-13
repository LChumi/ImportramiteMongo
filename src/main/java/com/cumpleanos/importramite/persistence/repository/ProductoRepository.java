package com.cumpleanos.importramite.persistence.repository;

import com.cumpleanos.importramite.persistence.model.Producto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends MongoRepository<Producto, String> {
}
