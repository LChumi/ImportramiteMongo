package com.cumpleanos.importramite.presentation.controller.embarque;

import com.cumpleanos.importramite.persistence.model.embarques.SalidaBuque;
import com.cumpleanos.importramite.service.interfaces.embarque.ISalidaBuqueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("salida-buque")
@RequiredArgsConstructor
public class SalidaBuqueController {

    private final ISalidaBuqueService service;

    @PostMapping("/save")
    public ResponseEntity<SalidaBuque> saveSalidaBuque(@RequestBody SalidaBuque salidaBuque){
        SalidaBuque sb = service.save(salidaBuque);
        return ResponseEntity.ok(sb);
    }

    @GetMapping("/list")
    public ResponseEntity<List<SalidaBuque>> listSalidaBuque(){
        List<SalidaBuque> sb = service.findAll();
        return ResponseEntity.ok(sb);
    }

    @GetMapping("/list-by-cotizacion/{idCotizacion}")
    public ResponseEntity<List<SalidaBuque>> listSalidaBuqueByCotizacion(@PathVariable String idCotizacion){
        List<SalidaBuque> sbs = service.getByProcesoCotizacionId(idCotizacion);
        return ResponseEntity.ok(sbs);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<SalidaBuque> updateBuque (@PathVariable String id, @RequestBody SalidaBuque buque){
        SalidaBuque updatedBuque = service.update(id, buque);
        return ResponseEntity.ok(updatedBuque);
    }

}