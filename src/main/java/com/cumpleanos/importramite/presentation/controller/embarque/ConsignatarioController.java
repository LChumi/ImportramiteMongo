package com.cumpleanos.importramite.presentation.controller.embarque;

import com.cumpleanos.importramite.service.interfaces.embarque.IConsignatarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping("consignatario")
@RequiredArgsConstructor
public class ConsignatarioController {

    private final IConsignatarioService service;


}