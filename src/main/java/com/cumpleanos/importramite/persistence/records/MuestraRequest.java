package com.cumpleanos.importramite.persistence.records;

public record MuestraRequest (
    String barra,
    String muestra,
    String tramiteId,
    String contenedor,
    String usuario,
    boolean status
){}
