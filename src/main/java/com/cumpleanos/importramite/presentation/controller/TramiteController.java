package com.cumpleanos.importramite.presentation.controller;

import com.cumpleanos.importramite.persistence.model.Tramite;
import com.cumpleanos.importramite.service.interfaces.ITramiteService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("mongo")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class TramiteController {

    private final ITramiteService service;

    @GetMapping("/tramite")
    public ResponseEntity<List<Tramite>> getAll() {
        List<Tramite> tramites = service.findAll();
        return ResponseEntity.ok(tramites);
    }

    @PostMapping("/tramite/save")
    public ResponseEntity<Tramite> save(@RequestBody Tramite tramite) {
        Tramite saved = service.save(tramite);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/tramite/update/{id}")
    public ResponseEntity<Tramite> update(@PathVariable String id, @RequestBody Tramite tramite) {
        Tramite tra = service.findById(id);
        if (tra == null) {
            return ResponseEntity.notFound().build();
        }
        tra.setObservacion(tramite.getObservacion());
        tra.setListProductos(tramite.getListProductos());
        return ResponseEntity.ok(service.save(tra));
    }

    @DeleteMapping("/tramite/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        Tramite tra = service.findById(id);
        if (tra == null) {
            return ResponseEntity.notFound().build();
        }
        service.delete(id);
        return ResponseEntity.ok().build();
    }

}
