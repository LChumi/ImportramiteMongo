package com.cumpleanos.importramite.service.implementation;

import com.cumpleanos.importramite.persistence.model.Producto;
import com.cumpleanos.importramite.persistence.repository.ProductoRepository;
import com.cumpleanos.importramite.service.interfaces.IProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ProductoServiceImpl extends GenericServiceImpl<Producto, String> implements IProductoService {

    private final ProductoRepository repository;

    @Override
    public CrudRepository<Producto, String> getRepository() {
        return repository;
    }
}
