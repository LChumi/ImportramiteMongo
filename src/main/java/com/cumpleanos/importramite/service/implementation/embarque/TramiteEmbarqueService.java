package com.cumpleanos.importramite.service.implementation.embarque;

import com.cumpleanos.importramite.persistence.model.embarques.FleteValidado;
import com.cumpleanos.importramite.persistence.model.embarques.TramiteEmbarque;
import com.cumpleanos.importramite.persistence.repository.embarques.FleteValidadoRepository;
import com.cumpleanos.importramite.persistence.repository.embarques.TramiteEmbarqueRepository;
import com.cumpleanos.importramite.service.exception.DocumentNotFoundException;
import com.cumpleanos.importramite.utils.enums.EstadoFlete;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TramiteEmbarqueService {

    private final TramiteEmbarqueRepository repository;
    private final FleteValidadoRepository fleteRepository;

    public List<TramiteEmbarque> findAll() {
        return repository.findAllByOrderByFechaEmbarqueDesc();
    }

    public void ReemplazarFleteTramite(String tramiteId, String nuevoFleteId) {
        TramiteEmbarque tramite = repository.findById(tramiteId).orElseThrow();

        FleteValidado nuevo = fleteRepository.findById(nuevoFleteId).orElseThrow();

        if (nuevo.getEstado() != EstadoFlete.VIGENTE) {
            throw new RuntimeException("Flete no vigente");
        }

        tramite.setFleteValidadoId(nuevo.getId());
        tramite.setActualizadoEn(LocalDateTime.now());
        repository.save(tramite);
    }

    public TramiteEmbarque update(String id, TramiteEmbarque t) {
        TramiteEmbarque found = repository.findById(id).orElseThrow(() -> new DocumentNotFoundException("No existe Tramite registrado"));
        found.setProveedorId(t.getProveedorId());
        found.setNumeroBl(t.getNumeroBl());
        found.setFechaEmbarque(t.getFechaEmbarque());
        found.setFechaArribo(t.getFechaArribo());
        found.setDiasLibres(t.getDiasLibres());
        found.setPuertoSalida(t.getPuertoSalida());
        found.setPuertoLlegada(t.getPuertoLlegada());
        found.setActualizadoEn(LocalDateTime.now());
        found.setEstado(t.getEstado());
        found.setSolicitudNEcuapass(t.getSolicitudNEcuapass());
        found.setFechaSolicitudEcuapass(t.getFechaSolicitudEcuapass());
        found.setIdentificar(t.getIdentificar());
        found.setSolicitudNIntertek(t.getSolicitudNIntertek());
        found.setPreLiquidacion(t.getPreLiquidacion());
        found.setPolizaNChub(t.getPolizaNChub());
        found.setCertificadoOrigen(t.getCertificadoOrigen());
        return repository.save(found);
    }

    public Boolean existeTramite(String numeoTramite) {
        Optional<TramiteEmbarque> found = repository.findByNumeroTramite(numeoTramite);
        return found.isPresent();
    }
}