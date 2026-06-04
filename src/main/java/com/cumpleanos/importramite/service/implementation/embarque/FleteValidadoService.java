package com.cumpleanos.importramite.service.implementation.embarque;

import com.cumpleanos.importramite.persistence.model.embarques.*;
import com.cumpleanos.importramite.persistence.repository.embarques.FleteValidadoRepository;
import com.cumpleanos.importramite.persistence.repository.embarques.ProcesoCotizacionRepository;
import com.cumpleanos.importramite.persistence.repository.embarques.TramiteEmbarqueRepository;
import com.cumpleanos.importramite.utils.enums.EstadoFlete;
import com.cumpleanos.importramite.utils.enums.EstadoProceso;
import com.cumpleanos.importramite.utils.enums.EstadoTramite;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FleteValidadoService {

    private final FleteValidadoRepository repository;
    private final ProcesoCotizacionRepository procesoRepository;
    private final TramiteEmbarqueRepository tramiteRepository;

    //Validar nuevo Flete
    public FleteValidado validarFlete(FleteValidacionRequest r) {
        List<FleteValidado> vigentes = repository.findByProcesoCotizacionIdAndEstado(r.proceso().getId(), EstadoFlete.VIGENTE);

        //Anular anteriores
        for (FleteValidado actual : vigentes) {
            actual.setEstado(EstadoFlete.ANULADO);
            actual.setMotivoAnulacion("REEMPLAZADO POR NUEVA OPCION");
            repository.save(actual);
        }

        //Crear Nuevo
        FleteValidado nuevo = new FleteValidado();
        nuevo.setProcesoCotizacionId(r.proceso().getId());
        nuevo.setSalidaBuqueId(r.salida().getId());
        nuevo.setNombreConsignatario(r.consignatario());
        nuevo.setPuertoEmbarqueNombre(r.salida().getPuertoEmbarqueNombre());
        nuevo.setPuertoDestino(r.opcion().getPuertoDestino());
        nuevo.setTipoContenedor(r.opcion().getTipoContenedor());
        nuevo.setEspacioM3(r.opcion().getEspacioM3());
        nuevo.setFlete(r.opcion().getFlete());
        nuevo.setThc(r.opcion().getThc());
        nuevo.setImo(r.opcion().getImo());
        nuevo.setGastosBlTotal(r.opcion().getSubtotalGastos());
        nuevo.setHandlingContenedorTotal(r.opcion().getHandlingContenedor());
        nuevo.setTotal(r.opcion().getTotal());
        nuevo.setEstado(EstadoFlete.VIGENTE);
        nuevo.setValidadoPor(r.usuario());
        nuevo.setFechaValidacion(LocalDateTime.now());
        FleteValidado guardado = repository.save(nuevo);

        // FINALIZAR PROCESO
        r.proceso().setEstado(EstadoProceso.FINALIZADO);

        procesoRepository.save(r.proceso());

        crearTramite(guardado, r.proceso(), r.salida());
        return guardado;
    }

    //Anular flete
    public void anularFlete(FleteAnularRequest r) {
        FleteValidado flete = repository.findById(r.fleteId()).orElseThrow();
        flete.setEstado(EstadoFlete.ANULADO);
        flete.setMotivoAnulacion(r.motivo());
        repository.save(flete);
    }

    private void crearTramite(FleteValidado f, ProcesoCotizacion p, SalidaBuque b){
        TramiteEmbarque t = new TramiteEmbarque();
        t.setOrdenLlegada("AAA");
        t.setEmpresaId(p.getEmpresaId());
        t.setNumeroTramite(p.getNumeroReferencia());
        t.setProveedorId(p.getProveedorId());
        t.setNumeroBl(f.getNumeroBl());
        t.setFleteValidadoId(f.getId());

        t.setFechaEmbarque(b.getFechaDesde());
        t.setFechaArribo(b.getFechaHasta());
        t.setDiasLibres(b.getDiasLibres());
        t.setPuertoSalida(f.getPuertoEmbarqueNombre());
        t.setPuertoLlegada(f.getPuertoDestino());

        t.setCreadoEn(LocalDateTime.now());
        t.setEstado(EstadoTramite.EMBARCADO);
    }

}