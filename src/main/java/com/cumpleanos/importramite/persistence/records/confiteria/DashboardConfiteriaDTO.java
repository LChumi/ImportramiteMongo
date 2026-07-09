package com.cumpleanos.importramite.persistence.records.confiteria;

import java.math.BigDecimal;
import java.util.List;

public record DashboardConfiteriaDTO(
        Long totalReposiciones,
        Long totalProductos,
        BigDecimal valorTotal,
        List<ProveedorDTO> proveedores,
        List<ProductoDTO> topProductos,
        List<FechaDTO> historial) {
}