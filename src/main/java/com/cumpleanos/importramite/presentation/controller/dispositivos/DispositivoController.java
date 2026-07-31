package com.cumpleanos.importramite.presentation.controller.dispositivos;

import com.cumpleanos.importramite.persistence.model.dispositivosinv.Dispositivo;
import com.cumpleanos.importramite.persistence.model.dispositivosinv.EstadoDispositivo;
import com.cumpleanos.importramite.service.interfaces.dispositivos.IDispositivoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("dispositivos")
@RequiredArgsConstructor
public class DispositivoController {

    private final IDispositivoService dispositivoService;

    @PostMapping
    public ResponseEntity<Dispositivo> registrar(@RequestBody Dispositivo dispositivo) {
        return ResponseEntity.ok(dispositivoService.registrar(dispositivo));
    }

    @GetMapping
    public ResponseEntity<List<Dispositivo>> findAll() {
        return ResponseEntity.ok(dispositivoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dispositivo> findById(@PathVariable String id) {
        Dispositivo d = dispositivoService.findById(id);
        return d != null ? ResponseEntity.ok(d) : ResponseEntity.notFound().build();
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<Dispositivo>> disponibles(
            @RequestParam(required = false) String marca) {
        if (marca != null && !marca.isBlank()) {
            return ResponseEntity.ok(dispositivoService.disponiblesPorMarca(marca));
        }
        return ResponseEntity.ok(dispositivoService.disponibles());
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Dispositivo> cambiarEstado(
            @PathVariable String id,
            @RequestParam EstadoDispositivo estado) {
        return ResponseEntity.ok(dispositivoService.cambiarEstado(id, estado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        dispositivoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/actualizar")
    public ResponseEntity<Dispositivo> actualizar(@RequestBody Dispositivo dispositivo, @PathVariable String id) {
        return ResponseEntity.ok(dispositivoService.actualizar(id,  dispositivo));
    }

}