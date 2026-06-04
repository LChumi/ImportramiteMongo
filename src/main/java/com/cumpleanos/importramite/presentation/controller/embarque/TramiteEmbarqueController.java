package com.cumpleanos.importramite.presentation.controller.embarque;

import com.cumpleanos.importramite.persistence.model.embarques.TramiteEmbarque;
import com.cumpleanos.importramite.service.implementation.embarque.TramiteEmbarqueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("tramite-embarque")
@RequiredArgsConstructor
public class TramiteEmbarqueController {

    private final TramiteEmbarqueService service;

    @GetMapping("/list")
    public ResponseEntity<List<TramiteEmbarque>> getAll(){
        List<TramiteEmbarque> tes = service.findAll();
        return ResponseEntity.ok(tes);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<TramiteEmbarque> updateTramiteEmbarque(@PathVariable String id, @RequestBody TramiteEmbarque tramiteEmbarque){
        TramiteEmbarque updatedTramiteEmbarque = service.update(id, tramiteEmbarque);
        return ResponseEntity.ok(updatedTramiteEmbarque);
    }

    @GetMapping("/reemplazar/{tramite}/{fleteNuevo}")
    public ResponseEntity<Void> ReemplazarFeleteTramite(@PathVariable String tramite,@PathVariable String fleteNuevo){
        service.ReemplazarFeleteTramite(tramite,fleteNuevo);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/exist/numeroTramite")
    public ResponseEntity<Boolean> existeTramite(@RequestParam String numeroTramite){
        return ResponseEntity.ok(service.existeTramite(numeroTramite));
    }

}