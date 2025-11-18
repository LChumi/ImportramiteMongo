package com.cumpleanos.importramite.persistence.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;
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

    private List<ProdcutoCantidades> cantidades;

    //Datos Revision
    private Integer cantidadRevision;
    private Integer cantidadDiferenciaRevision;
    private String estadoRevision;
    private String usuarioRevision;
    private List<String> historialRevision;
    private List<String> observacionRevision;

    //Validacion
    private String usrValida;
    private String novedad;

    //Datos Muestra
    private String barraMuestra;
    private Integer cantidadMuestra;
    private List<String> historialBarrasMuestra;
    private String procesoMuestra;
    private String usuarioMuestra;
    private Boolean statusMuestra;

    public void calcularTotal() {

        if (cantidades == null || cantidades.isEmpty()) {
            if (this.bultos != null && this.cxb != null) {
                this.total = this.bultos * this.cxb;
            } else {
                this.total = 0; // O maneja el caso de error como prefieras
            }
            return;
        }

        boolean mismoCxb = cantidades.stream()
                .map(ProdcutoCantidades::getCxb)
                .distinct()
                .count() == 1;

        int totalBultos = cantidades.stream()
                .mapToInt(ProdcutoCantidades::getCantidad)
                .sum();

        if (mismoCxb) {
            int cxb = cantidades.get(0).getCxb();
            this.setCxb(cxb);
            this.setBultos(totalBultos);
            this.setTotal(cxb * totalBultos);
        } else {
            int total = cantidades.stream()
                    .mapToInt(pc -> pc.getCantidad() * pc.getCxb())
                    .sum();

            int maxCxb = cantidades.stream()
                    .mapToInt(ProdcutoCantidades::getCxb)
                    .max()
                    .orElse(0);
            this.setCxb(maxCxb);
            this.setBultos(totalBultos);
            this.setTotal(total);
        }
    }

    public void generateId(){
        this.id = this.tramiteId + "_" + this.contenedorId + "_" + this.barcode;
    }
}