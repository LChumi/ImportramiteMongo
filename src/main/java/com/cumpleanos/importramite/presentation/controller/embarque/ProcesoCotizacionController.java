package com.cumpleanos.importramite.presentation.controller.embarque;

import com.cumpleanos.importramite.persistence.model.embarques.ProcesoCotizacion;
import com.cumpleanos.importramite.service.interfaces.embarque.IProcesoCotizacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("proceso-contizacion")
@RequiredArgsConstructor
public class ProcesoCotizacionController {

    private final IProcesoCotizacionService service;

    @PostMapping("/crear")
    public ResponseEntity<ProcesoCotizacion> saveProcesoCotizacion(@RequestBody ProcesoCotizacion procesoCotizacion) {
        ProcesoCotizacion pc = service.save(procesoCotizacion);
        return ResponseEntity.ok(pc);
    }
}