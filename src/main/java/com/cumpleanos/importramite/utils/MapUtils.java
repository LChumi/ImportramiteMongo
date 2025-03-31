package com.cumpleanos.importramite.utils;

import com.cumpleanos.importramite.persistence.model.Contenedor;
import com.cumpleanos.importramite.persistence.model.Producto;
import com.cumpleanos.importramite.persistence.model.Tramite;

import java.util.HashMap;
import java.util.Map;

public class MapUtils {

    // Crear un mapa para acumular la cantidad de bultos por producto en el trámite
    public static Map<String, Producto> listByTramite(Tramite tr) {
        Map<String, Producto> productosMap = new HashMap<>();

        for (Contenedor contenedor : tr.getContenedores()) {
            for (Producto producto : contenedor.getProductos()) {
                productosMap.merge(producto.getId(), producto, (p1, p2) -> {
                    p1.setBultos(p1.getBultos() + p2.getBultos());
                    p1.setTotal(p1.getTotal() + p2.getTotal());
                    return p1;
                });
            }
        }
        return productosMap;
    }

    //Crear un map para acumular la cantidad de bultos por producto en el contenedor
    public static Map<String, Producto> listByContainer(Contenedor contenedor) {
        Map<String, Producto> productosMap = new HashMap<>();
        for (Producto producto : contenedor.getProductos()) {
            productosMap.put(producto.getId(), producto);
        }
        return productosMap;
    }
}
