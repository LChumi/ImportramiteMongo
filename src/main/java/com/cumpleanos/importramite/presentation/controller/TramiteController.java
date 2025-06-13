package com.cumpleanos.importramite.presentation.controller;

import com.cumpleanos.importramite.persistence.model.Contenedor;
import com.cumpleanos.importramite.persistence.model.Producto;
import com.cumpleanos.importramite.persistence.model.Tramite;
import com.cumpleanos.importramite.persistence.records.StatusResponse;
import com.cumpleanos.importramite.service.interfaces.ITramiteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@CrossOrigin("*")
@RestController
@RequestMapping("tramite")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class TramiteController {

    private final ITramiteService service;

    @GetMapping("/All")
    public ResponseEntity<List<Tramite>> getAll() {
        List<Tramite> tramites = service.findAll();
        return ResponseEntity.ok(tramites);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Tramite>> getStatus(@PathVariable Short status) {
        List<Tramite> tramites = service.findByProceso(status);
        return ResponseEntity.ok(tramites);
    }

    @GetMapping("/{tramiteId}/products")
    public ResponseEntity<List<Producto>> getProductos(@PathVariable String tramiteId) {
        List<Producto> productos = service.listByTramite(tramiteId.trim());
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/findId/{id}")
    public ResponseEntity<Tramite> findById(@PathVariable String id) {
        Tramite tramite = service.findById(id.trim());
        return ResponseEntity.ok(tramite);
    }

    @GetMapping("/lock-unlock/container/{tramite}/{contenedor}/{usr}")
    public ResponseEntity<StatusResponse> lockUnlockContainer(@PathVariable String tramite, @PathVariable String contenedor, @PathVariable String usr) {
        StatusResponse tra = service.findTramiteBloqueaContenedor(tramite.trim(), contenedor.trim(), usr.trim());
        return ResponseEntity.ok(tra);
    }

    @GetMapping("/filtros")
    public ResponseEntity<List<Tramite>> filtros(
            @RequestParam(required = false) String id,
            @RequestParam(required = false) Short estado,
            @RequestParam(required = false) LocalDate fechaInicio,
            @RequestParam(required = false) LocalDate fechaFin) {

        List<Tramite> tramites = service.buscarTramites(id, estado, fechaInicio, fechaFin);
        return ResponseEntity.ok(tramites);
    }

    @GetMapping("/update/date")
    public ResponseEntity<StatusResponse> updateDates(@RequestParam LocalDate fechaArribo, @RequestParam LocalTime horaArribo, @RequestParam String id) {
        StatusResponse status = service.updateDateAndSendEmails(id.trim(), fechaArribo, horaArribo);
        return ResponseEntity.ok(status);
    }

    @GetMapping("/contenedor/{tramite}/{contenedor}")
    public ResponseEntity<Contenedor> getContenedor(@PathVariable String tramite, @PathVariable String contenedor) {
        Contenedor c = service.findByTramiteAndId(tramite.trim(), contenedor.trim());
        return ResponseEntity.ok(c);
    }

     @GetMapping("/productos/{tramite}/{contenedor}")
    public ResponseEntity<List<Producto>> getProductosByTramiteAndContenedor(@PathVariable String tramite, @PathVariable String contenedor) {
        List<Producto> productos = service.findByTramiteAndContenedor(tramite.trim(), contenedor.trim());
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/week")
    public ResponseEntity<List<Tramite>> getTramitesOfTheWeek() {
        List<Tramite> tramites = service.getTramitesOfTheWeek();
        return ResponseEntity.ok(tramites);
    }

    @GetMapping("/total/package/{tramite}/{contenedor}")
    public ResponseEntity<Integer> getTotalPackage(@PathVariable String tramite, @PathVariable String contenedor) {
        Integer total = service.getTotal(tramite,contenedor);
        return ResponseEntity.ok(total);
    }

    @GetMapping("/percentage/package/{tramite}/{contenedor}")
    public ResponseEntity<Double> getPercentage(@PathVariable String tramite, @PathVariable String contenedor) {
        Double total = service.getPercentage(tramite,contenedor);
        return ResponseEntity.ok(total);
    }

    @GetMapping("/month")
    public ResponseEntity<List<Tramite>> getTramitesOfTheMonth() {
        List<Tramite> tramites = service.getTramitesOfTheMonth();
        return ResponseEntity.ok(tramites);
    }

}