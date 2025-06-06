package com.cumpleanos.importramite.utils;

import com.cumpleanos.importramite.persistence.model.Contenedor;
import com.cumpleanos.importramite.persistence.model.Producto;
import com.cumpleanos.importramite.persistence.repository.ProductoRepository;
import com.cumpleanos.importramite.service.exception.ExcelNotCreateException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapUtils {

    // Crear un mapa para acumular la cantidad de bultos por producto en el trámite
    public static Map<String, Producto> listByTramite(List<Contenedor> contenedores, ProductoRepository repository) {
        Map<String, Producto> productosMap = new HashMap<>();

        for (Contenedor contenedor : contenedores) {
            List<Producto> productos = repository.findByTramiteIdAndContenedorId(contenedor.getTramiteId(), contenedor.getContenedorId()).orElseThrow(() -> new ExcelNotCreateException("No se encontraron productos para el trámite: " + contenedor.getTramiteId() + " y el contenedor: " + contenedor));

            for (Producto producto : productos) {
                productosMap.merge(producto.getBarcode(), producto, (p1, p2) -> {
                    p1.setBultos(p1.getBultos() + p2.getBultos());
                    p1.setTotal(p1.getTotal() + p2.getTotal());
                    if (p1.getCantidadRevision() != null && p2.getCantidadRevision() != null) {
                        p1.setCantidadRevision(p1.getCantidadRevision() + p2.getCantidadRevision());
                    }
                    if (p1.getCantidadDiferenciaRevision() != null && p2.getCantidadDiferenciaRevision() != null) {
                        p1.setCantidadDiferenciaRevision(p1.getCantidadDiferenciaRevision() + p2.getCantidadDiferenciaRevision());
                    }
                    if (p1.getCantidadMuestra() != null && p2.getCantidadMuestra() != null) {
                        p1.setCantidadMuestra(p1.getCantidadMuestra() + p2.getCantidadMuestra());
                    }
                    return p1;
                });
            }
        }
        return productosMap;
    }

    //Crear un map para acumular la cantidad de bultos por producto en el contenedor
    public static Map<String, Producto> listByContainer(List<Producto> productos) {
        Map<String, Producto> productosMap = new HashMap<>();
        for (Producto producto : productos) {
            productosMap.put(producto.getId(), producto);
        }
        return productosMap;
    }
}