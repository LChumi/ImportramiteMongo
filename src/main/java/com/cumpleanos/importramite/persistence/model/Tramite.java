package com.cumpleanos.importramite.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@Document(collection = "tramite")
public class Tramite implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @CreatedDate
    private LocalDate fechaCarga;
    private String observacion;
    @JsonIgnore
    private List<Producto> listProductos;
}
