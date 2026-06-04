package com.cumpleanos.importramite.service.implementation.embarque;

import com.cumpleanos.importramite.persistence.model.embarques.FleteValidado;
import com.cumpleanos.importramite.persistence.model.embarques.TramiteEmbarque;
import com.cumpleanos.importramite.persistence.repository.embarques.FleteValidadoRepository;
import com.cumpleanos.importramite.persistence.repository.embarques.TramiteEmbarqueRepository;
import com.cumpleanos.importramite.utils.enums.EstadoFlete;
import com.cumpleanos.importramite.utils.enums.EstadoTramite;
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

    public List<TramiteEmbarque> findAll(){
        return repository.findAll();
    }

    public void ReemplazarFeleteTramite(String tramiteId, String nuevoFleteId){
        TramiteEmbarque tramite = repository.findById(tramiteId).orElseThrow();

        FleteValidado nuevo = fleteRepository.findById(nuevoFleteId).orElseThrow();

        if (nuevo.getEstado() != EstadoFlete.VIGENTE){
            throw new RuntimeException("Flete no vigente");
        }

        tramite.setFleteValidadoId(nuevo.getId());
        tramite.setActualizadoEn(LocalDateTime.now());
        repository.save(tramite);
    }

    public Boolean existeTramite(String numeoTramite){
        Optional<TramiteEmbarque> found = repository.findByNumeroTramite(numeoTramite);
        return found.isPresent();
    }
}