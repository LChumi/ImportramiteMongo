package com.cumpleanos.importramite.presentation.controller;

import com.cumpleanos.importramite.persistence.model.Muestra;
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

    @GetMapping("/list/{tramiteId}")
    public ResponseEntity<List<Muestra>> getAll(@PathVariable String tramiteId) {
        List<Muestra> muestras = service.findByRevision_Tramite_Id(tramiteId.trim());
        return ResponseEntity.ok(muestras);
    }

    @GetMapping("/add/compare/{barra}/{muestra}/{tramite}/{status}")
    public ResponseEntity<Muestra> compare(
            @PathVariable String barra,
            @PathVariable String muestra,
            @PathVariable String tramite,
            @PathVariable Boolean status) {
        Muestra mr = service.saveAndCompare(barra.trim(), muestra.trim(), tramite.trim(), status);
        return ResponseEntity.ok(mr);
    }

    @GetMapping("/validate/{tramite}")
    public ResponseEntity<List<Muestra>> validate(@PathVariable String tramite) {
        List<Muestra> muestras = service.updateWithRevision(tramite.trim());
        return ResponseEntity.ok(muestras);
    }

}
