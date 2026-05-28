package com.cumpleanos.importramite.persistence.model.embarques;

import lombok.Data;

import java.util.List;

@Data
public class CotizacionConsignatario {
    private String consignatarioId;
    private String nombreConsignatario;  // "ACE GROUP"

    // Cada tipo de contenedor con su destino y precios
    private List<OpcionFlete> opciones;
}
