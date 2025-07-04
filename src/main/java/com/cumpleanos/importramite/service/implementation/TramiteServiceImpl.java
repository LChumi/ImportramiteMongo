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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
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
        List<Contenedor> contenedores = contenedorRepository.findByTramiteId(tramite)
                .orElseThrow(() -> new DocumentNotFoundException("Trámite " + tramite + " no encontrado"));

        for (Contenedor cont : contenedores) {
            if (cont.getContenedorId().equals(contenedor)) {
                return lockUnlockContenedor(cont, usr);
            }
        }

        return new StatusResponse("Contenedor no encontrado en el trámite.", false);
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

    @Override
    public Contenedor findByTramiteAndId(String tramite, String id) {
        return contenedorRepository.findByTramiteIdAndContenedorId(tramite, id).orElseThrow(() -> new DocumentNotFoundException("No se encontraron datos de contenedores"));
    }

    @Override
    public List<Producto> findByTramiteAndContenedor(String tramite, String contenedor) {
        return productoRepository.findByTramiteIdAndContenedorId(tramite, contenedor).orElseThrow(() -> new DocumentNotFoundException("No se encontraron datos de productos"));
    }

    @Override
    public List<Tramite> getTramitesOfTheWeek() {
        LocalDate inicioSemana = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate finSemana = LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        return repository.findByFechaArriboBetween(inicioSemana.minusDays(1), finSemana.plusDays(1));
    }

    @Override
    public List<Tramite> getTramitesOfTheMonth() {
        LocalDate inicioMes = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
        LocalDate finMes = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());

        return repository.findByFechaArriboBetween(inicioMes.minusDays(1), finMes.plusDays(1));
    }

    @Override
    public Integer getTotal(String tramite, String contenedor) {
        List<Producto> productos = productoRepository.findByTramiteIdAndContenedorId(tramite, contenedor).orElseThrow(() ->
                new DocumentNotFoundException("No se encontraron datos de productos en el Tramite: " + tramite));

        return productos.stream()
                .filter(p -> p.getBultos() != null)
                .mapToInt(Producto::getBultos)
                .sum();
    }

    @Override
    public Double getPercentage(String tramite, String contenedor) {
        return productoRepository.findByTramiteIdAndContenedorId(tramite, contenedor)
                .map(productos -> {
                    int totalBultos = productos.stream()
                            .filter(p -> p.getBultos() != null)
                            .mapToInt(Producto::getBultos)
                            .sum();

                    int totalRevision = productos.stream()
                            .filter(p -> p.getCantidadRevision() != null)
                            .mapToInt(Producto::getCantidadRevision)
                            .sum();

                    return (totalBultos > 0) ? (totalRevision / (double) totalBultos) * 100 : 0;
                })
                .orElse((double) 0);
    }

    private StatusResponse lockUnlockContenedor(Contenedor cont, String usr) {
        if (cont.getFinalizado()) {
            return new StatusResponse("finalizado.", true);
        }

        if (cont.getUsrBloquea() == null || cont.getUsrBloquea().isEmpty()) {
            if (!cont.getBloqueado()) {
                cont.setUsrBloquea(usr);
                cont.setBloqueado(true);
                contenedorRepository.save(cont);
                return new StatusResponse("bloqueado", true);
            }
        } else if (cont.getBloqueado() && cont.getUsrBloquea().equals(usr)) {
            cont.setUsrBloquea(null);
            cont.setBloqueado(false);
            contenedorRepository.save(cont);
            return new StatusResponse("desbloqueado", true);
        }

        return new StatusResponse("Error: usuario no autorizado para desbloquear este contenedor.", false);
    }

}
