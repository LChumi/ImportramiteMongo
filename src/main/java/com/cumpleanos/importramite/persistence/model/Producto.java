package com.cumpleanos.importramite.persistence.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Document(collection = "producto")
public class Producto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private String id;
    private String id1;
    private String nombre;
    private Long cxb;
    private Long bultos;
    private BigDecimal cbm;
    private BigDecimal total;
}
