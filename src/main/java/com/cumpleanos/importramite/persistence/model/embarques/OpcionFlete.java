package com.cumpleanos.importramite.persistence.model.embarques;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OpcionFlete {
    private String tipoContenedor;   // "GYE - 40HQ", "POSORJA - 40HQ", "40NORD"
    private Integer espacioM3;       // 64, 55
    private String puertoDestino;    // "GUAYAQUIL", "POSORJA"

    private BigDecimal flete;
    private BigDecimal thc;
    private BigDecimal imo;
    private BigDecimal subtotalFlete;   // calculado

    private BigDecimal gastosBl;
    private BigDecimal ivaGastosBl;     // calculado 15%
    private BigDecimal handlingContenedor;
    private BigDecimal ivaHandling;     // calculado 15%
    private BigDecimal subtotalGastos;  // calculado

    private BigDecimal total;           // calculado
}