package com.cumpleanos.importramite.service.implementation.embarque;

import com.cumpleanos.importramite.persistence.model.embarques.CotizacionConsignatario;
import com.cumpleanos.importramite.persistence.model.embarques.OpcionFlete;
import com.cumpleanos.importramite.persistence.model.embarques.OpcionMasBarataResponse;
import com.cumpleanos.importramite.persistence.model.embarques.SalidaBuque;
import com.cumpleanos.importramite.persistence.repository.embarques.SalidaBuqueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CotizacionService {

    private final SalidaBuqueRepository buqueRepository;

    public OpcionMasBarataResponse obtenerMejorOpcion(String procesoCotizacionId){
        List<SalidaBuque> salidas = buqueRepository.findByProcesoCotizacionId(procesoCotizacionId);

        OpcionFlete mejorOpcion = null ;

        String mejorConsignatario = null;
        String mejorPuerto = null;

        BigDecimal menor = null;

        for (SalidaBuque salida : salidas) {
            for (CotizacionConsignatario consignatario: salida.getCotizaciones()){
                for (OpcionFlete opcion : consignatario.getOpciones()){

                    BigDecimal total = opcion.getTotal();

                    if (menor == null || total.compareTo(menor) < 0){
                        menor = total;
                        mejorOpcion = opcion;
                        mejorConsignatario = consignatario.getNombreConsignatario();
                        mejorPuerto = salida.getPuertoEmbarqueNombre();
                    }
                }
            }
        }

        return OpcionMasBarataResponse.builder()
                .consignatario(mejorConsignatario)
                .puerto(mejorPuerto)
                .opcion(mejorOpcion)
                .total(menor)
                .build();
    }
}
