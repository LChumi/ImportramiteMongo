package com.cumpleanos.importramite.persistence.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "destinatarios")
public class Destinatario implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String direccion;
}
