package com.cumpleanos.importramite.persistence.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductoCantidades {
    private int cantidad;
    private int cxb;
    private String observacion;
    private int cantRevision;
    private int cxbRevision;
    private Boolean status;
}