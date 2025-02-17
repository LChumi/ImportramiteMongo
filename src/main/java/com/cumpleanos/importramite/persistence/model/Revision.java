package com.cumpleanos.importramite.persistence.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@Builder
@Document(collection = "REVISION")
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
    private Tramite tramite;
}
