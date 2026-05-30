package com.cumpleanos.importramite.service.implementation.embarque;

import com.cumpleanos.importramite.persistence.model.embarques.*;
import com.cumpleanos.importramite.persistence.repository.embarques.FleteValidadoRepository;
import com.cumpleanos.importramite.persistence.repository.embarques.ProcesoCotizacionRepository;
import com.cumpleanos.importramite.utils.enums.EstadoFlete;
import com.cumpleanos.importramite.utils.enums.EstadoProceso;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FleteValidadoService {

    private final FleteValidadoRepository repository;
    private final ProcesoCotizacionRepository procesoRepository;

    //Validar nuevo Flete
    public FleteValidado validarFlete(ProcesoCotizacion proceso, SalidaBuque salida, CotizacionConsignatario consignatario, OpcionFlete opcion, String usuario) {
        List<FleteValidado> vigentes = repository.findByProcesoCotizacionIdAndEstado(proceso.getId(), EstadoFlete.VIGENTE);

        //Anular anteriores
        for (FleteValidado actual : vigentes) {
            actual.setEstado(EstadoFlete.ANULADO);
            actual.setMotivoAnulacion("REEMPLAZADO POR NUEVA OPCION");
            repository.save(actual);
        }

        //Crear Nuevo
        FleteValidado nuevo = new FleteValidado();
        nuevo.setProcesoCotizacionId(proceso.getId());
        nuevo.setSalidaBuqueId(salida.getId());
        nuevo.setConsignatarioId(consignatario.getConsignatarioId());
        nuevo.setNombreConsignatario(consignatario.getNombreConsignatario());
        nuevo.setPuertoEmbarqueNombre(salida.getPuertoEmbarqueNombre());
        nuevo.setPuertoDestino(opcion.getPuertoDestino());
        nuevo.setTipoContenedor(opcion.getTipoContenedor());
        nuevo.setEspacioM3(opcion.getEspacioM3());
        nuevo.setFlete(opcion.getFlete());
        nuevo.setThc(opcion.getThc());
        nuevo.setImo(opcion.getImo());
        nuevo.setGastosBlTotal(opcion.getSubtotalGastos());
        nuevo.setHandlingContenedorTotal(opcion.getHandlingContenedor());
        nuevo.setTotal(opcion.getTotal());
        nuevo.setEstado(EstadoFlete.VIGENTE);
        nuevo.setValidadoPor(usuario);
        nuevo.setFechaValidacion(LocalDateTime.now());
        FleteValidado guardado = repository.save(nuevo);

        // FINALIZAR PROCESO
        proceso.setEstado(EstadoProceso.FINALIZADO);

        procesoRepository.save(proceso);

        return guardado;
    }

    //Anular flete
    public void anularFlete(String fleteId, String motivo) {
        FleteValidado flete = repository.findById(fleteId).orElseThrow();
        flete.setEstado(EstadoFlete.ANULADO);
        flete.setMotivoAnulacion(motivo);
        repository.save(flete);
    }


}