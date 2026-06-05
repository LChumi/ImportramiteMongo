package com.cumpleanos.importramite.persistence.model.embarques;

public record FleteValidacionRequest(
        ProcesoCotizacion proceso,
        SalidaBuque salida,
        String consignatario,
        OpcionFlete opcion,
        String usuario,
        String observacion
) {
}
