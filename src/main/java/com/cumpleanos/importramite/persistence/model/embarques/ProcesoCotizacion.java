package com.cumpleanos.importramite.persistence.model.embarques;

import com.cumpleanos.importramite.utils.enums.EstadoProceso;
import com.cumpleanos.importramite.utils.enums.TipoReferencia;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "procesos_cotizacion")
public class ProcesoCotizacion {
    @Id
    private String id;

    // Q01765
    private String numeroReferencia;

    // BL o tramite
    private TipoReferencia tipoReferencia;

    private String empresaId;

    private String proveedorId;

    // ACTIVO, FINALIZADO
    private EstadoProceso estado;

    private LocalDateTime creadoEn;
}
