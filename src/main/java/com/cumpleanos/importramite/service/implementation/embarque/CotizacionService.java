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
import java.util.stream.IntStream;

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
        String idBuque = null;

        BigDecimal menor = null;

        for (SalidaBuque salida : salidas) {
                for (OpcionFlete opcion : salida.getCotizacion().getOpciones()){

                    BigDecimal total = opcion.getTotal();

                    if (total == null) {
                        continue;
                    }

                    if (menor == null || total.compareTo(menor) < 0){
                        menor = total;
                        mejorOpcion = opcion;
                        mejorConsignatario = salida.getCotizacion().getNombreConsignatario();
                        mejorPuerto = salida.getPuertoEmbarqueNombre();
                        idBuque = salida.getId();
                    }
                }
        }
        if (mejorOpcion == null) {
            return null;
        }

        return OpcionMasBarataResponse.builder()
                .consignatario(mejorConsignatario)
                .puerto(mejorPuerto)
                .opcion(mejorOpcion)
                .total(menor)
                .idBuque(idBuque)
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

    @Override
    public SalidaBuque agregarOpcion(String idBuque, String cotizacionId, OpcionFlete opcion) {
        SalidaBuque buque = buqueRepository.findById(idBuque)
                .orElseThrow(() -> new DocumentNotFoundException("Buque no encontrado con id: " + idBuque));

        CotizacionConsignatario cotizacion = buque.getCotizacion();
        if (cotizacion == null || !cotizacion.getId().equals(cotizacionId)) {
            throw new DocumentNotFoundException("Cotización no encontrada con id: " + cotizacionId);
        }

        cotizacion.getOpciones().add(opcion);
        return buqueRepository.save(buque);
    }


    @Override
    public SalidaBuque actualizarOpcion(String idBuque, String cotizacionId, String opcionId, OpcionFlete opcionActualizada) {
        SalidaBuque buque = buqueRepository.findById(idBuque)
                .orElseThrow(() -> new DocumentNotFoundException("Buque no encontrado: " + idBuque));

        CotizacionConsignatario cotizacion = buque.getCotizacion();
        if (cotizacion == null || !cotizacion.getId().equals(cotizacionId)) {
            throw new DocumentNotFoundException("Cotización no encontrada: " + cotizacionId);
        }

        List<OpcionFlete> opciones = cotizacion.getOpciones();
        int idx = IntStream.range(0, opciones.size())
                .filter(i -> opciones.get(i).getId().equals(opcionId))
                .findFirst()
                .orElseThrow(() -> new DocumentNotFoundException("Opción no encontrada: " + opcionId));

        // Preservar el id original
        opcionActualizada.setId(opcionId);
        opciones.set(idx, opcionActualizada);

        buque.setActualizadoEn(LocalDateTime.now());
        return buqueRepository.save(buque);
    }

    @Override
    public SalidaBuque eliminarOpcion(String idBuque, String cotizacionId, String opcionId) {
        SalidaBuque buque = buqueRepository.findById(idBuque)
                .orElseThrow(() -> new DocumentNotFoundException("Buque no encontrado: " + idBuque));

        CotizacionConsignatario cotizacion = buque.getCotizacion();
        if (cotizacion == null || !cotizacion.getId().equals(cotizacionId)) {
            throw new DocumentNotFoundException("Cotización no encontrada: " + cotizacionId);
        }

        boolean removed = cotizacion.getOpciones().removeIf(o -> o.getId().equals(opcionId));
        if (!removed) {
            throw new DocumentNotFoundException("Opción no encontrada: " + opcionId);
        }

        buque.setActualizadoEn(LocalDateTime.now());
        return buqueRepository.save(buque);
    }
}