package com.cumpleanos.importramite.persistence.model.embarques;

import com.cumpleanos.importramite.utils.enums.EstadoFlete;
import com.cumpleanos.importramite.utils.enums.TipoContenedor;
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

    private String procesoCotizacionId;

    private String salidaBuqueId;
    private String consignatarioId;
    private String nombreConsignatario;
    private String puertoEmbarqueNombre; // "NINGBO"
    private TipoContenedor tipoContenedor;       // "40HQ"
    private String puertoDestino;        // "GUAYAQUIL"
    private Integer espacioM3;           // 64

    // Valores congelados al momento de validar
    private BigDecimal flete;
    private BigDecimal thc;
    private BigDecimal imo;
    private BigDecimal gastosBlTotal;           // con IVA incluido
    private BigDecimal handlingContenedorTotal; // con IVA incluido
    private BigDecimal total;

    private EstadoFlete estado = EstadoFlete.VIGENTE;// VIGENTE, ANULADO
    private String motivoAnulacion;
    private String fleteReemplazadoPorId;  // si fue anulado, apunta al nuevo

    private String validadoPor;
    private LocalDateTime fechaValidacion;
}
