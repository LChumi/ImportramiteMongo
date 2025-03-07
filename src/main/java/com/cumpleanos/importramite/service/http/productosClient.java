package com.cumpleanos.importramite.service.http;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "productosview", url = "http://192.168.112.36:7569/producto")
public interface productosClient {

    @GetMapping("/BuscarProducto/{bodega}")
    ResponseEntity<String> buscarProdBod(@PathVariable long bodega, @RequestParam String data);
}
