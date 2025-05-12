package com.cumpleanos.importramite.persistence.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@Builder
@Document(collection = "producto")
@CompoundIndex(name = "barcode_unique_contenedor_tramite_idx",
        def = "{'barcode' : 1, 'contenedorId': 1, 'tramiteId': 1}",
        unique = true)

public class Producto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private String id;
    private String barcode;
    private String contenedorId;
    private String tramiteId;

    private String id1;
    private String nombre;
    private Integer cxb;
    private Integer bultos;
    private Integer total;
    private String itemAlterno;
    private Double pvp;
    private Integer cxbAnterior;
    private String ubicacionBulto;
    private String ubicacionUnidad;
    private Integer stockZhucay;
    private Integer stockNarancay;
    private String descripcion;
    private String barraSistema;
    private Integer diferencia;
    private int secuencia;

    //Datos Revision
    private Integer cantidadRevision;
    private Integer cantidadDiferenciaRevision;
    private String estadoRevision;
    private String usuarioRevision;
    private List<String> historialRevision;

    //Datos Muestra
    private String barraMuestra;
    private Integer cantidadMuestra;
    private List<String> historialBarrasMuestra;
    private String procesoMuestra;
    private String usuarioMuestra;
    private String procesoMuetsra;


    public void calcularTotal() {
        if (this.bultos != null && this.cxb != null) {
            this.total = this.bultos * this.cxb;
        } else {
            this.total = 0; // O maneja el caso de error como prefieras
        }
    }

    public void generateId(){
        this.id = this.tramiteId + "_" + this.contenedorId + "_" + this.barcode;
    }
}
