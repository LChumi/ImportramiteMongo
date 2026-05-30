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

    @GetMapping("/crear-desde-flete/{flete}/{bl}/{proveedor}")
    public ResponseEntity<TramiteEmbarque> crearDesdeFlete(@PathVariable String flete,@PathVariable String bl,@PathVariable String proveedor){
        TramiteEmbarque te = service.crearDesdeFlete(flete,bl,proveedor);
        return ResponseEntity.ok(te);
    }

    @GetMapping("/reemplazar/{tramite}/{fleteNuevo}")
    public ResponseEntity<Void> ReemplazarFeleteTramite(@PathVariable String tramite,@PathVariable String fleteNuevo){
        service.ReemplazarFeleteTramite(tramite,fleteNuevo);
        return ResponseEntity.ok().build();
    }

}