package com.cumpleanos.importramite.presentation.controller.confiteria;

import com.cumpleanos.importramite.persistence.model.confiteria.ConfiteriaDetalle;
import com.cumpleanos.importramite.persistence.model.confiteria.ReposicionConfiteria;
import com.cumpleanos.importramite.service.interfaces.confiteria.IConfiteriaDetalleService;
import com.cumpleanos.importramite.service.interfaces.confiteria.IReposicionConfiteriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("confiteria")
@RequiredArgsConstructor
public class ConfiteriaController {

    private final IConfiteriaDetalleService detalleService;
    private final IReposicionConfiteriaService reposicionService;

    @PostMapping("/crear/reposicion")
    public ResponseEntity<ReposicionConfiteria> saveReposicion(@RequestBody ReposicionConfiteria reposicionConfiteria) {
        ReposicionConfiteria repo = reposicionService.save(reposicionConfiteria);
        return ResponseEntity.ok(repo);
    }

    @PostMapping("/crear/detalles/{reposicionId}")
    public ResponseEntity<List<ConfiteriaDetalle>> saveDetalles(@RequestBody List<ConfiteriaDetalle> confiteriaDetalles, @PathVariable String reposicionId) {
        List<ConfiteriaDetalle> detalles = detalleService.saveList(confiteriaDetalles, reposicionId);
        return ResponseEntity.ok(detalles);
    }

    @GetMapping("/lista/reposicion")
    public ResponseEntity<List<ReposicionConfiteria>> listaReposicion(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {

        List<ReposicionConfiteria> reposiciones = reposicionService.findByFechaBetween(fechaInicio, fechaFin);
        return ResponseEntity.ok(reposiciones);
    }


    @GetMapping("/obtener/reposicion/{reposicionId}")
    public ResponseEntity<List<ConfiteriaDetalle>> obtenerPorIdReposicion(@PathVariable String reposicionId) {
        Optional<List<ConfiteriaDetalle>> detalle = detalleService.findByReposicionId(reposicionId);
        return ResponseEntity.ok(detalle.orElse(new ArrayList<>()));
    }

}