package com.cumpleanos.importramite.persistence.records.confiteria;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FechaDTO(
        LocalDate fecha,
        Long reposiciones,
        Long productos,
        BigDecimal valorTotal
) {
}