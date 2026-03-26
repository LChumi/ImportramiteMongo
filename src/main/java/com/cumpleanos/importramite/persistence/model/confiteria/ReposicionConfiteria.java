package com.cumpleanos.importramite.persistence.model.confiteria;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Builder
@Document(collection = "Reposicion_confiteria")
public class ReposicionConfiteria {

    @Id
    @Setter(AccessLevel.NONE)
    private String id;

    private LocalDate fecha;
    private String usuarioSolicitante;
    private String proveedor;
    private Boolean estado;
}