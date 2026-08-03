package com.cumpleanos.importramite.service.implementation.reports;

import com.cumpleanos.importramite.persistence.model.pos.MedianetPOS;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Slf4j
@Service
public class ReporteService {

    public byte[] pdfPos(MedianetPOS m) {

        try {

            ReportesPOS reportes = seleccionarReportes(
                    m.getRequest().tipoTransaccion()
            );

            Map<String, Object> parametros = construirParametros(m);

            JasperPrint principal = generarPrint(
                    reportes.reporteComercio(),
                    parametros
            );

            JasperPrint cliente = generarPrint(
                    reportes.reporteCliente(),
                    parametros
            );

            principal.getPages().addAll(cliente.getPages());

            return JasperExportManager.exportReportToPdf(principal);

        } catch (JRException e) {
            throw new RuntimeException("Error al generar el reporte", e);
        }
    }

    private JasperPrint generarPrint(
            String rutaReporte,
            Map<String, Object> parametros
    ) throws JRException {

        JasperReport report = cargarReporte(rutaReporte);

        return JasperFillManager.fillReport(
                report,
                parametros,
                new JREmptyDataSource()
        );
    }

    private ReportesPOS seleccionarReportes(String tipoTransaccion) {

        return switch (tipoTransaccion) {

            case "01" -> new ReportesPOS(
                    "reports/ticketPos.jasper",
                    "reports/ticketCliente.jasper"
            );

            case "02" -> new ReportesPOS(
                    "reports/ticketPosDiferido.jasper",
                    "reports/ticketClienteDif.jasper"
            );

            case "03" -> new ReportesPOS(
                    "reports/ticketPosAnulacion.jasper",
                    "reports/ticketPosAnulacion.jasper"
            );

            default -> throw new IllegalArgumentException(
                    "Tipo de transacción no soportado: "
                            + tipoTransaccion
            );
        };
    }
    private JasperReport cargarReporte(String recurso) throws JRException {
        InputStream in = getClass().getClassLoader().getResourceAsStream(recurso);
        if (in == null){
            throw new JRException("No se pudo encontrar el recurso: " + recurso);
        }
        return (JasperReport) JRLoader.loadObject(in); //CARGA DIRECTA
    }

    private record ReportesPOS(
            String reporteComercio,
            String reporteCliente
    ) {}

    private Map<String, Object> construirParametros(MedianetPOS m) {
        String fecha = convertirFecha(m.getResponse().fecha());
        String hora = convertirHora(m.getResponse().hora());
        String tipoDiferido;
        String interes = null;
        String granTotal = null;
        String targetaTruncada = m.getResponse().tarjetaTruncada();
        switch (m.getRequest().codigoDiferido()) {
            case "01" -> tipoDiferido = "CON INTERES";
            case "04" -> tipoDiferido = "SIN INTERESES";
            default -> tipoDiferido ="";
        }

        if (m.getResponse().interes() != null) {
            BigDecimal interesBD = parseValor(m.getResponse().interes(), 2);
            BigDecimal granTotalBD = BigDecimal.valueOf(m.getRequest().total())
                    .add(interesBD);
            interes = interesBD.toString();
            granTotal = granTotalBD.toString();
        }

        String referenciaAnulada = "";
        String referencia = m.getResponse().referencia();

        if ("03".equals(m.getRequest().tipoTransaccion())) {
            // La anulada es igual a la referencia original
            referenciaAnulada = referencia;

            // La referencia se incrementa en 1
            try {
                int refNum = Integer.parseInt(referencia);
                referencia = String.format("%06d", refNum + 1);
                // Resultado: "000215"
            } catch (NumberFormatException e) {
                log.error("Error al convertir formato: {}", e.getMessage());
            }
        }

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("empresa", m.getEmpresa());
        parametros.put("direccion", m.getDireccion());
        parametros.put("telefono", m.getTelefono());
        parametros.put("mid", m.getRequest().mid());
        parametros.put("mid2", m.getResponse().mid());
        parametros.put("tid", m.getRequest().tid());
        parametros.put("ruc", m.getRuc());
        parametros.put("referencia", referencia);
        parametros.put("numeroAutorizacion", m.getResponse().numeroAutorizacion());
        parametros.put("lote", m.getResponse().lote());
        parametros.put("tarifaStr", "BASE CONSUMO TARIFA 15:");
        parametros.put("ivaStr", "IVA 15%:");
        parametros.put("subtotal", String.format("%.2f", m.getRequest().subtotal()));
        parametros.put("subtotal0", "0.00");
        parametros.put("iva", String.format("%.2f",m.getRequest().iva()));
        parametros.put("redPos", m.getRed().trim());
        parametros.put("plazo", m.getRequest().plazo());
        parametros.put("fechaVencimiento", m.getResponse().fechaVencimiento());
        parametros.put("total", String.format("%.2f", m.getRequest().total()));
        parametros.put("grupoTarjeta", m.getResponse().grupoTarjeta());
        parametros.put("tarjetaTruncada", targetaTruncada);
        parametros.put("fecha", fecha);
        parametros.put("hora", hora);
        parametros.put("ciudad", m.getCiudad());
        parametros.put("aid", m.getResponse().aid());
        parametros.put("tipoDiferido", tipoDiferido);
        parametros.put("interes", interes);
        parametros.put("granTotal", granTotal);
        parametros.put("referenciaAnulada", referenciaAnulada);
        parametros.put("nombreTarjetahabiente", m.getResponse().nombreTarjetahabiente());
        return parametros;
    }

    public String convertirFecha(String fechaStr) {
        // Parsear la cadena como yyyyMMdd
        LocalDate fecha = LocalDate.parse(fechaStr, DateTimeFormatter.ofPattern("yyyyMMdd"));
        // Formatear como dd/MMM/yy (mes abreviado en mayúsculas)
        return fecha.format(DateTimeFormatter.ofPattern("dd/MMM/yy", Locale.ENGLISH)).toUpperCase();
    }

    public String convertirHora(String horaStr) {
        // Parsear la cadena como HHmmss
        LocalTime hora = LocalTime.parse(horaStr, DateTimeFormatter.ofPattern("HHmmss"));
        // Formatear como HH:mm:ss
        return hora.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    public BigDecimal parseValor(String valor, int decimales) {
        if (valor == null || valor.isEmpty()) {
            return BigDecimal.ZERO;
        }
        // Elimina ceros a la izquierda
        StringBuilder limpio = new StringBuilder(valor.replaceFirst("^0+(?!$)", ""));

        // Si la longitud es menor que los decimales, rellena
        while (limpio.length() <= decimales) {
            limpio.insert(0, "0");
        }

        // Parte entera y parte decimal
        int index = limpio.length() - decimales;
        String parteEntera = limpio.substring(0, index);
        String parteDecimal = limpio.substring(index);

        String resultado = parteEntera + "." + parteDecimal;
        return new BigDecimal(resultado);
    }
}