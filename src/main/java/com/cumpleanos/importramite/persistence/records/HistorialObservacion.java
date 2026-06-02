package com.cumpleanos.importramite.persistence.records;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record HistorialObservacion (
        @JsonFormat(pattern = "dd-MM-yyyy") LocalDate fecha,
        String observacion,
        String correccion,
        String usuarioReporta,
        String usuarioCorrige
){}