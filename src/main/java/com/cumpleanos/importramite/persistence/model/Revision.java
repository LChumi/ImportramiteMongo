package com.cumpleanos.importramite.persistence.model;

import lombok.Data;
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
    private Integer cantidad;

    @CreatedDate
    private LocalDate fecha;
    private String usuario;

    @DBRef
    private Tramite tramite;

    private Integer cantidadPedida=0;
    private Integer cantidadDiferencia =0;
    private String estado = "SN";
    private int secuencia;
}
