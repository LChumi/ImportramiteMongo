package com.cumpleanos.importramite.presentation.controller;

import com.cumpleanos.importramite.persistence.model.Tramite;
import com.cumpleanos.importramite.service.implementation.FileServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
            @RequestParam("observacion") String observacion) {
        Tramite tramite = fileService.readExcelFile(file, tramiteId, observacion);
        return ResponseEntity.ok(tramite);
    }
}
