package com.cumpleanos.importramite.service.implementation;

import com.cumpleanos.importramite.persistence.model.Contenedor;
import com.cumpleanos.importramite.persistence.model.Producto;
import com.cumpleanos.importramite.persistence.model.Tramite;
import com.cumpleanos.importramite.persistence.records.StatusResponse;
import com.cumpleanos.importramite.persistence.repository.TramiteRepository;
import com.cumpleanos.importramite.service.exception.DocumentNotFoundException;
import com.cumpleanos.importramite.service.interfaces.ITramiteService;
import com.cumpleanos.importramite.utils.MapUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.*;
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
    public List<Tramite> findByEstadoTrue() {
        return repository.findByEstadoTrue();
    }

    @Override
    public List<Producto> listByTramite(String tramite) {
        Tramite tr = repository.findById(tramite).orElseThrow(() -> new DocumentNotFoundException("Tramite " + tramite + " no encontrado"));

        Map<String, Producto> productosMap = MapUtils.listByTramite(tr);


        return productosMap.values().stream()
                .sorted(Comparator.comparingInt(Producto::getSecuencia))
                .collect(Collectors.toList());
    }

    @Override
    public StatusResponse findTramiteBloqueaContenedor(String tramite, String contenedor, String usr) {
        Tramite tr = repository.findById(tramite).orElseThrow(() -> new DocumentNotFoundException("Tramite " + tramite + " no encontrado"));
        StatusResponse response = null;
        for (Contenedor cont : tr.getContenedores()) {
            if (cont.getId().equals(contenedor)) {
                response =lockUnlockContenedor(cont, usr);
                break;
            }
        }
        repository.save(tr);
        return response;
    }

    private StatusResponse lockUnlockContenedor(Contenedor cont, String usr) {
        if (cont.getUsrBloquea() == null) {
            cont.setUsrBloquea("");
        }
        if (!cont.getBloqueado() && cont.getUsrBloquea().isEmpty()) {
            cont.setUsrBloquea(usr);
            cont.setBloqueado(true);
            return new StatusResponse("bloqueado", true);
        } else if (cont.getBloqueado() && cont.getUsrBloquea().equals(usr)) {
            cont.setUsrBloquea(null);
            cont.setBloqueado(false);
            return new StatusResponse("desbloqueado", true);
        } else {
            return new StatusResponse("error", false);
        }
    }
}
