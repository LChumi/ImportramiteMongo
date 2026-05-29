package com.cumpleanos.importramite.service.implementation.embarque;

import com.cumpleanos.importramite.persistence.model.embarques.ProcesoCotizacion;
import com.cumpleanos.importramite.persistence.repository.embarques.ProcesoCotizacionRepository;
import com.cumpleanos.importramite.service.implementation.GenericServiceImpl;
import com.cumpleanos.importramite.service.interfaces.embarque.IProcesoCotizacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProcesoCotizacionServiceImp extends GenericServiceImpl<ProcesoCotizacion, String> implements IProcesoCotizacionService {

    private final ProcesoCotizacionRepository repository;


    @Override
    public CrudRepository<ProcesoCotizacion, String> getRepository() {
        return repository;
    }
}
