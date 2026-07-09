package com.cumpleanos.importramite.service.implementation.confiteria;

import com.cumpleanos.importramite.persistence.model.confiteria.ReposicionConfiteria;
import com.cumpleanos.importramite.persistence.records.confiteria.*;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardConfiteriaService {

    private final MongoTemplate mongoTemplate;

    public DashboardConfiteriaDTO obtenerDashboard(LocalDate fechaInicio,
                                                   LocalDate fechaFin) {

        return new DashboardConfiteriaDTO(
                obtenerTotalReposiciones(fechaInicio, fechaFin),
                obtenerTotalProductos(fechaInicio, fechaFin),
                obtenerValorTotal(fechaInicio, fechaFin),
                obtenerProveedores(fechaInicio, fechaFin),
                obtenerTopProductos(fechaInicio, fechaFin),
                obtenerHistorial(fechaInicio, fechaFin)
        );
    }

    private Long obtenerTotalReposiciones(LocalDate inicio, LocalDate fin) {

        Query query = new Query();

        query.addCriteria(
                Criteria.where("fecha")
                        .gte(inicio)
                        .lte(fin)
        );

        return mongoTemplate.count(query, ReposicionConfiteria.class);
    }

    private Long obtenerTotalProductos(LocalDate inicio, LocalDate fin) {

        Aggregation aggregation = Aggregation.newAggregation(

                addObjectId(),

                Aggregation.lookup(
                        "Reposicion_confiteria",
                        "reposicionObjectId",
                        "_id",
                        "reposicion"
                ),
                Aggregation.unwind("reposicion"),

                Aggregation.match(
                        Criteria.where("reposicion.fecha")
                                .gte(inicio)
                                .lte(fin)
                ),

                Aggregation.group()
                        .sum("pedido")
                        .as("totalProductos")
        );

        AggregationResults<Document> results = mongoTemplate.aggregate(
                aggregation,
                "Confiteria_detalle",
                Document.class
        );

        Document result = results.getUniqueMappedResult();

        if (result == null) {
            return 0L;
        }

        Number total = result.get("totalProductos", Number.class);


        return total == null ? 0L : total.longValue();
    }

    private BigDecimal obtenerValorTotal(LocalDate inicio, LocalDate fin) {

        Aggregation aggregation = Aggregation.newAggregation(

                addObjectId(),

                Aggregation.lookup(
                        "Reposicion_confiteria",
                        "reposicionObjectId",
                        "_id",
                        "reposicion"
                ),

                Aggregation.unwind("reposicion"),

                Aggregation.match(
                        Criteria.where("reposicion.fecha")
                                .gte(inicio)
                                .lte(fin)
                ),

                Aggregation.project()
                        .andExpression("pedido * pvp")
                        .as("valor"),

                Aggregation.group()
                        .sum("valor")
                        .as("valorTotal")
        );

        AggregationResults<Document> results = mongoTemplate.aggregate(
                aggregation,
                "Confiteria_detalle",
                Document.class
        );

        Document result = results.getUniqueMappedResult();

        if (result == null) {
            return BigDecimal.ZERO;
        }

        Number total = result.get("valorTotal", Number.class);

        return total == null ? BigDecimal.ZERO : BigDecimal.valueOf(total.doubleValue()).setScale(2, RoundingMode.HALF_UP);
    }

    private List<ProveedorDTO> obtenerProveedores(LocalDate inicio,
                                                  LocalDate fin) {

        Aggregation aggregation = Aggregation.newAggregation(

                addObjectId(),

                Aggregation.lookup(
                        "Reposicion_confiteria",
                        "reposicionObjectId",
                        "_id",
                        "reposicion"
                ),

                Aggregation.unwind("reposicion"),

                Aggregation.match(
                        Criteria.where("reposicion.fecha")
                                .gte(inicio)
                                .lte(fin)
                ),

                Aggregation.project()
                        .and("reposicion.proveedor").as("proveedor")
                        .and("reposicionId").as("reposicionId")
                        .and("pedido").as("pedido")
                        .andExpression("pedido * pvp").as("valor"),


                // Primero agrupamos por proveedor + reposición
                Aggregation.group("proveedor", "reposicionId")
                        .sum("pedido").as("totalProductos")
                        .sum("valor").as("valorTotal"),


                // Luego agrupamos solo por proveedor
                Aggregation.group("_id.proveedor")
                        .count().as("cantidadReposiciones")
                        .sum("totalProductos").as("totalProductos")
                        .sum("valorTotal").as("valorTotal"),


                Aggregation.sort(
                        Sort.Direction.DESC,
                        "valorTotal"
                )
        );


        AggregationResults<Document> results =
                mongoTemplate.aggregate(
                        aggregation,
                        "Confiteria_detalle",
                        Document.class
                );


        return results.getMappedResults()
                .stream()
                .map(doc -> new ProveedorDTO(
                        doc.getString("_id"),
                        ((Number) doc.get("cantidadReposiciones")).longValue(),
                        ((Number) doc.get("totalProductos")).longValue(),
                        new BigDecimal(doc.get("valorTotal").toString()).setScale(2, BigDecimal.ROUND_HALF_UP)
                ))
                .toList();
    }

    private List<ProductoDTO> obtenerTopProductos(LocalDate inicio, LocalDate fin) {

        Aggregation aggregation = Aggregation.newAggregation(

                addObjectId(),

                Aggregation.lookup(
                        "Reposicion_confiteria",
                        "reposicionObjectId",
                        "_id",
                        "reposicion"
                ),

                Aggregation.unwind("reposicion"),

                Aggregation.match(
                        Criteria.where("reposicion.fecha")
                                .gte(inicio)
                                .lte(fin)
                ),

                Aggregation.project()
                        .and("item").as("item")
                        .and("proNombre").as("producto")
                        .and("pedido").as("pedido")
                        .andExpression("pedido * pvp").as("valor"),

                Aggregation.group("item", "producto")
                        .sum("pedido").as("cantidadPedida")
                        .sum("valor").as("valorTotal"),

                Aggregation.sort(Sort.Direction.DESC, "cantidadPedida"),

                Aggregation.limit(10),

                Aggregation.project()
                        .and("_id.item").as("item")
                        .and("_id.producto").as("producto")
                        .and("cantidadPedida").as("cantidadPedida")
                        .and("valorTotal").as("valorTotal")
                        .andExclude("_id")
        );

        AggregationResults<ProductoDTO> results =
                mongoTemplate.aggregate(
                        aggregation,
                        "Confiteria_detalle",
                        ProductoDTO.class
                );

        return results.getMappedResults();
    }

    private List<FechaDTO> obtenerHistorial(LocalDate inicio, LocalDate fin) {

        Aggregation aggregation = Aggregation.newAggregation(

                addObjectId(),

                Aggregation.lookup(
                        "Reposicion_confiteria",
                        "reposicionObjectId",
                        "_id",
                        "reposicion"
                ),

                Aggregation.unwind("reposicion"),

                Aggregation.match(
                        Criteria.where("reposicion.fecha")
                                .gte(inicio)
                                .lte(fin)
                ),

                Aggregation.project()
                        .and("reposicion.fecha").as("fecha")
                        .and("reposicionId").as("reposicionId")
                        .and("pedido").as("pedido")
                        .andExpression("pedido * pvp").as("valor"),

                // Agrupa por fecha + reposición
                Aggregation.group("fecha", "reposicionId")
                        .sum("pedido").as("productos")
                        .sum("valor").as("valorTotal"),

                // Agrupa por fecha
                Aggregation.group("_id.fecha")
                        .count().as("reposiciones")
                        .sum("productos").as("productos")
                        .sum("valorTotal").as("valorTotal"),

                Aggregation.sort(Sort.Direction.ASC, "_id"),

                Aggregation.project()
                        .and("_id").as("fecha")
                        .and("reposiciones").as("reposiciones")
                        .and("productos").as("productos")
                        .and("valorTotal").as("valorTotal")
                        .andExclude("_id")
        );

        AggregationResults<FechaDTO> results =
                mongoTemplate.aggregate(
                        aggregation,
                        "Confiteria_detalle",
                        FechaDTO.class
                );

        return results.getMappedResults()
                .stream()
                .map(fecha -> new FechaDTO(
                        fecha.fecha(),
                        fecha.reposiciones(),
                        fecha.productos(),
                        fecha.valorTotal()
                                .setScale(2, RoundingMode.HALF_UP)
                ))
                .toList();
    }

    private AggregationOperation addObjectId() {
        return context ->
                new Document("$addFields",
                        new Document("reposicionObjectId",
                                new Document("$toObjectId", "$reposicionId")));
    }

}