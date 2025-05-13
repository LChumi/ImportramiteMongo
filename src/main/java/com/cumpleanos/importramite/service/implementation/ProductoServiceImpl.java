package com.cumpleanos.importramite.service.implementation;

import com.cumpleanos.importramite.persistence.model.Producto;
import com.cumpleanos.importramite.persistence.repository.ProductoRepository;
import com.cumpleanos.importramite.service.exception.DocumentNotFoundException;
import com.cumpleanos.importramite.service.interfaces.IProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ProductoServiceImpl extends GenericServiceImpl<Producto, String> implements IProductoService {

    private final ProductoRepository repository;

    @Override
    public CrudRepository<Producto, String> getRepository() {
        return repository;
    }

    @Override
    public List<Producto> findByTramiteIdAndContenedorId(String tramiteStr, String contenedorStr) {
        String tramiteId = tramiteStr.trim();
        String contenedorId = contenedorStr.trim();

        return repository.findByTramiteIdAndContenedorIdOrderBySecuencia(tramiteId, contenedorId).orElseThrow(() -> new DocumentNotFoundException("No se encontraron productos para el trámite: " + tramiteId + " y el contenedor: " + contenedorId));
    }

    @Override
    public Producto findByBarcodeAndTramiteIdAndContenedorId(String barcode, String tramiteId, String contenedorId) {
        return repository.findByBarcodeAndTramiteIdAndContenedorId(barcode, tramiteId, contenedorId).orElse(new Producto());
    }
}
