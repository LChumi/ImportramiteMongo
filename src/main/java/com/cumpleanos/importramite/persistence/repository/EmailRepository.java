package com.cumpleanos.importramite.persistence.repository;

import com.cumpleanos.importramite.persistence.model.Emails;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface EmailRepository extends MongoRepository<Emails, String> {
    Optional<Emails> findByTipo(Long tipo);
}
