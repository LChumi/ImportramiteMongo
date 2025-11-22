package com.cumpleanos.importramite.persistence.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProdcutoCantidades {
    private int cantidad;
    private int cxb;
    private String observacion;
    private int cantRevision;
    private Boolean status;
}