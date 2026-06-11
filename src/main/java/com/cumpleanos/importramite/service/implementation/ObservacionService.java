package com.cumpleanos.importramite.service.implementation;

import com.cumpleanos.importramite.persistence.records.ObservacionPorBodegaDTO;
import com.cumpleanos.importramite.persistence.records.ObservacionPorMesDTO;
import com.cumpleanos.importramite.persistence.records.ObservacionResumenDTO;
import com.cumpleanos.importramite.persistence.records.TopProductoRaw;
import com.cumpleanos.importramite.persistence.repository.ProductoObservacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ObservacionService {

    private final ProductoObservacionRepository repository;

    public ObservacionResumenDTO getResumen(Long idBodega) {
        var raw = repository.resumenPorBodega(idBodega);
        if (raw == null) return new ObservacionResumenDTO(0, 0, 0, BigDecimal.ZERO);
        long sin = raw.total() - raw.conCorreccion();
        return new ObservacionResumenDTO(raw.total(), raw.conCorreccion(), sin, raw.totalValor());
    }

    public List<ObservacionPorBodegaDTO> getPorBodega() {
        return repository.agrupadoPorBodega().stream()
                .map(r -> new ObservacionPorBodegaDTO(
                        r.id(), r.total(), r.corregidos(), r.total() - r.corregidos()
                )).toList();
    }

    public List<ObservacionPorMesDTO> getPorMes(Long idBodega, int anio) {
        LocalDate desde = LocalDate.of(anio, 1, 1);
        LocalDate hasta = LocalDate.of(anio, 12, 31);
        return repository.porMesYBodega(idBodega, desde, hasta).stream()
                .map(r -> new ObservacionPorMesDTO(r.id().mes(), r.total(), r.corregidos()))
                .toList();
    }

    public List<TopProductoRaw> getTopProductos(Long idBodega, int limite) {
        return repository.topProductos(idBodega, limite);
    }
}