package com.cumpleanos.importramite.presentation.controller;

import com.cumpleanos.importramite.persistence.model.Producto;
import com.cumpleanos.importramite.service.interfaces.IProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("product")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ProductoController {

    private final IProductoService service;

    @GetMapping("/all")
    public ResponseEntity<List<Producto>> getAllProductos() {
        List<Producto> productos = service.findAll();
        return ResponseEntity.ok(productos);
    }

    @PostMapping("/save")
    public ResponseEntity<Producto> saveProducto(@RequestBody Producto producto) {
        Producto newProducto = service.save(producto);
        return ResponseEntity.ok(newProducto);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Producto> updateProducto(@RequestBody Producto producto, @PathVariable String id) {
        Producto found = service.findById(id.trim());
        if (found == null) {
            return ResponseEntity.notFound().build();
        }
        found.setNombre(producto.getNombre());
        found.setBultos(producto.getBultos());
        found.setCxb(producto.getCxb());
        found.setTotal(producto.getTotal());
        return ResponseEntity.ok(service.save(found));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Producto> deleteProducto(@PathVariable String id) {
        Producto found = service.findById(id);
        if (found == null) {
            return ResponseEntity.notFound().build();
        }
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
