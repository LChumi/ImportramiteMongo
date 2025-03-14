package com.cumpleanos.importramite.persistence.repository;

import com.cumpleanos.importramite.persistence.model.Muestra;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MuestraRepository extends MongoRepository<Muestra, String> {
    List<Muestra> findByRevision_Tramite_Id(String tramiteId);

    Muestra findByBarraBultoAndRevision_Tramite_Id(String barraBulto, String tramiteId);
}
