package com.cumpleanos.importramite.persistence.model.embarques;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Document(collection = "fletes_validados")
public class FleteValidado {
    @Id
    private String id;

    private String salidaBuqueId;
    private String consignatarioId;
    private String nombreConsignatario;
    private String puertoEmbarqueNombre; // "NINGBO"
    private String tipoContenedor;       // "GYE - 40HQ"
    private String puertoDestino;        // "GUAYAQUIL"
    private Integer espacioM3;           // 64

    // Valores congelados al momento de validar
    private BigDecimal flete;
    private BigDecimal thc;
    private BigDecimal imo;
    private BigDecimal gastosBl;           // con IVA incluido
    private BigDecimal handlingContenedor; // con IVA incluido
    private BigDecimal total;

    private EstadoFlete estado; // VIGENTE, ANULADO
    private String motivoAnulacion;
    private String fleteReemplazadoPorId;  // si fue anulado, apunta al nuevo

    private String validadoPor;
    private LocalDateTime fechaValidacion;

    public enum EstadoFlete { VIGENTE, ANULADO }
}
