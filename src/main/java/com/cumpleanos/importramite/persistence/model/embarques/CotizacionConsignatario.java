package com.cumpleanos.importramite.persistence.model.embarques;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class CotizacionConsignatario {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String id = UUID.randomUUID().toString();

    private String consignatarioId;
    private String nombreConsignatario;  // "ACE GROUP"

    // Cada tipo de contenedor con su destino y precios
    private List<OpcionFlete> opciones = new ArrayList<>();
}
