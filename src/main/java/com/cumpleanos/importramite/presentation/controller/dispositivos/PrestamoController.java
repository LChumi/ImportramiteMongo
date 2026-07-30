package com.cumpleanos.importramite.presentation.controller.dispositivos;

import com.cumpleanos.importramite.persistence.model.dispositivosinv.EstadoDispositivo;
import com.cumpleanos.importramite.persistence.model.dispositivosinv.Prestamo;
import com.cumpleanos.importramite.service.interfaces.dispositivos.IPrestamoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("prestamos")
@RequiredArgsConstructor
public class PrestamoController {

    private final IPrestamoService prestamoService;

    @PostMapping("/entregar")
    public ResponseEntity<Prestamo> entregar(@RequestBody Prestamo prestamo) {
        return ResponseEntity.ok(prestamoService.entregar(prestamo));
    }

    @PatchMapping("/{id}/devolver")
    public ResponseEntity<Prestamo> devolver(
            @PathVariable String id,
            @RequestParam EstadoDispositivo estadoFinal,
            @RequestParam(required = false) String observaciones) {
        return ResponseEntity.ok(prestamoService.devolver(id, estadoFinal, observaciones));
    }

    @GetMapping("/activos")
    public ResponseEntity<List<Prestamo>> activos() {
        return ResponseEntity.ok(prestamoService.prestamosActivos());
    }

    @GetMapping("/dispositivo/{dispositivoId}/ocupado")
    public ResponseEntity<Prestamo> ocupadoPor(@PathVariable String dispositivoId) {
        return prestamoService.ocupadoPor(dispositivoId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @GetMapping("/historial/{serial}")
    public ResponseEntity<List<Prestamo>> historial(@PathVariable String serial) {
        return ResponseEntity.ok(prestamoService.historialPorSerial(serial));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Prestamo> findById(@PathVariable String id) {
        Prestamo p = prestamoService.findById(id);
        return p != null ? ResponseEntity.ok(p) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<Prestamo>> findAll() {
        return ResponseEntity.ok(prestamoService.findAll());
    }
}