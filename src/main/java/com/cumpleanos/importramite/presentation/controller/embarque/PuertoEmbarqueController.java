package com.cumpleanos.importramite.presentation.controller.embarque;

import com.cumpleanos.importramite.persistence.model.embarques.PuertoEmbarque;
import com.cumpleanos.importramite.service.interfaces.embarque.IPuertoEmbarqueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("puerto-embarque")
@RequiredArgsConstructor
public class PuertoEmbarqueController {

    private final IPuertoEmbarqueService service;

    @PostMapping("/save")
    public ResponseEntity<PuertoEmbarque> savePuertoEmbarque(@RequestBody PuertoEmbarque puertoEmbarque){
        PuertoEmbarque pe = service.save(puertoEmbarque);
        return ResponseEntity.ok(pe);
    }

    @GetMapping("/list")
    public ResponseEntity<List<PuertoEmbarque>> listPuertosEmbarque(){
        List<PuertoEmbarque> pes = service.findAll();
        return ResponseEntity.ok(pes);
    }
}