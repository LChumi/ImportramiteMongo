package com.cumpleanos.importramite.service.http;

import com.cumpleanos.importramite.persistence.api.ProductoApi;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "productosview", url = "http://localhost:7569", path = "/producto")
public interface ProductosClient {

    @GetMapping("/BuscarProducto/{bodega}")
    ResponseEntity<ProductoApi> buscarProdBod(@PathVariable long bodega,@RequestParam String data);

    @GetMapping("/get/matches/{bodega}")
    ResponseEntity<String> getMatches(@PathVariable long bodega,@RequestParam String data, @RequestParam String item);

    @GetMapping("/exist/empresa/{empresa}")
    ResponseEntity<String> existIntoCompany(@PathVariable Long empresa,@RequestParam String barra, @RequestParam String item);
}
