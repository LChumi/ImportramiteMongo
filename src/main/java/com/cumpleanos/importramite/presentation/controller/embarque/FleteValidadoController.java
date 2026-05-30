package com.cumpleanos.importramite.presentation.controller.embarque;

import com.cumpleanos.importramite.persistence.model.embarques.FleteAnularRequest;
import com.cumpleanos.importramite.persistence.model.embarques.FleteValidacionRequest;
import com.cumpleanos.importramite.persistence.model.embarques.FleteValidado;
import com.cumpleanos.importramite.service.implementation.embarque.FleteValidadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("flete")
@RequiredArgsConstructor
public class FleteValidadoController {

    private final FleteValidadoService service;

    @PostMapping("/save")
    ResponseEntity<FleteValidado> saveFleteValidado(@RequestBody FleteValidacionRequest request){
        FleteValidado fl = service.validarFlete(request);
        return ResponseEntity.ok(fl);
    }

    @PutMapping("/anular")
    ResponseEntity<Void> anularFlete(@RequestBody FleteAnularRequest r ){
        service.anularFlete(r);
        return ResponseEntity.ok().build();
    }

}