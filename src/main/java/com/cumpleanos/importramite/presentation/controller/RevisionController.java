package com.cumpleanos.importramite.presentation.controller;

import com.cumpleanos.importramite.persistence.model.Revision;
import com.cumpleanos.importramite.persistence.records.RevisionRequest;
import com.cumpleanos.importramite.service.interfaces.IRevisionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("revision")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class RevisionController {

    private final IRevisionService service;

    @GetMapping("/tramiteId/{id}")
    public List<Revision> findByTramite_Id(@PathVariable String id) {
        return service.findByTramite_Id(id.trim());
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Revision>> listar() {
        return ResponseEntity.ok(service.findAll());
    }

    @PostMapping("/save")
    public ResponseEntity<Revision> save(@RequestBody Revision revision) {
        return ResponseEntity.ok(service.save(revision));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Revision> update(@PathVariable String id, @RequestBody Revision revision) {
        Revision found = service.findById(id.trim());
        if (found == null) {
            return ResponseEntity.notFound().build();
        }
        found.setBarra(revision.getBarra());
        found.setCantidad(revision.getCantidad());
        found.setUsuario(revision.getUsuario());
        return ResponseEntity.ok(service.save(found));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Revision> delete(@PathVariable String id) {
        Revision found = service.findById(id.trim());
        if (found == null) {
            return ResponseEntity.notFound().build();
        }
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/updateQuantities/{tramiteId}/{contenedorId}")
    public ResponseEntity<List<Revision>> updateRevisionWithTramiteQuantities(@PathVariable String tramiteId, @PathVariable String contenedorId) {
        List<Revision> revisionList = service.validateAndProcessTramite(tramiteId.trim(), contenedorId.trim());
        return ResponseEntity.ok(revisionList);
    }

    @GetMapping("validate/{tramiteId}")
    public ResponseEntity<List<Revision>> validateAndProcessTramite(@PathVariable String tramiteId) {
        List<Revision> verified = service.updateRevisionWithTramiteQuantities(tramiteId.trim());
        return ResponseEntity.ok(verified);
    }

    @PutMapping("/updateQuantity")
    public ResponseEntity<Revision> updateCantidadByBarra(
            @RequestBody RevisionRequest request
            ) {
        Revision updatedRevision = service.updateCantidadByBarra(request);
        return ResponseEntity.ok(updatedRevision);
    }
}
