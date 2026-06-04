package com.cumpleanos.importramite.persistence.model.embarques;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OpcionMasBarataResponse {
    private String consignatario;
    private String puerto;
    private OpcionFlete opcion;
    private String idBuque;
    private BigDecimal total;
}
