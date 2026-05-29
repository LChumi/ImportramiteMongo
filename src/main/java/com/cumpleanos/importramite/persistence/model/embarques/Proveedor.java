package com.cumpleanos.importramite.persistence.model.embarques;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "proveedores")
public class Proveedor {

    @Id
    private String id;
    private String nombre;
}
