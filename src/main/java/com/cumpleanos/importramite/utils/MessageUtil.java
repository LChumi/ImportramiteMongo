package com.cumpleanos.importramite.utils;

public class MessageUtil {

    public static String MENSAJE_TRAMITE(String tramite, String fecha, String contenedores) {
        return String.format(
                """
                        Estimados,
                        
                        Adjunto a este correo encontrarán un archivo de Excel correspondiente a la lista de productos del trámite %s que llegará el día %s que llega al puerto de Guayaquil.\s
                        
                        El tramite llega en %s contenedor(es). \s
                        
                        Saludos cordiales.""",
                tramite, fecha, contenedores
        );
    }
}
