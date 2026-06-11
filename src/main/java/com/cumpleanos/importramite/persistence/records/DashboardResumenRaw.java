package com.cumpleanos.importramite.persistence.records;

import java.math.BigDecimal;

public record DashboardResumenRaw(
        long total,
        long conCorreccion,
        BigDecimal totalValor
) {
}
