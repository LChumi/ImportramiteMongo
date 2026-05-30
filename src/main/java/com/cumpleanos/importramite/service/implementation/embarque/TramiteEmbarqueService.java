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

@Service
@RequiredArgsConstructor
public class TramiteEmbarqueService {

    private final TramiteEmbarqueRepository repository;
    private final FleteValidadoRepository fleteRepository;

    public List<TramiteEmbarque> findAll(){
        return repository.findAll();
    }

    //CrearTramite desde flete Validado
    public TramiteEmbarque crearDesdeFlete(String fleteValidadoId, String numeroBl, String proveedorId){
        FleteValidado flete = fleteRepository.findById(fleteValidadoId).orElseThrow();

        if (flete.getEstado() != EstadoFlete.VIGENTE){
            throw new RuntimeException("Flete no vigente");
        }

        TramiteEmbarque tramite = new TramiteEmbarque();

        tramite.setNumeroBl(numeroBl);

        tramite.setProveedorId(proveedorId);

        tramite.setFleteValidadoId(
                flete.getId());

        tramite.setPuertoSalida(
                flete.getPuertoEmbarqueNombre());

        tramite.setPuertoLlegada(
                flete.getPuertoDestino());

        tramite.setDiasLibres(
                0);

        tramite.setEstado(
                EstadoTramite.BORRADOR);

        tramite.setCreadoEn(
                LocalDateTime.now());

        return repository.save(tramite);
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
}