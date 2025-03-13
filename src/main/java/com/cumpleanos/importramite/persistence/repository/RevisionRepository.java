package com.cumpleanos.importramite.persistence.repository;

import com.cumpleanos.importramite.persistence.model.Revision;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RevisionRepository extends MongoRepository<Revision, String> {
    List<Revision> findByTramite_IdOrderBySecuenciaAsc(String tramiteId);

    Revision findByBarraAndTramite_Id(String barra, String tramiteId);
}