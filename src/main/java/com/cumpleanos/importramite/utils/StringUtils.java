package com.cumpleanos.importramite.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.cumpleanos.importramite.utils.ProductoStatus.AGREGADO;
import static com.cumpleanos.importramite.utils.ProductoStatus.RETIRADO;

public class StringUtils {
    public static String trimWhitespace(String input) {
        return input == null ? null : input.trim();
    }

    public static String obtenerHora() {
        LocalDateTime fecha = LocalDateTime.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("HH:mm:ss");
        return fecha.format(formato);
    }

    public static String historial(boolean status) {
        if (status) {
            return " | " + obtenerHora() + " | " + AGREGADO.name();
        } else {
            return  " | " + obtenerHora() + " | " + RETIRADO.name();
        }
    }

    /**
     * Metodo para extraer la hora
     *
     * @param texto String texto ya sea 16:42:36 AGREGADO o BARRA 1234567890 16:52:36 AGREGADO
     * @return la hora extraida del texto Output: 16:42:36
     */
    public static String obtenerHora(String texto) {
        Pattern pattern = Pattern.compile("\\b(\\d{2}:\\d{2}:\\d{2})\\b");
        Matcher matcher = pattern.matcher(texto);
        return matcher.find() ? matcher.group(1) : null;
    }

    /**
     * Metodo para separar las Barras de un texto
     *
     * @param texto BARRA: cod_barras
     * @return codigo de barras
     */
    public static String extraerBarraPattern(String texto) {
        Pattern pattern = Pattern.compile("BARRA: (\\d+)");
        Matcher matcher = pattern.matcher(texto);
        return matcher.find() ? matcher.group(1) : null;
    }

    public static String extraerBarra(String texto) {
        return texto.split(":")[1].split("\\|")[0].trim();
    }


}