package com.cumpleanos.importramite.persistence.records.confiteria;

import java.math.BigDecimal;

public record ProductoDTO(
        String item,
        String producto,
        Long cantidadPedida,
        BigDecimal valorTotal
) {
}