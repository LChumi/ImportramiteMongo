package com.cumpleanos.importramite.persistence.records;

public record RevisionRequest (
        String tramiteId,
        String contenedor,
        String barra,
        String usuario,
        Boolean status,
        Integer cantidad,
        Integer cxb,
        String obsCxb,
        Integer cxbNov
        )
{}
