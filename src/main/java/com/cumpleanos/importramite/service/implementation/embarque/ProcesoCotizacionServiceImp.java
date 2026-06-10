package com.cumpleanos.importramite.service.implementation.embarque;

import com.cumpleanos.importramite.persistence.model.embarques.ProcesoCotizacion;
import com.cumpleanos.importramite.persistence.repository.embarques.ProcesoCotizacionRepository;
import com.cumpleanos.importramite.service.implementation.GenericServiceImpl;
import com.cumpleanos.importramite.service.interfaces.embarque.IProcesoCotizacionService;
import com.cumpleanos.importramite.utils.enums.EstadoProceso;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProcesoCotizacionServiceImp extends GenericServiceImpl<ProcesoCotizacion, String> implements IProcesoCotizacionService {

    private final ProcesoCotizacionRepository repository;


    @Override
    public CrudRepository<ProcesoCotizacion, String> getRepository() {
        return repository;
    }

    @Override
    public ProcesoCotizacion save(ProcesoCotizacion p) {
        p.setCreadoEn(LocalDateTime.now());
        p.setEstado(EstadoProceso.EN_VALIDACION);
        return super.save(p);
    }

    @Override
    public List<ProcesoCotizacion> findAll() {
        return repository.findAllByOrderByCreadoEn();
    }
}