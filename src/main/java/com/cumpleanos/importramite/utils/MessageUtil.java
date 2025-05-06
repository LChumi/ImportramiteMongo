package com.cumpleanos.importramite.utils;

public class MessageUtil {

    public static String MENSAJE_TRAMITE(String tramite, String fecha, String contenedores) {
        return String.format("""
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
                """, tramite, fecha, contenedores);
    }

    public static String MENSAJE_LLEGADA_BODEGA(String tramite, String fechaLlegada, String horaEstimada, String contenedores) {
        return String.format("""
                    <div style='background-color: #ffffff; border-radius: 10px; padding: 30px; max-width: 600px; margin: auto; box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);'>
                                <h2 style='color: #333;'>Notificación de Llegada a Bodega</h2>
                                <p style='font-size: 16px; color: #555;'>
                                    Estimados,
                                </p>
                                <p style='font-size: 16px; color: #555;'>
                                    Llegada del trámite <strong style='color: #354edc;'>%s</strong>.
                                </p>
                                <p style='font-size: 16px; color: #555;'>
                                    El trámite consta de <strong style='color: #354edc;'>%s contenedor(es)</strong>, El cual llegara a la bodega el día\s
                                    <strong style='color: #354edc; font-size: 18px;'>%s</strong>.
                                </p>
                                <p style ></p>
                                <p style='font-size: 16px; color: #555;'>
                                    La hora estimada de llegada es: <strong style='color: #354edc;'>%s</strong>.
                                </p>
                                <p style='font-size: 14px; color: #888;'>
                                    ⚠ Estar atentos: si la hora cambia, se enviará una nueva notificación.
                                </p>
                                <hr style='border: none; border-top: 1px solid #ccc;'/>
                                <p style='font-size: 14px; color: #888; text-align: center;'>
                                    Este es un mensaje automático, por favor no responder.
                                </p>
                    </div>
                """, tramite, contenedores, fechaLlegada, horaEstimada);
    }

}
