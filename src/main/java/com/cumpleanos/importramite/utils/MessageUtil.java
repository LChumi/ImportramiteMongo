package com.cumpleanos.importramite.utils;

public class MessageUtil {

    public static String MENSAJE_TRAMITE(String tramite, String fecha, String contenedores) {
        return String.format(
                """
                        Estimados,
                        
                        Adjunto a este correo encontrarán un archivo de Excel correspondiente a la lista de productos del trámite %s que llegará el día %s al puerto de Guayaquil.
                        
                        El trámite consta de %s contenedor(es).
                        
                        Saludos cordiales.
                        """,
                tramite, fecha, contenedores
        );
    }
}
