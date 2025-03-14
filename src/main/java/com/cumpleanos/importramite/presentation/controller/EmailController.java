package com.cumpleanos.importramite.presentation.controller;

import com.cumpleanos.importramite.persistence.model.Destinatario;
import com.cumpleanos.importramite.persistence.model.Emails;
import com.cumpleanos.importramite.service.interfaces.IEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("email")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class EmailController {

    private final IEmailService service;

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<Emails> getByTipo(@PathVariable("tipo") Long tipo) {
        Emails emails = service.getByTipo(tipo);
        return ResponseEntity.ok(emails);
    }

    @PostMapping("/save")
    public ResponseEntity<Emails> save(@RequestBody Emails emails) {
        return ResponseEntity.ok(service.save(emails));
    }

    @PutMapping("/add/addressee/{tipo}")
    public ResponseEntity<Emails> addAddressee(@PathVariable Long tipo, @RequestBody Destinatario addressee) {
        Emails emails = service.addAddressee(tipo, addressee);
        return ResponseEntity.ok(emails);
    }

    @DeleteMapping("/remove/{tipo}/{addressee}")
    public ResponseEntity<Emails> removeAddressee(@PathVariable Long tipo, @PathVariable String addressee) {
        Emails email = service.removeAddressee(tipo, addressee);
        return ResponseEntity.ok(email);
    }
}

