package com.cumpleanos.importramite.presentation.controller.embarque;

import com.cumpleanos.importramite.service.implementation.embarque.FleteValidadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping("flete")
@RequiredArgsConstructor
public class FleteValidadoController {

    private final FleteValidadoService service;
}