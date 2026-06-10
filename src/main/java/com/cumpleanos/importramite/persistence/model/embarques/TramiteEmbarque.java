package com.cumpleanos.importramite.persistence.model.embarques;

import com.cumpleanos.importramite.utils.enums.EstadoTramite;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Document(collection = "tramites_embarque")
public class TramiteEmbarque {
    @Id
    private String id;
    private String ordenLlegada;         // "AAA"
    private String empresaId;
    private String numeroTramite;             // "2026-1012"
    private String proveedorId;
    private String numeroBl;                  // "Q01765"
    private String cantidadContenedores; // "3 X 40 HQ"
    private String fleteValidadoId;      // → FleteValidado escogido
    private String consignatario;

    // Logística
    private LocalDate fechaEmbarque;
    private LocalDate fechaArribo;
    private Integer diasLibres;
    private String puertoSalida;         // "NINGBO"
    private String puertoLlegada;        // "GUAYAQUIL"

    // INEN / Documentos
    private String solicitudNEcuapass;
    private LocalDate fechaSolicitudEcuapass;
    private String identificar;          // "VIDRIO", "CERAMICA"
    private String solicitudNIntertek;
    private String preLiquidacion;
    private String polizaNChub;

    private LocalDateTime creadoEn;
    private LocalDateTime actualizadoEn;

    private EstadoTramite estado;

    private Boolean certificadoOrigen;
}