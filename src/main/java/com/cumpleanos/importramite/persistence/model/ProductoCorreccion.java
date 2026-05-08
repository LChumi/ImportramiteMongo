package com.cumpleanos.importramite.persistence.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ProductoCorreccion {
    private String usuario;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate fecha;
    private String detalle;
}