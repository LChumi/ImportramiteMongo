package com.cumpleanos.importramite.persistence.model.dispositivosinv;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@Document(collection = "prestamos")
public class Prestamo {
    @Id private String id;

    @Indexed  // índice para buscar préstamos por dispositivo rápido
    private String dispositivoId;

    @Indexed  // índice para búsquedas directas por S/N
    private String serialDispositivo;

    private String responsable;

    private LocalDateTime fechaEntrega;
    private LocalDateTime fechaDevolucion;

    private EstadoPrestamo estado;
    private String observaciones;

    private String creadoPor;
    private String modificadoPor;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}