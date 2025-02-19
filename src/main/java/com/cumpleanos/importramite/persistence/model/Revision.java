package com.cumpleanos.importramite.persistence.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@Document(collection = "revision")
@CompoundIndexes({
        @CompoundIndex(name = "barra_tramite_idx", def = "{'barra':1, 'tramite._id':1}",  unique = true)
})
public class Revision implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private String id;
    private String barra;
    private Long cantidad;

    @Setter(AccessLevel.NONE)
    @CreatedDate
    private LocalDate fecha;
    private String usuario;

    @DBRef
    private Tramite tramite;

    private Long cantidadPedida=0L;
    private Long cantidadDiferencia =0L;
    private String estado = "SN";
}
