package com.cumpleanos.importramite.utils;

public class MessageUtil {

    public static String MENSAJE_TRAMITE(String tramite, String fecha) {
        return String.format(
                "Estimados,\n\n" +
                        "Adjunto a este correo encontrarán un archivo de Excel correspondiente a la lista de productos del trámite %s que llegará el día %s que llega al puerto de Guayaquil. \n\n" +
                        "Saludos cordiales.",
                tramite, fecha
        );
    }
}
