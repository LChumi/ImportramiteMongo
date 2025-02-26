package com.cumpleanos.importramite.presentation.controller;

import com.cumpleanos.importramite.persistence.model.Producto;
import com.cumpleanos.importramite.persistence.model.Tramite;
import com.cumpleanos.importramite.service.interfaces.ITramiteService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

    @GetMapping("/pending")
    public ResponseEntity<List<Tramite>> getPending() {
        List<Tramite> tramites = service.findByEstadoFalse();
        return ResponseEntity.ok(tramites);
    }

    @PostMapping("/save")
    public ResponseEntity<Tramite> save(@RequestBody Tramite tramite) {
        Tramite saved = service.save(tramite);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Tramite> update(@PathVariable String id, @RequestBody Tramite tramite) {
        Tramite tra = service.findById(id);
        if (tra == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(service.save(tra));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        Tramite tra = service.findById(id);
        if (tra == null) {
            return ResponseEntity.notFound().build();
        }
        service.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{tramiteId}/products")
    public ResponseEntity<List<Producto>> getProductos(@PathVariable String tramiteId) {
        List<Producto> productos = service.listByTramite(tramiteId);
        return ResponseEntity.ok(productos);
    }

    @GetMapping("findId/{id}")
    public ResponseEntity<Tramite> findById(@PathVariable String id) {
        Tramite tramite = service.findById(id);
        return ResponseEntity.ok(tramite);
    }

    @GetMapping("lock-unlock/container/{tramite}/{contenedor}/{usr}")
    public ResponseEntity<Tramite> lockUnlockContainer(@PathVariable  String tramite, @PathVariable String contenedor, @PathVariable String usr) {
        Tramite tra = service.findTramiteBloqueaContenedor(tramite, contenedor, usr);
        return ResponseEntity.ok(tra);
    }

}
