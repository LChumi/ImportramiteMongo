package com.cumpleanos.importramite.presentation.controller;

import com.cumpleanos.importramite.persistence.model.Tramite;
import com.cumpleanos.importramite.service.implementation.FileServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@CrossOrigin("*")
@Slf4j
@RestController
@RequestMapping("file")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class FileController {

    private final FileServiceImpl fileService;

    @PostMapping("/excel/tramite")
    public ResponseEntity<Tramite> importExcelTramite(
            @RequestParam("file") MultipartFile file,
            @RequestParam("tramiteId") String tramiteId,
            @RequestParam("fechaLlegada") LocalDate fechaLlegada,
            @RequestParam("contenedorId") String contenedorId) {
        Tramite tramite = fileService.readExcelFile(file, tramiteId.trim(), fechaLlegada, contenedorId.trim());
        return ResponseEntity.ok(tramite);
    }
}
