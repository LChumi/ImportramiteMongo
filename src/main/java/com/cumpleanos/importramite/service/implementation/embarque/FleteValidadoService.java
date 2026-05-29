package com.cumpleanos.importramite.service.implementation.embarque;

import com.cumpleanos.importramite.persistence.model.embarques.CotizacionConsignatario;
import com.cumpleanos.importramite.persistence.model.embarques.FleteValidado;
import com.cumpleanos.importramite.persistence.model.embarques.OpcionFlete;
import com.cumpleanos.importramite.persistence.model.embarques.SalidaBuque;
import com.cumpleanos.importramite.persistence.repository.embarques.FleteValidadoRepository;
import com.cumpleanos.importramite.utils.enums.EstadoFlete;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FleteValidadoService {

    private final FleteValidadoRepository repository;

    public FleteValidado validar (SalidaBuque salida, CotizacionConsignatario consignatario, OpcionFlete opcion, String usuario){
        FleteValidado entity = new FleteValidado();

        entity.setSalidaBuqueId(salida.getId());
        entity.setConsignatarioId(consignatario.getConsignatarioId());
        entity.setNombreConsignatario(consignatario.getNombreConsignatario());
        entity.setPuertoEmbarqueNombre(salida.getPuertoEmbarqueNombre());
        entity.setPuertoDestino(opcion.getPuertoDestino());
        entity.setTipoContenedor(opcion.getTipoContenedor());
        entity.setEspacioM3(opcion.getEspacioM3());

        entity.setFlete(opcion.getFlete());
        entity.setThc(opcion.getThc());
        entity.setImo(opcion.getImo());

        entity.setGastosBlTotal(opcion.getSubtotalGastos());
        entity.setHandlingContenedorTotal(opcion.getHandlingContenedor());

        entity.setTotal(opcion.getTotal());
        entity.setEstado(EstadoFlete.VIGENTE);
        entity.setValidadoPor(usuario);

        entity.setFechaValidacion(LocalDateTime.now());

        return repository.save(entity);
    }
}