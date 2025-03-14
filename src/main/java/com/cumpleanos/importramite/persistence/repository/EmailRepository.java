package com.cumpleanos.importramite.persistence.repository;

import com.cumpleanos.importramite.persistence.model.Emails;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EmailRepository extends MongoRepository<Emails, String> {
    Emails findByTipo(Long tipo);
}
