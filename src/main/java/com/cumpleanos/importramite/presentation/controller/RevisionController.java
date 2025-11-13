package com.cumpleanos.importramite.presentation.controller;

import com.cumpleanos.importramite.persistence.model.Contenedor;
import com.cumpleanos.importramite.persistence.model.ProdcutoCantidades;
import com.cumpleanos.importramite.persistence.model.Producto;
import com.cumpleanos.importramite.persistence.records.ProductValidateRequest;
import com.cumpleanos.importramite.persistence.records.RevisionRequest;
import com.cumpleanos.importramite.persistence.records.StatusResponse;
import com.cumpleanos.importramite.service.interfaces.IRevisionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("revision")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class RevisionController {

    private final IRevisionService service;

    @GetMapping("/tramite/{tramiteId}/contenedor/{contenedorId}/productos/revision")
    public ResponseEntity<List<Producto>> processProductRevision(@PathVariable String tramiteId, @PathVariable String contenedorId) {
        List<Producto> verified = service.processProductRevision(tramiteId, contenedorId);
        return ResponseEntity.ok(verified);
    }

    @GetMapping("/tramite/{tramiteId}/contenedor/{contenedorId}/finalizar")
    public ResponseEntity<StatusResponse> processTramiteCompletion(@PathVariable String tramiteId, @PathVariable String contenedorId) {
        StatusResponse status = service.processTramiteCompletion(tramiteId, contenedorId);
        return ResponseEntity.ok(status);
    }

    @PutMapping("/updateQuantity")
    public ResponseEntity<Producto> updateCantidadByBarra(
            @RequestBody RevisionRequest request
    ) {
        Producto updatedRevision = service.updateCantidadByBarra(request);
        return ResponseEntity.ok(updatedRevision);
    }

    @GetMapping("/contenedores/{tramiteId}")
    public ResponseEntity<List<Contenedor>> listContenedoresByTramite(@PathVariable String tramiteId) {
        List<Contenedor> contenedores = service.listContenedoresByTramite(tramiteId);
        return ResponseEntity.ok(contenedores);
    }

    @GetMapping("/productos/{tramiteId}/{contenedorId}")
    public ResponseEntity<List<Producto>> findByTramiteIdAndContenedorId(@PathVariable String tramiteId, @PathVariable String contenedorId) {
        List<Producto> productos = service.findByTramiteId(tramiteId, contenedorId);
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/producto/existe/{barcode}/{tramite}/{contenedor}")
    public ResponseEntity<StatusResponse> productExist(@PathVariable String barcode, @PathVariable String tramite, @PathVariable String contenedor) {
        StatusResponse response = service.getProducto(tramite, contenedor, barcode);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/producto/validate")
    public ResponseEntity<Producto> validateProduct(@RequestBody ProductValidateRequest request){
        Producto producto = service.updateProdcutoById(request);
        return ResponseEntity.ok(producto);
    }

    @GetMapping("/get-cantidades/{barcode}/{tramite}/{contenedor}")
    public ResponseEntity<List<ProdcutoCantidades>> getCantidades(@PathVariable String barcode, @PathVariable String tramite, @PathVariable String contenedor){
        List<ProdcutoCantidades> cantidades = service.getCantidades(tramite, contenedor, barcode);
        return ResponseEntity.ok(cantidades);
    }

}
