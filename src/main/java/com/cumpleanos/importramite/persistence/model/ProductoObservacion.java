package com.cumpleanos.importramite.persistence.model;

import com.cumpleanos.importramite.persistence.records.HistorialObservacion;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "producto_observacion")
@CompoundIndex(name = "producto_unique_bodega_idx", def = "{'item' : 1, 'idBodega': 1}", unique = true)
public class ProductoObservacion {

    @Id
    private String id;

    private Long idBodega;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate fecha;
    private String item;
    private String descripcion;
    private String bulto;
    private String unidad;
    private String cxb;

    private int stock;

    private BigDecimal precio;
    private BigDecimal precioTotal;

    private String usuario;
    private String detalle;
    private String diferencia;

    private ProductoCorreccion correccion;

    private List<HistorialObservacion> historial = new ArrayList<>();

}