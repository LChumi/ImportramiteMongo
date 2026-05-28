package com.cumpleanos.importramite.persistence.model.embarques;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Data
@Document(collection = "salidas_buque")
public class SalidaBuque {
    @Id
    private String id;

    private String puertoEmbarqueId;     // referencia a PuertoEmbarque
    private String puertoEmbarqueNombre; // "NINGBO" (desnormalizado)

    private LocalDate fechaDesde;
    private LocalDate fechaHasta;
    private Integer diasLibres;
    private String naviera;              // "INARPI", "OOCL"
    private String tipoServicio;         // "T. DIRECTO", "TRANSBORDO"

    // Todos los consignatarios con sus precios para esta ventana
    private List<CotizacionConsignatario> cotizaciones;
}