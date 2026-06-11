package com.cumpleanos.importramite.persistence.records;

public record ObservacionPorBodegaDTO(
        Long idBodega,
        long total,
        long corregidos,
        long pendientes
) { }