package com.cumpleanos.importramite.persistence.repository.embarques;

import com.cumpleanos.importramite.persistence.model.embarques.PuertoEmbarque;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PuertoEmbarqueRepository extends MongoRepository<PuertoEmbarque, String> {
}
