package com.cumpleanos.importramite.presentation.controller.embarque;

import com.cumpleanos.importramite.persistence.model.embarques.Consignatario;
import com.cumpleanos.importramite.service.interfaces.embarque.IConsignatarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("consignatario")
@RequiredArgsConstructor
public class ConsignatarioController {

    private final IConsignatarioService service;

    @PostMapping("/save")
    public ResponseEntity<Consignatario> saveConsignatario(@RequestBody Consignatario consignatario){
        Consignatario c = service.save(consignatario);
        return ResponseEntity.ok(c);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Consignatario>> listConsignatarios(){
        List<Consignatario> cs = service.findAll();
        return ResponseEntity.ok(cs);
    }

}