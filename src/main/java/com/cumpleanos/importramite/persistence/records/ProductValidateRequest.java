package com.cumpleanos.importramite.persistence.records;

public record ProductValidateRequest(
        String productId,
        Integer cantidad,
        String usuario,
        String novedad
) {
}
