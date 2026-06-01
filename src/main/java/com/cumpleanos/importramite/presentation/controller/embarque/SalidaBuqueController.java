package com.cumpleanos.importramite.presentation.controller.embarque;

import com.cumpleanos.importramite.persistence.model.embarques.CotizacionConsignatario;
import com.cumpleanos.importramite.persistence.model.embarques.OpcionFlete;
import com.cumpleanos.importramite.persistence.model.embarques.OpcionMasBarataResponse;
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

    @PutMapping("/update-buque/{id}")
    public ResponseEntity<SalidaBuque> updateBuque (@PathVariable String id, @RequestBody SalidaBuque buque){
        SalidaBuque updatedBuque = service.update(id, buque);
        return ResponseEntity.ok(updatedBuque);
    }

    @GetMapping("/mejor-opcion/{cotizacionId}")
    public ResponseEntity<OpcionMasBarataResponse> obtenerMejorOpcion(@PathVariable String cotizacionId) {
        return ResponseEntity.ok(service.obtenerMejorOpcion(cotizacionId));
    }

    @PutMapping("/add-cotizacion/{idBuque}")
    public ResponseEntity<SalidaBuque> agregarCotizacion(@PathVariable String idBuque, @RequestBody CotizacionConsignatario cotizacion) {
        return ResponseEntity.ok(service.agregarCotizacion(idBuque, cotizacion));
    }

    @PutMapping("/add-opcion/{idBuque}/{cotizacionId}")
    public ResponseEntity<SalidaBuque> agregarOpcion(@PathVariable String idBuque, @PathVariable String cotizacionId, @RequestBody OpcionFlete opcion) {
        return ResponseEntity.ok(service.agregarOpcion(idBuque, cotizacionId, opcion));
    }

    @PutMapping("/{idBuque}/cotizaciones/{cotizacionId}/opciones/{opcionId}")
    public ResponseEntity<SalidaBuque> actualizarOpcion(
            @PathVariable String idBuque,
            @PathVariable String cotizacionId,
            @PathVariable String opcionId,
            @RequestBody OpcionFlete opcion) {
        return ResponseEntity.ok(service.actualizarOpcion(idBuque, cotizacionId, opcionId, opcion));
    }

    @DeleteMapping("/{idBuque}/cotizaciones/{cotizacionId}/opciones/{opcionId}")
    public ResponseEntity<SalidaBuque> eliminarOpcion(
            @PathVariable String idBuque,
            @PathVariable String cotizacionId,
            @PathVariable String opcionId) {
        return ResponseEntity.ok(service.eliminarOpcion(idBuque, cotizacionId, opcionId));
    }

    @DeleteMapping("/{idBuque}/cotizaciones/{cotizacionId}")
    public ResponseEntity<SalidaBuque> eliminarCotizacion(
            @PathVariable String idBuque,
            @PathVariable String cotizacionId) {
        return ResponseEntity.ok(service.eliminarCotizacion(idBuque, cotizacionId));
    }

}