package com.cumpleanos.importramite.persistence.repository.embarques;

import com.cumpleanos.importramite.persistence.model.embarques.PuertoEmbarque;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PuertoEmbarqueRepository extends MongoRepository<PuertoEmbarque, String> {

    List<PuertoEmbarque> findAllByOrderByNombre();
}