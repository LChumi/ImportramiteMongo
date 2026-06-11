package com.cumpleanos.importramite.persistence.records;

import java.math.BigDecimal;

public record ObservacionResumenDTO(
        long totalObservaciones,
        long conCorreccion,
        long sinCorreccion,
        BigDecimal totalValorAfectado
) { }