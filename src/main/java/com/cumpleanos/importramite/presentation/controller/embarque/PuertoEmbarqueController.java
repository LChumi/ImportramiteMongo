package com.cumpleanos.importramite.presentation.controller.embarque;

import com.cumpleanos.importramite.service.interfaces.embarque.IPuertoEmbarqueService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping("puerto-embarque")
@RequiredArgsConstructor
public class PuertoEmbarqueController {

    private final IPuertoEmbarqueService service;

}