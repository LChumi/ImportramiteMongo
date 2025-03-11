package com.cumpleanos.importramite.persistence.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@Document(collection = "producto")
public class Producto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private String id;
    private String id1;
    private String nombre;
    private Integer cxb;
    private Integer bultos;
    private Integer total;
    private String itemAlterno;
    private Double pvp;
    private Integer cxbAnterior;
    private String ubicacionBulto;
    private Integer stockReal;
    private String descripcion;
    private String barraSistema;
    private Integer diferencia =0;
    private int secuencia;
}
