package com.cumpleanos.importramite.persistence.repository.embarques;

import com.cumpleanos.importramite.persistence.model.embarques.TramiteEmbarque;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TramiteEmbarqueRepository extends MongoRepository<TramiteEmbarque, String> {

    Optional<TramiteEmbarque> findByNumeroTramite(String numeroTramite);

    List<TramiteEmbarque> findAllByOrderByFechaEmbarqueDesc();
}
