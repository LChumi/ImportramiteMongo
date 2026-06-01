package com.cumpleanos.importramite.service.implementation.embarque;

import com.cumpleanos.importramite.persistence.model.embarques.CotizacionConsignatario;
import com.cumpleanos.importramite.persistence.model.embarques.OpcionFlete;
import com.cumpleanos.importramite.persistence.model.embarques.OpcionMasBarataResponse;
import com.cumpleanos.importramite.persistence.model.embarques.SalidaBuque;
import com.cumpleanos.importramite.persistence.repository.embarques.SalidaBuqueRepository;
import com.cumpleanos.importramite.service.exception.DocumentNotFoundException;
import com.cumpleanos.importramite.service.implementation.GenericServiceImpl;
import com.cumpleanos.importramite.service.interfaces.embarque.ISalidaBuqueService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CotizacionService extends GenericServiceImpl<SalidaBuque, String> implements ISalidaBuqueService {

    private final SalidaBuqueRepository buqueRepository;

    @Override
    public CrudRepository<SalidaBuque, String> getRepository() {
        return buqueRepository;
    }

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

    @Override
    public List<SalidaBuque> getByProcesoCotizacionId(String procesoCotizacionId) {
        return buqueRepository.findByProcesoCotizacionId(procesoCotizacionId);
    }

    @Override
    public SalidaBuque update(String id, SalidaBuque s) {
        SalidaBuque found = buqueRepository.findById(id).orElseThrow( () -> new DocumentNotFoundException("Buque no encontrado con id: " + id));
        found.setFechaDesde(s.getFechaDesde());
        found.setFechaHasta(s.getFechaHasta());
        found.setDiasLibres(s.getDiasLibres());
        found.setActualizadoEn(LocalDateTime.now());
        return buqueRepository.save(found);
    }
}