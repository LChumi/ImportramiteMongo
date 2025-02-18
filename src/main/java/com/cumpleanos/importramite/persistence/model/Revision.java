package com.cumpleanos.importramite.persistence.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@Document(collection = "revision")
public class Revision implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private String id;
    private String barra;
    private Long cantidad;
    private LocalDate fecha;
    private String usuario;
    private Long usr_id;

    @DBRef
    private Tramite tramite;

    private Long cantidadPedida;
    private Long cantidadDiferencia;
    private Boolean estado = false;
}
