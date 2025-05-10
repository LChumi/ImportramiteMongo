package com.cumpleanos.importramite.persistence.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "muestra")
public class Muestra implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Setter(AccessLevel.NONE)
    private String id;

    private String revisionId;
    private String tramiteId;
    private String contendorId;

    private String barraBulto;
    private String barraMuestra;
    private Integer cantidad;
    private Boolean status;
    private String proceso;

    private List<String> barrasNovedad;

    @CreatedDate
    private LocalDate fecha;
    @CreatedDate
    private LocalTime hora;
}
