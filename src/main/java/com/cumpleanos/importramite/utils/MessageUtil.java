package com.cumpleanos.importramite.utils;

public class MessageUtil {

    public static String MENSAJE_TRAMITE(String tramite, String fecha, String contenedores) {
        return String.format("""
                        <div style='font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;'>
                                <div style='background-color: #ffffff; border-radius: 10px; padding: 30px; max-width: 600px; margin: auto; box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);'>
                                    <h2 style='color: #333;'>Notificación de Trámite</h2>
                                    <p style='font-size: 16px; color: #555;'>
                                        Estimados,
                                    </p>
                                    <p style='font-size: 16px; color: #555;'>
                                        Adjunto a este correo encontrarán un archivo de Excel correspondiente a la <strong>lista de productos</strong> del trámite <strong style='color: #354edc;'>%s</strong>\s
                                        que llegará el día <strong style='color: #354edc;'>%s</strong> al puerto de Guayaquil.
                                    </p>
                                    <p style='font-size: 16px; color: #555;'>
                                        El trámite consta de <strong style='color: #354edc;'>%s contenedor(es)</strong>.
                                    </p>
                                    <p style='font-size: 16px; color: #555;'>
                                        Saludos cordiales.
                                    </p>
                                </div>
                        </div>
                """, tramite, fecha, contenedores);
    }
}
