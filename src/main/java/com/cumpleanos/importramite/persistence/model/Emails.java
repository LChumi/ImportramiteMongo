package com.cumpleanos.importramite.persistence.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "emails")
@CompoundIndexes({
        @CompoundIndex(name = "emails_idx", def = "{'tipo':1}", unique = true)
})
public class Emails implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Setter(AccessLevel.NONE)
    private String id;

    private Long tipo;
    private String descripcion;
    private List<Destinatario> destinatarios;
}
