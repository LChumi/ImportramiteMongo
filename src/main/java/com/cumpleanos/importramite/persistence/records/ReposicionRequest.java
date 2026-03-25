package com.cumpleanos.importramite.persistence.records;

import com.cumpleanos.importramite.persistence.model.confiteria.ConfiteriaDetalle;
import com.cumpleanos.importramite.persistence.model.confiteria.ReposicionConfiteria;

import java.util.List;

public record ReposicionRequest(
        ReposicionConfiteria repo,
        List<ConfiteriaDetalle> detalles
) {
}
