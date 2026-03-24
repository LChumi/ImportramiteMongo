package com.cumpleanos.importramite.service.implementation.confiteria;

import com.cumpleanos.importramite.persistence.model.confiteria.ConfiteriaDetalle;
import com.cumpleanos.importramite.persistence.repository.confiteria.ConfiteriaDetalleRepository;
import com.cumpleanos.importramite.service.implementation.GenericServiceImpl;
import com.cumpleanos.importramite.service.interfaces.confiteria.IConfiteriaDetalleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConfiteriaDetallaServiceImpl extends GenericServiceImpl<ConfiteriaDetalle, String> implements IConfiteriaDetalleService {

    private final ConfiteriaDetalleRepository repository;

    @Override
    public CrudRepository<ConfiteriaDetalle, String> getRepository() {
        return repository;
    }

    @Override
    public List<ConfiteriaDetalle> findByReposicionId(String reposisicionId) {
        List<ConfiteriaDetalle> detalle = repository.findByReposicionId(reposisicionId);
        return detalle;
    }

    @Override
    public List<ConfiteriaDetalle> saveList(List<ConfiteriaDetalle> confiteriaDetalles, String reposicionId) {
        for (ConfiteriaDetalle item :  confiteriaDetalles) {
            item.setReposicionId(reposicionId);
            repository.save(item);
        }
        return confiteriaDetalles;
    }
}