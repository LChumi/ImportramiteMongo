package com.cumpleanos.importramite.persistence.repository.confiteria;

import com.cumpleanos.importramite.persistence.model.confiteria.ReposicionConfiteria;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReposicionConfiteriaRepository extends MongoRepository<ReposicionConfiteria, String> {

    List<ReposicionConfiteria> findByFechaBetween(LocalDate fechaAfter, LocalDate fechaBefore);

}