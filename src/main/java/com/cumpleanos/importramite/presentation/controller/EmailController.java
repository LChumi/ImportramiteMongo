package com.cumpleanos.importramite.presentation.controller;

import com.cumpleanos.importramite.persistence.model.Emails;
import com.cumpleanos.importramite.service.interfaces.IEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
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


}
