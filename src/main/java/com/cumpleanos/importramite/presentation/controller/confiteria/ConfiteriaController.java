package com.cumpleanos.importramite.presentation.controller.confiteria;

import com.cumpleanos.importramite.persistence.model.confiteria.ConfiteriaDetalle;
import com.cumpleanos.importramite.persistence.model.confiteria.ReposicionConfiteria;
import com.cumpleanos.importramite.persistence.records.ReposicionRequest;
import com.cumpleanos.importramite.service.interfaces.confiteria.IConfiteriaDetalleService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("confiteria")
@RequiredArgsConstructor
public class ConfiteriaController {

    private final IConfiteriaDetalleService detalleService;

    @PostMapping("/crear/detalles")
    public ResponseEntity<List<ConfiteriaDetalle>> saveDetalles(@RequestBody ReposicionRequest request) {
        List<ConfiteriaDetalle> detalles = detalleService.saveList(request);
        return ResponseEntity.ok(detalles);
    }

    @GetMapping("/lista/reposicion")
    public ResponseEntity<List<ReposicionConfiteria>> listaReposicion(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {

        List<ReposicionConfiteria> reposiciones = detalleService.findByFechaBetween(fechaInicio, fechaFin);
        return ResponseEntity.ok(reposiciones);
    }


    @GetMapping("/obtener/reposicion/{reposicionId}")
    public ResponseEntity<List<ConfiteriaDetalle>> obtenerPorIdReposicion(@PathVariable String reposicionId) {
        List<ConfiteriaDetalle> detalle = detalleService.findByReposicionId(reposicionId);
        return ResponseEntity.ok(detalle);
    }

    @GetMapping("/obtenerExcel")
    public void getExcel(@RequestParam String reposicionId, HttpServletResponse response) throws IOException {
        try {
            byte[] excelBytes = detalleService.getExcelConfiteria(reposicionId);
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=report.xlsx");
            response.setContentLength(excelBytes.length);
            response.getOutputStream().write(excelBytes);
            response.getOutputStream().flush();
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al generar el reporte en Excel: " + e.getMessage());
        }

    }

}