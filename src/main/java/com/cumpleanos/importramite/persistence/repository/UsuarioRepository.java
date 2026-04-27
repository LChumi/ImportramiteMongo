package com.cumpleanos.importramite.persistence.repository;

import com.cumpleanos.importramite.persistence.model.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UsuarioRepository extends MongoRepository<Usuario, String> {

    Optional<Usuario> findByIdUsuario(String idUsuario);
}