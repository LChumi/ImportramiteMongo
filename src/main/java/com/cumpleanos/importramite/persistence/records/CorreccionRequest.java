package com.cumpleanos.importramite.persistence.records;

import com.cumpleanos.importramite.persistence.model.ProductoCorreccion;

public record CorreccionRequest(
        String idProducto,
        ProductoCorreccion correccion
) {
}