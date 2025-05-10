package com.cumpleanos.importramite.persistence.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "tramite")
public class Tramite implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @CreatedDate
    private LocalDate fechaCarga;
    private LocalDate fechaLlegada;
    private LocalDate fechaArribo;
    private LocalTime horaArribo;

    private List<Contenedor> contenedores = new ArrayList<>();
    private Short proceso = 1; // 1:Registrado 2:Pendiente 3:Validado 4:Muestra 5:Finalizado
}
