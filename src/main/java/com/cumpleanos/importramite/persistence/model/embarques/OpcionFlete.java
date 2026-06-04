package com.cumpleanos.importramite.persistence.model.embarques;

import com.cumpleanos.importramite.utils.enums.TipoContenedor;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Data
public class OpcionFlete {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String id = UUID.randomUUID().toString();

    private TipoContenedor tipoContenedor;   // "GYE - 40HQ", "POSORJA - 40HQ", "40NORD"
    private Integer espacioM3;       // 64, 55
    private String puertoDestino;    // "GUAYAQUIL", "POSORJA"

    private BigDecimal flete = BigDecimal.ZERO;
    private BigDecimal thc = BigDecimal.ZERO;
    private BigDecimal imo = BigDecimal.ZERO;

    private BigDecimal gastosBl = BigDecimal.ZERO;
    private BigDecimal handlingContenedor = BigDecimal.ZERO;
    private String numerBl;

    private BigDecimal porcentajeIva = new BigDecimal("0.15");

    public BigDecimal getIvaBl() {

        return gastosBl
                .multiply(porcentajeIva)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getIvaHandling() {

        return handlingContenedor
                .multiply(porcentajeIva)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getSubtotalFlete() {

        return flete
                .add(thc)
                .add(imo)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getSubtotalGastos() {

        return gastosBl
                .add(getIvaBl())
                .add(handlingContenedor)
                .add(getIvaHandling())
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getTotal() {

        return getSubtotalFlete()
                .add(getSubtotalGastos())
                .setScale(2, RoundingMode.HALF_UP);
    }
}