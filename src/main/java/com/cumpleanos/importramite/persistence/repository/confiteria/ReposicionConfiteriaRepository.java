package com.cumpleanos.importramite.persistence.repository.confiteria;

import com.cumpleanos.importramite.persistence.model.confiteria.ReposicionConfiteria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ReposicionConfiteriaRepository extends MongoRepository<ReposicionConfiteria, String> {

    List<ReposicionConfiteria> findByFechaBetween(LocalDate fechaAfter, LocalDate fechaBefore);

    @Query("{ 'fecha': { $gte: ?0, $lte: ?1 } }")
    List<ReposicionConfiteria> findByFechaRange(LocalDate fechaInicio, LocalDate fechaFin);

}