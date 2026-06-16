package com.cumpleanos.importramite.presentation.controller.reports;

import com.cumpleanos.importramite.persistence.model.pos.MedianetPOS;
import com.cumpleanos.importramite.service.implementation.reports.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final ReporteService reporteService;

    @PostMapping(value = "/medianet", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> generarPdf(@RequestBody MedianetPOS pos){
        byte[] pdf = reporteService.pdfPos(pos);
        String filename = pos.getRequest().referencia();

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename ="+filename+".pdf"
                )
                .body(pdf);
    }
}
