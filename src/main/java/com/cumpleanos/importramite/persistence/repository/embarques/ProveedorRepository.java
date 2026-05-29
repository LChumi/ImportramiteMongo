package com.cumpleanos.importramite.persistence.repository.embarques;

import com.cumpleanos.importramite.persistence.model.embarques.Proveedor;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProveedorRepository extends MongoRepository<Proveedor, String> {
}
