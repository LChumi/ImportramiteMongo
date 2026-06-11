package com.cumpleanos.importramite.presentation.controller;

import com.cumpleanos.importramite.persistence.model.ProductoObservacion;
import com.cumpleanos.importramite.persistence.records.CorreccionRequest;
import com.cumpleanos.importramite.persistence.records.ObservacionPorBodegaDTO;
import com.cumpleanos.importramite.persistence.records.ObservacionPorMesDTO;
import com.cumpleanos.importramite.persistence.records.ObservacionResumenDTO;
import com.cumpleanos.importramite.service.implementation.ObservacionService;
import com.cumpleanos.importramite.service.interfaces.IProductoObservacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("observacion")
@RequiredArgsConstructor
@Tag(name = "Producto Observacion", description = "Documentacion API Observaciones de productos")
public class ProductoObservacionController {

    private final IProductoObservacionService service;
    private final ObservacionService dashboardService;

    @Operation(summary = "Guardar", description = "Guardar Observacion de Producto")
    @PostMapping("/guardar")
    public ResponseEntity<ProductoObservacion> saveProductoObservacion(@RequestBody ProductoObservacion productoObservacion) {
        return ResponseEntity.ok(service.saveObservation(productoObservacion));
    }

    @Operation(summary = "Listar", description = "Listar Observaciones de Productos")
    @Parameter(name = "bodegaId", description = "Id de la bodega donde se encuentra la observacion del producto")
    @GetMapping("/listar/{bodegaId}")
    public ResponseEntity<List<ProductoObservacion>> listarProductoObservacion(@PathVariable Long bodegaId) {
        return ResponseEntity.ok(service.findByBodega(bodegaId));
    }

    @Operation(summary = "Correccion", description = "Agregar correccion de productos")
    @PutMapping("/agregar-correccion")
    public ResponseEntity<ProductoObservacion> agregarCorreccion(@RequestBody CorreccionRequest request) {
        return ResponseEntity.ok(service.addCorrection(request));
    }

    //DASHBOARD

    @GetMapping("/dashboard/resumen")
    public ResponseEntity<ObservacionResumenDTO> resumen(@RequestParam Long bodega) {
        return ResponseEntity.ok(dashboardService.getResumen(bodega));
    }

    @GetMapping("/dashboard/por-bodega")
    public ResponseEntity<List<ObservacionPorBodegaDTO>> porBodega() {
        return ResponseEntity.ok(dashboardService.getPorBodega());
    }

    @GetMapping("/dashboard/por-mes")
    public ResponseEntity<List<ObservacionPorMesDTO>> porMes(
            @RequestParam Long bodega,
            @RequestParam(defaultValue = "2026") int anio) {
        return ResponseEntity.ok(dashboardService.getPorMes(bodega, anio));
    }

    @GetMapping("/dashboard/top-productos")
    public ResponseEntity<?> topProductos(
            @RequestParam Long bodega,
            @RequestParam(defaultValue = "10") int limite) {
        return ResponseEntity.ok(dashboardService.getTopProductos(bodega, limite));
    }

}