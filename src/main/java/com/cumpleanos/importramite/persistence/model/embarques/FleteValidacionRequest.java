package com.cumpleanos.importramite.persistence.model.embarques;

public record FleteValidacionRequest(
        ProcesoCotizacion proceso,
        SalidaBuque salida,
        CotizacionConsignatario consignatario,
        OpcionFlete opcion,
        String usuario
) {
}
