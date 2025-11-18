package com.cumpleanos.importramite.service.implementation;

import com.cumpleanos.importramite.persistence.model.Contenedor;
import com.cumpleanos.importramite.persistence.model.Producto;
import com.cumpleanos.importramite.persistence.model.Tramite;
import com.cumpleanos.importramite.persistence.records.MuestraRequest;
import com.cumpleanos.importramite.persistence.records.ProductValidateRequest;
import com.cumpleanos.importramite.persistence.repository.ContenedorRepository;
import com.cumpleanos.importramite.persistence.repository.TramiteRepository;
import com.cumpleanos.importramite.service.exception.DocumentNotFoundException;
import com.cumpleanos.importramite.service.interfaces.IMuestraService;
import com.cumpleanos.importramite.service.interfaces.IProductoService;
import com.cumpleanos.importramite.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.cumpleanos.importramite.utils.ProductoStatus.*;
import static com.cumpleanos.importramite.utils.StringUtils.historial;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class MuestraServiceImpl implements IMuestraService {

    private final TramiteRepository tramiteRepository;
    private final IProductoService productoService;
    private final ContenedorRepository contenedorRepository;

    @Override
    public Producto saveAndCompare(MuestraRequest request) {

        String idProducto = request.tramiteId() + "_" + request.contenedor() + "_" + request.barra();

        Producto p = productoService.findById(idProducto);
        Contenedor c = contenedorRepository.findByContenedorId(request.contenedor()).orElseThrow(() -> new DocumentNotFoundException("Contenedor no encontrado: " + request.contenedor()));

        if (c.getStartMuestra() == null) {
            c.setStartMuestra(LocalTime.now());
            contenedorRepository.save(c);
        }

        if (p == null) {
            throw new DocumentNotFoundException("Producto no encontrado");
        }

        if (p.getHistorialBarrasMuestra() == null) {
            p.setHistorialBarrasMuestra(new ArrayList<>());
        }

        if (p.getBarraMuestra() == null || p.getBarraMuestra().isEmpty()) {
            p.setBarraMuestra(request.muestra());
            p.setCantidadMuestra(1);
            p.getHistorialBarrasMuestra().add("BARRA: " + p.getBarraMuestra() + historial(true));
        } else {
            if (request.status()) {
                p.setCantidadMuestra(p.getCantidadMuestra() + 1);

                p.getHistorialBarrasMuestra().add("BARRA: " + p.getBarraMuestra() + historial(true));
            } else {
                int nuevaCantidad = p.getCantidadMuestra() - 1;
                if (nuevaCantidad >= 0) {
                    p.setCantidadMuestra(nuevaCantidad);
                    p.setStatusMuestra(validateMuestra(p));
                    p.getHistorialBarrasMuestra().add("BARRA: " + p.getBarraMuestra() + historial(false));
                }
            }
        }
        p.setUsuarioMuestra(request.usuario());
        p.setStatusMuestra(validateMuestra(p));
        return productoService.save(p);
    }

    @Override
    public List<Producto> updateWithRevision(String tramite, String contenedorId) {

        Tramite tr = tramiteRepository.findById(tramite.trim()).orElseThrow(() -> new DocumentNotFoundException("Tramite no encontrado"));

        List<Producto> productos = productoService.findByTramiteIdAndContenedorId(tramite, contenedorId);

        for (Producto producto : productos) {
            //Si no existe la muestra se registra
            if (producto.getBarraMuestra() == null) {
                producto.setProcesoMuestra(FALTANTE.name());
            } else {
                producto.setProcesoMuestra(COMPLETO.name());
            }
            productoService.save(producto);
        }
        boolean allProductsCompleted = productos.stream().allMatch(producto -> COMPLETO.name().equals(producto.getProcesoMuestra()));

        if (allProductsCompleted) {
            Contenedor contenedor = contenedorRepository.findByContenedorId(contenedorId).orElseThrow(() -> new DocumentNotFoundException("Contenedor no encontrado: " + contenedorId));
            contenedor.setMuestras(true);
            contenedor.setEndMuestra(LocalTime.now());
            contenedorRepository.save(contenedor);
            tr.setProceso((short) 4);
            tramiteRepository.save(tr);
        }

        Optional<List<Contenedor>> contenedoresOpt = contenedorRepository.findByTramiteId(tramite);
        boolean allContenedoresCompleted = contenedoresOpt
                .map(list -> list.stream()
                        .allMatch(contenedor -> Boolean.TRUE.equals(contenedor.getMuestras()))) // Trata null como false
                .orElse(false);

        if (allContenedoresCompleted) {
            tr.setProceso((short) 5);
            tramiteRepository.save(tr);
        }

        return productoService.findByTramiteIdAndContenedorId(tramite, contenedorId);
    }

    @Override
    public List<Producto> getMuestras(String tramite, String contenedor) {
        return productoService.findByTramiteIdAndContenedorId(tramite, contenedor).stream()
                .filter(p -> p.getBarraMuestra() != null &&
                        p.getCantidadMuestra() != null
                ).collect(Collectors.toList());
    }

    @Override
    public Producto updateProdcutoById(ProductValidateRequest request) {
        Producto pr = productoService.findById(request.productId());
        if (pr == null) {
            throw new DocumentNotFoundException("La muestra no existe");
        }
        pr.setBarraMuestra("-");
        pr.setCantidadMuestra(0);
        pr.setProcesoMuestra("ITEM SIN MUESTRA");
        pr.setStatusMuestra(false);
        pr.setUsuarioMuestra(request.usuario());
        if (pr.getNovedad() == null ||  pr.getNovedad().isEmpty()) {
            pr.setNovedad("No se registra la muestra del producto");
        }else {
            pr.setNovedad(pr.getNovedad() + " No se registra la muestra del producto");
        }
        return productoService.save(pr);
    }

    private static boolean validateMuestra(Producto p) {
        return p.getHistorialBarrasMuestra() != null &&
                p.getHistorialBarrasMuestra().stream()
                        .map(StringUtils::extraerBarra) // Extrae la barra
                        .collect(Collectors.toSet()).size() == 1;
    }

}