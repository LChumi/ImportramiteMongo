package com.cumpleanos.importramite.presentation.controller;

import com.cumpleanos.importramite.persistence.model.Muestra;
import com.cumpleanos.importramite.persistence.model.Producto;
import com.cumpleanos.importramite.persistence.records.MuestraRequest;
import com.cumpleanos.importramite.service.interfaces.IMuestraService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("muestra")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class MuestraController {

    private final IMuestraService service;

    @PutMapping("/add/compare/")
    public ResponseEntity<Producto> compare(@RequestBody MuestraRequest request) {
        Producto mr = service.saveAndCompare(request);
        return ResponseEntity.ok(mr);
    }

    @GetMapping("/validate/{tramite}/{containerId}")
    public ResponseEntity<List<Producto>> validate(@PathVariable String tramite, @PathVariable String containerId) {
        List<Producto> muestras = service.updateWithRevision(tramite, containerId);
        return ResponseEntity.ok(muestras);
    }

}
