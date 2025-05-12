package com.cumpleanos.importramite.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StringUtils {
    public static String trimWhitespace(String input) {
        return input == null ? null : input.trim();
    }

    public static String sanitizeForMongoDB(String input) {
        if (input == null) return null;
        return trimWhitespace(input).replaceAll("[^\\w\\s]", ""); // Remueve caracteres especiales

    }

    public static String obtenerHora(){
        LocalDateTime fecha = LocalDateTime.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("HH:mm:ss");
        return fecha.format(formato);
    }

}