package com.cumpleanos.importramite.persistence.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document("usuario_bod")
public class Usuario {

    @Id
    private String id;

    private String idUsuario;
    private String nombre;

    private List<String> roles;
}