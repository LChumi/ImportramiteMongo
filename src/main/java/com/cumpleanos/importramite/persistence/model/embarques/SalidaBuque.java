package com.cumpleanos.importramite.persistence.model.embarques;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "salidas_buque")
@CompoundIndex(
        name = "idx_puerto_fecha",
        def = "{'puertoEmbarqueNombre':1,'fechaDesde':1}"
)
public class SalidaBuque {
    @Id
    private String id;

    // RELACION CENTRAL
    private String procesoCotizacionId;

    private String puertoEmbarqueId;     // referencia a PuertoEmbarque
    private String puertoEmbarqueNombre; // "NINGBO" (desnormalizado)

    private LocalDate fechaDesde;
    private LocalDate fechaHasta;

    private Integer diasLibres;
    private String naviera;              // "INARPI", "OOCL"
    private String tipoServicio;         // "T. DIRECTO", "TRANSBORDO"

    // Todos los consignatarios con sus precios para esta ventana
    private List<CotizacionConsignatario> cotizaciones = new ArrayList<>();

    private Boolean activo = true;

    private LocalDateTime creadoEn;
    private LocalDateTime actualizadoEn;
}