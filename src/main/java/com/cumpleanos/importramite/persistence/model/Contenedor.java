package com.cumpleanos.importramite.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@Builder
@Document(collection = "contenedor")
public class Contenedor implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private String id;
    private String usrBloquea = "";
    private Boolean bloqueado = false;
    @JsonIgnore
    private List<Producto> productos;

}
