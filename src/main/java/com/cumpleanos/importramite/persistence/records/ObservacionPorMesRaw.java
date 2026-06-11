package com.cumpleanos.importramite.persistence.records;

public record ObservacionPorMesRaw(
        MesId id,
        long total,
        long corregidos
) {
    public record MesId(int mes) {}
}

