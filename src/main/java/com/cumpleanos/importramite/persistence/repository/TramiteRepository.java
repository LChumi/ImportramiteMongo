package com.cumpleanos.importramite.persistence.repository;

import com.cumpleanos.importramite.persistence.model.Tramite;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TramiteRepository extends MongoRepository<Tramite, String> {

    List<Tramite> findByProceso(Short proceso);

    List<Tramite> findByFechaArriboBetween(LocalDate inicio, LocalDate fin);
}
