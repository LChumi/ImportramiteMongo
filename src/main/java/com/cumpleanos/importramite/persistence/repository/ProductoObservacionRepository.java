package com.cumpleanos.importramite.persistence.repository;

import com.cumpleanos.importramite.persistence.model.ProductoObservacion;
import com.cumpleanos.importramite.persistence.records.DashboardResumenRaw;
import com.cumpleanos.importramite.persistence.records.ObservacionPorBodegaRaw;
import com.cumpleanos.importramite.persistence.records.ObservacionPorMesRaw;
import com.cumpleanos.importramite.persistence.records.TopProductoRaw;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProductoObservacionRepository extends MongoRepository<ProductoObservacion, String> {

    List<ProductoObservacion> findByIdBodegaOrderByFechaDesc(Long idBodega);

    Optional<ProductoObservacion> findByItemAndIdBodega(String item, Long idBodega);

    // Resumen por bodega
    @Aggregation(pipeline = {
            "{ $match: { 'idBodega': ?0 } }",
            "{ $group: { _id: null, total: { $sum: 1 }, conCorreccion: { $sum: { $cond: [{ $ifNull: ['$correccion', false] }, 1, 0] } }, totalValor: { $sum: '$precioTotal' } } }"
    })
    DashboardResumenRaw resumenPorBodega(Long idBodega);

    // Por bodega (todas)
    @Aggregation(pipeline = {
            "{ $group: { _id: '$idBodega', total: { $sum: 1 }, corregidos: { $sum: { $cond: [{ $ifNull: ['$correccion', false] }, 1, 0] } } } }",
            "{ $sort: { total: -1 } }"
    })
    List<ObservacionPorBodegaRaw> agrupadoPorBodega();

    // Por mes y bodega
    @Aggregation(pipeline = {
            "{ $match: { 'idBodega': ?0, 'fecha': { $gte: ?1, $lte: ?2 } } }",
            "{ $group: { _id: { mes: { $month: '$fecha' } }, total: { $sum: 1 }, corregidos: { $sum: { $cond: [{ $ifNull: ['$correccion', false] }, 1, 0] } } } }",
            "{ $sort: { '_id.mes': 1 } }"
    })
    List<ObservacionPorMesRaw> porMesYBodega(Long idBodega, LocalDate desde, LocalDate hasta);

    // Top productos con más observaciones
    @Aggregation(pipeline = {
            "{ $match: { 'idBodega': ?0 } }",
            "{ $group: { _id: '$item', descripcion: { $first: '$descripcion' }, total: { $sum: 1 } } }",
            "{ $sort: { total: -1 } }",
            "{ $limit: ?1 }"
    })
    List<TopProductoRaw> topProductos(Long idBodega, int limite);
}