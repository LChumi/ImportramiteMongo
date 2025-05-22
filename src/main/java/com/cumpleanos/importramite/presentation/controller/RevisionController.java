package com.cumpleanos.importramite.presentation.controller;

import com.cumpleanos.importramite.persistence.model.Contenedor;
import com.cumpleanos.importramite.persistence.model.Producto;
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

    @GetMapping("validate/{tramiteId}/{containerId}")
    public ResponseEntity<List<Producto>> validateAndProcessTramite(@PathVariable String tramiteId, @PathVariable String containerId) {
        List<Producto> verified = service.updateRevisionWithTramiteQuantities(tramiteId, containerId);
        return ResponseEntity.ok(verified);
    }

    @PutMapping("/updateQuantity")
    public ResponseEntity<Producto> updateCantidadByBarra(
            @RequestBody RevisionRequest request
    ) {
        Producto updatedRevision = service.updateCantidadByBarra(request);
        return ResponseEntity.ok(updatedRevision);
    }

    @GetMapping("/contenedores/{tramiteId}")
    public ResponseEntity<List<Contenedor>> listContenedoresByTramite(@PathVariable String tramiteId) {
        List<Contenedor> contenedores = service.listContenedoresByTramite(tramiteId);
        return ResponseEntity.ok(contenedores);
    }

    @GetMapping("/productos/{tramiteId}/{contenedorId}")
    public ResponseEntity<List<Producto>> findByTramiteIdAndContenedorId(@PathVariable String tramiteId, @PathVariable String contenedorId) {
        List<Producto> productos = service.findByTramiteId(tramiteId, contenedorId);
        return ResponseEntity.ok(productos);
    }
}
