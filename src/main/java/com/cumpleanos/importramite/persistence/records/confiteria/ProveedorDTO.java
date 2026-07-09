package com.cumpleanos.importramite.persistence.records.confiteria;

import java.math.BigDecimal;

public record ProveedorDTO(
        String proveedor,
        Long cantidadReposiciones,
        Long totalProductos,
        BigDecimal valorTotal
) {
}