package com.cumpleanos.importramite.presentation.controller.embarque;

import com.cumpleanos.importramite.persistence.model.embarques.SalidaBuque;
import com.cumpleanos.importramite.service.interfaces.embarque.ISalidaBuqueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

}