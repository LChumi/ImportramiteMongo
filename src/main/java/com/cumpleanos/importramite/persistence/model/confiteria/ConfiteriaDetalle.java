package com.cumpleanos.importramite.persistence.model.confiteria;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "Confiteria_detalle")
@CompoundIndex(name = "detalle_reposicion_idx",
        def = "{'reposicionId' : 1, 'barra': 1}",
unique = true)
public class ConfiteriaDetalle {

    @Id
    private String id;

    private String reposicionId;
    private String ultVenta;
    private String ultComp;
    private Long ultCantCom;
    private Double cantVenta;
    private Long stockIni;
    private String cliNombre;
    private String barra;
    private String item;
    private String proNombre;
    private Long stockDisp;
    private Long stockReal;
    private Double pvp;
}
