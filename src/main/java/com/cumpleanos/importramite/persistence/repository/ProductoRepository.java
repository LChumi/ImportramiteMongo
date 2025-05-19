package com.cumpleanos.importramite.persistence.repository;

import com.cumpleanos.importramite.persistence.model.Producto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends MongoRepository<Producto, String> {

    Optional<List<Producto>> findByTramiteIdAndContenedorId(String tramiteId, String contenedorId);

    Optional<Producto> findByBarcodeAndTramiteIdAndContenedorId(String barcode, String tramiteId, String contenedorId);

}