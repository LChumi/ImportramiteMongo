package com.cumpleanos.importramite.persistence.records;

public record ObservacionPorBodegaRaw(
        Long id,       // mapea el _id del $group (idBodega)
        long total,
        long corregidos
) {
}
