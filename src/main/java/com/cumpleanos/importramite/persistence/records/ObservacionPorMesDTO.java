package com.cumpleanos.importramite.persistence.records;

public record ObservacionPorMesDTO(
        int mes,
        long total,
        long corregidos
) { }