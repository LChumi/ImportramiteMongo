package com.cumpleanos.importramite.persistence.model.embarques;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "puertos_embarque")
public class PuertoEmbarque {
    @Id
    private String id;
    private String nombre;  // "NINGBO", "QUINGDAO", "SHANTOU", "YANTIAN"
}