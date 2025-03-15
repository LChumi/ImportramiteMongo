package com.cumpleanos.importramite.persistence.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@Document("novedades")
public class Novedades implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String tramiteId;

}
