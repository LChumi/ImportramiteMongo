package com.cumpleanos.importramite.presentation.controller.embarque;

import com.cumpleanos.importramite.persistence.model.embarques.ProcesoCotizacion;
import com.cumpleanos.importramite.service.interfaces.embarque.IProcesoCotizacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("proceso-contizacion")
@RequiredArgsConstructor
public class ProcesoCotizacionController {

    private final IProcesoCotizacionService service;

    @PostMapping("/crear")
    public ResponseEntity<ProcesoCotizacion> saveProcesoCotizacion(@RequestBody ProcesoCotizacion p) {
        ProcesoCotizacion pc = service.save(p);
        return ResponseEntity.ok(pc);
    }

    @GetMapping("/list")
    public ResponseEntity<List<ProcesoCotizacion>> listProcesoCotizacion() {
        List<ProcesoCotizacion> pc = service.findAll();
        return ResponseEntity.ok(pc);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProcesoCotizacion> getProcesoCotizacion(@PathVariable String id) {
        ProcesoCotizacion pc = service.findById(id);
        return ResponseEntity.ok(pc);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ProcesoCotizacion> update(@PathVariable String id, @RequestBody ProcesoCotizacion pc){
        return ResponseEntity.ok(service.update(id, pc));
    }
}