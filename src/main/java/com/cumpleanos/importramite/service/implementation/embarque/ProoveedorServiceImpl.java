package com.cumpleanos.importramite.service.implementation.embarque;

import com.cumpleanos.importramite.persistence.model.embarques.Proveedor;
import com.cumpleanos.importramite.persistence.repository.embarques.ProveedorRepository;
import com.cumpleanos.importramite.service.implementation.GenericServiceImpl;
import com.cumpleanos.importramite.service.interfaces.embarque.IProveedorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProoveedorServiceImpl extends GenericServiceImpl<Proveedor, String> implements IProveedorService {

    private final ProveedorRepository repository;


    @Override
    public CrudRepository<Proveedor, String> getRepository() {
        return repository;
    }
}