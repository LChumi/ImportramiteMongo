package com.cumpleanos.importramite.service.implementation;

import com.cumpleanos.importramite.persistence.model.Contenedor;
import com.cumpleanos.importramite.persistence.model.Producto;
import com.cumpleanos.importramite.persistence.model.Tramite;
import com.cumpleanos.importramite.persistence.records.StatusResponse;
import com.cumpleanos.importramite.persistence.repository.ContenedorRepository;
import com.cumpleanos.importramite.persistence.repository.ProductoRepository;
import com.cumpleanos.importramite.persistence.repository.TramiteRepository;
import com.cumpleanos.importramite.persistence.repository.TramiteRepositoryCustom;
import com.cumpleanos.importramite.service.exception.DocumentNotFoundException;
import com.cumpleanos.importramite.service.interfaces.ITramiteService;
import com.cumpleanos.importramite.utils.MapUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class TramiteServiceImpl extends GenericServiceImpl<Tramite, String> implements ITramiteService {

    private final TramiteRepository repository;
    private final TramiteRepositoryCustom repositoryCustom;
    private final FileServiceImpl fileService;
    private final ContenedorRepository contenedorRepository;
    private final ProductoRepository productoRepository;

    @Override
    public CrudRepository<Tramite, String> getRepository() {
        return repository;
    }

    @Override
    public List<Tramite> findByProceso(Short proceso) {
        return repository.findByProceso(proceso);
    }

    @Override
    public List<Producto> listByTramite(String tramite) {

        List<Contenedor> contenedores = contenedorRepository.findByTramiteId(tramite).orElseThrow(() -> new DocumentNotFoundException("Tramite " + tramite + " not found"));


        Map<String, Producto> productosMap = MapUtils.listByTramite(contenedores, productoRepository);


        return productosMap.values().stream()
                .sorted(Comparator.comparingInt(Producto::getSecuencia))
                .collect(Collectors.toList());
    }

    @Override
    public StatusResponse findTramiteBloqueaContenedor(String tramite, String contenedor, String usr) {
        StatusResponse response = null;
        List<Contenedor> contenedores = contenedorRepository.findByTramiteId(tramite).orElseThrow(() -> new DocumentNotFoundException("Tramite " + tramite + " not found"));
        for (Contenedor cont : contenedores) {
            if (cont.getId().equals(contenedor)) {
                response = lockUnlockContenedor(cont, usr);
                contenedorRepository.save(cont);
                break;
            }
        }
        return response;
    }

    @Override
    public List<Tramite> buscarTramites(String id, Short estado, LocalDate fechaInicio, LocalDate fechaFin) {
        return repositoryCustom.buscarTramites(id, estado, fechaInicio, fechaFin);
    }

    @Override
    public StatusResponse updateDateAndSendEmails(String id, LocalDate fechaLlegada, LocalTime horaLlegada) {
        String response;
        Tramite found = repository.findById(id).orElseThrow(() -> new DocumentNotFoundException("No se encontraron datos a actualizar"));
        found.setFechaArribo(fechaLlegada);
        found.setHoraArribo(horaLlegada);
        Tramite saved = repository.save(found);
        if (saved.getFechaArribo() == null) {
            throw new DocumentNotFoundException("El documento no tiene fecha de arribo");
        }
        response = fileService.sendTramiteFinal(saved.getId());
        return new StatusResponse(response, true);
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
