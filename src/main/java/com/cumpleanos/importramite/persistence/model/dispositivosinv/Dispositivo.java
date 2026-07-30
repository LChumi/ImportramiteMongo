package com.cumpleanos.importramite.persistence.model.dispositivosinv;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@Document(collection = "dispositivos")
public class Dispositivo {
    @Id
    private String id;

    @Indexed(unique = true)
    private String serial;

    private String marca;
    private String modelo;
    private String categoria;
    private LocalDate fechaCompra;
    private EstadoDispositivo estadoActual;
    private String ubicacion;

    private boolean activo;
    private String creadoPor;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}