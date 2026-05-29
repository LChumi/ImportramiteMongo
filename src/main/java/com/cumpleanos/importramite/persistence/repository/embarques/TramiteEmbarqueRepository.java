package com.cumpleanos.importramite.persistence.repository.embarques;

import com.cumpleanos.importramite.persistence.model.embarques.TramiteEmbarque;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TramiteEmbarqueRepository extends MongoRepository<TramiteEmbarque, String> {

    Optional<TramiteEmbarque> findByNumeroBl(String numeroBl);
}
