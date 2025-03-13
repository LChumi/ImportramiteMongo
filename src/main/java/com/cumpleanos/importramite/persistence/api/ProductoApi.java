package com.cumpleanos.importramite.persistence.api;

public record ProductoApi(
        String secuencia,
        String pro_id,
        String pro_id1,
        Integer cxb,
        String pro_nombre,
        String bod_nombre,
        Double pvp,
        String unidad,
        String bulto,
        Integer stock_real,
        Integer stock_disponible,
        Long bod_codigo,
        Long pro_empresa,
        Long pro_codigo
) {
}
