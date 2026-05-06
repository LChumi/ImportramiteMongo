package com.cumpleanos.importramite.persistence.repository;

import com.cumpleanos.importramite.persistence.model.Menu;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Collection;
import java.util.List;

public interface MenuRepository extends MongoRepository<Menu, String> {

    @Query("{ 'rolesPermitidos': { $in: ?0 } }")
    List<Menu> findByRolesPermitidos(List<String> roles);
}
