package com.cumpleanos.importramite.persistence.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "producto")
@CompoundIndex(name = "barcode_unique_contenedor_tramite_idx",
        def = "{'barcode' : 1, 'contenedorId': 1, 'tramiteId': 1}",
        unique = true)
public class Producto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Setter(AccessLevel.NONE)
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
    private String observacion;
    private String IPC;
    private String PNCE;
    private String IEPNC;
    private int secuencia;

    private List<ProductoCantidades> cantidades;

    //Datos Revision
    private Integer cantidadRevision;
    private Integer cantidadDiferenciaRevision;
    private String estadoRevision;
    private String usuarioRevision;
    private List<String> historialRevision;
    private List<String> observacionRevision;

    //Validacion
    private String usrValida;
    private List<String> novedad;

    //Datos Muestra
    private String barraMuestra;
    private Integer cantidadMuestra;
    private List<String> historialBarrasMuestra;
    private String procesoMuestra;
    private String usuarioMuestra;
    private Boolean statusMuestra;

    public void sumarBultosPrincipal(int bultosNuevos){
        if (this.bultos == null){
            this.bultos = 0;
        }
        this.bultos += bultosNuevos;
        recalcularTotalPrincipal();
    }

    public void recalcularTotalPrincipal(){
        if (this.cxb != null && this.bultos != null){
            this.total = this.cxb * this.bultos;
        } else {
           total = 0;
        }
    }

    public void recalcularTotalDesdeCantidades() {
        int totalBultos = 0;
        int totalUnidades = 0;

        if (cantidades != null ){
            for (ProductoCantidades pc : cantidades) {
                totalBultos += pc.getCantidad();
                totalUnidades += pc.getCantidad() * pc.getCxb();
            }

            this.bultos = totalBultos;
            this.total = totalUnidades;
        }
    }

    /**
     * Suma una cantidad a un cxb en la lista; si no existe, lo crea.
     */
    public void sumarCantidades(int bultos, int cxb){
        if (cantidades == null){
            cantidades = new ArrayList<>();
        }

        ProductoCantidades existente = cantidades.stream()
                .filter(c -> c.getCxb() == cxb)
                .findFirst()
                .orElse(null);
        if (existente == null){
            existente = ProductoCantidades.builder()
                    .cantidad(bultos)
                    .cxb(cxb)
                    .cantRevision(0)
                    .build();
            cantidades.add(existente);
        } else {
            existente.setCantidad(existente.getCantidad() + bultos);
        }

        //Recalcular el total despues de tocar el array
        recalcularTotalPrincipal();
    }

    public void generateId(){
        this.id = this.tramiteId + "_" + this.contenedorId + "_" + this.barcode;
    }
}