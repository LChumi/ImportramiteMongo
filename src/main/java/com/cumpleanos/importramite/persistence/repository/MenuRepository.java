package com.cumpleanos.importramite.persistence.repository;

import com.cumpleanos.importramite.persistence.model.Menu;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MenuRepository extends MongoRepository<Menu, String> {

    List<Menu> findByRolesPermitidos(List<String> rolesPermitidos);
}
