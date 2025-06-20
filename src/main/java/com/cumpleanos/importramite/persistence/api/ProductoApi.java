package com.cumpleanos.importramite.persistence.api;

public record ProductoApi(
        String secuencia,
        String proId,
        String proId1,
        Integer cxb,
        String nombre,
        String bodega,
        Double pvp,
        String unidad,
        String bulto,
        Integer stockReal,
        Integer stockDisponible,
        Long bodCodigo,
        Long proEmpresa,
        Long proCodigo
) {
}
