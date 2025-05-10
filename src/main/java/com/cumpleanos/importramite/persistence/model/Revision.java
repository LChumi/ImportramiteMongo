package com.cumpleanos.importramite.persistence.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Document(collection = "revision")
@CompoundIndexes({
        @CompoundIndex(name = "barra_tramite_idx", def = "{'barra':1, 'tramite._id':1}", unique = true)
})
public class Revision implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private String id;
    private String tramiteId; //Referencia del tramite
    private String contenedorId;//Referencia del contenedor
    private String barra;
    private Integer cantidad;

    @CreatedDate
    private LocalDate fecha;
    @CreatedDate
    private LocalTime hora;
    private String usuario;

    private String tramite;

    private Integer cantidadPedida;
    private Integer cantidadDiferencia;
    private String estado;
    private int secuencia;
}
