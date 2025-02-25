package com.cumpleanos.importramite.service.implementation;

import com.cumpleanos.importramite.persistence.model.Contenedor;
import com.cumpleanos.importramite.persistence.model.Producto;
import com.cumpleanos.importramite.persistence.model.Tramite;
import com.cumpleanos.importramite.persistence.repository.TramiteRepository;
import com.cumpleanos.importramite.service.exception.DocumentNotFoundException;
import com.cumpleanos.importramite.service.interfaces.ITramiteService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ =  {@Autowired})
public class TramiteServiceImpl extends GenericServiceImpl<Tramite, String> implements ITramiteService {

    private final TramiteRepository repository;

    @Override
    public CrudRepository<Tramite, String> getRepository() {
        return repository;
    }

    @Override
    public List<Tramite> findByEstadoFalse() {
        return repository.findByEstadoFalse();
    }

    @Override
    public List<Producto> listByTramite(String tramite) {
        Tramite tr = repository.findById(tramite).orElseThrow(() -> new DocumentNotFoundException("Tramite " + tramite + " no encontrado"));

        List<Producto> productos = new ArrayList<>();

        for (Contenedor contenedor : tr.getContenedor()) {
            productos.addAll(contenedor.getProductos());
        }

        return productos.stream()
                .sorted(Comparator.comparingInt(Producto::getSecuencia))
                .collect(Collectors.toList());
    }
}
