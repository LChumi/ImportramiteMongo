package com.cumpleanos.importramite.persistence.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@Document(collection = "contenedor")
@CompoundIndex(name = "contenedor_unique_tramite_idx" ,
def = "{'id' : 1, 'tramite' : 1}")
public class Contenedor implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Setter(AccessLevel.NONE)
    private String id;

    private String contenedorId;
    private String usrBloquea;
    private Boolean bloqueado;
    private Boolean finalizado;
    private List<String> productIds;
    private String tramiteId;

    //Datos de Procesamiento
    private LocalDate startDate;
    private LocalTime startHour;
    private LocalTime endHour;

}
