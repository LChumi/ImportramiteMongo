package com.cumpleanos.importramite.presentation.controller;

import com.cumpleanos.importramite.persistence.model.Usuario;
import com.cumpleanos.importramite.service.interfaces.IUsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("usuario")
@RequiredArgsConstructor
public class UsuarioController {

    private final IUsuarioService service;

    @PostMapping("/upsert")
    public ResponseEntity<Usuario> upsert(@RequestBody Usuario usuario) {
        Usuario user = service.upsertUsuario(usuario);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{idUsuario}/roles")
    public ResponseEntity<Usuario> updateRoles(@PathVariable String idUsuario, @RequestBody List<String> roles){
        Usuario user = service.addRoles(idUsuario, roles);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{idUsuario}/roles")
    public ResponseEntity<Usuario> deleteRoles(@PathVariable String idUsuario,  @RequestBody List<String> roles){
        Usuario user = service.removeRoles(idUsuario, roles);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{idUsuario}")
    public ResponseEntity<Usuario> getUsuario(@PathVariable String idUsuario){
        Usuario user = service.getUserByUserid(idUsuario);
        return ResponseEntity.ok(user);
    }
}
