package com.cumpleanos.importramite.service.implementation.reports;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.HashMap;

@Service
public class ReporteService {

    public byte[] pdfPosCorriente(){
        JasperReport report = cargarReporte("reports/ticketCliente.jasper");
        HashMap<String, Object> parametros = new HashMap<>();
        parametros.put("empresa", );

    }


    private JasperReport cargarReporte(String recurso) throws JRException {
        InputStream in = getClass().getClassLoader().getResourceAsStream(recurso);
        if (in == null){
            throw new JRException("No se pudo encontrar el recurso: " + recurso);
        }
        return (JasperReport) JRLoader.loadObject(in); //CARGA DIRECTA
    }

    private JasperReport compilarReporte(String recurso) throws JRException {
        InputStream in = getClass().getClassLoader().getResourceAsStream(recurso);
        if (in == null){
            throw new JRException("No se pudo encontrar el recurso: " + recurso);
        }
        return JasperCompileManager.compileReport(in); //COMPILAR USADO CON JRXML
    }
}
