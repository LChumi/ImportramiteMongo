package com.cumpleanos.importramite.service.implementation;

import com.cumpleanos.importramite.persistence.model.Muestra;
import com.cumpleanos.importramite.persistence.model.Producto;
import com.cumpleanos.importramite.persistence.model.Tramite;
import com.cumpleanos.importramite.persistence.records.MuestraRequest;
import com.cumpleanos.importramite.persistence.repository.MuestraRepository;
import com.cumpleanos.importramite.persistence.repository.TramiteRepository;
import com.cumpleanos.importramite.service.exception.DocumentNotFoundException;
import com.cumpleanos.importramite.service.interfaces.IMuestraService;
import com.cumpleanos.importramite.service.interfaces.IProductoService;
import com.cumpleanos.importramite.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.cumpleanos.importramite.utils.ProductoStatus.COMPLETO;
import static com.cumpleanos.importramite.utils.ProductoStatus.FALTANTE;
import static com.cumpleanos.importramite.utils.StringUtils.historial;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class MuestraServiceImpl extends GenericServiceImpl<Muestra, String> implements IMuestraService {

    private final MuestraRepository repository;
    private final TramiteRepository tramiteRepository;
    private final IProductoService productoService;

    @Override
    public CrudRepository<Muestra, String> getRepository() {
        return repository;
    }

    @Override
    public Producto saveAndCompare(MuestraRequest request) {

        String idProducto = request.tramiteId() +"_" + request.contenedor() + "_" + request.barra();

        Producto p = productoService.findById(idProducto);

        if (p== null){
            throw new DocumentNotFoundException("Producto no encontrado");
        }

        if (p.getHistorialBarrasMuestra() == null || p.getHistorialBarrasMuestra().isEmpty()) {
            p.setHistorialRevision(new ArrayList<>());
        }
        if (p.getBarraMuestra() == null || p.getBarraMuestra().isEmpty()) {
            p.setBarraMuestra(request.muestra());
            p.setCantidadMuestra(1);
            p.getHistorialBarrasMuestra().add(historial(true));
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
        p.setStatusMuestra(validateMuestra(p));
        return productoService.save(p);
    }

    @Override
    public List<Producto> updateWithRevision(String tramite, String contenedorId) {

        Tramite tr = tramiteRepository.findById(tramite.trim()).orElseThrow(() -> new DocumentNotFoundException("Tramite no encontrado"));

        List<Producto> productos = productoService.findByTramiteIdAndContenedorId(tramite, contenedorId);

        for (Producto producto : productos) {
            //Si no existe la muestra se registra
            if (producto.getBarraMuestra() == null){
                producto.setProcesoMuestra(FALTANTE.name());
            }else {
                producto.setProcesoMuestra(COMPLETO.name());
            }
            productoService.save(producto);
        }
        boolean allComplete = productos.stream().allMatch(producto -> COMPLETO.name().equals(producto.getProcesoMuestra()));

        if (allComplete) {
            tr.setProceso((short) 5);
            tramiteRepository.save(tr);
        }

        return productoService.findByTramiteIdAndContenedorId(tramite, contenedorId);
    }

    private static boolean validateMuestra(Producto p) {
        return p.getHistorialBarrasMuestra() != null &&
                p.getHistorialBarrasMuestra().stream()
                        .map(StringUtils::extraerBarra)
                        .collect(Collectors.toSet()).size() == 1;
    }

}
