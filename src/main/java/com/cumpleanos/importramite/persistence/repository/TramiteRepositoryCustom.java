package com.cumpleanos.importramite.persistence.repository;

import com.cumpleanos.importramite.persistence.model.Tramite;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor(onConstructor_ =  {@Autowired})
public class TramiteRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    public List<Tramite> buscarTramites(String id, Short proceso, LocalDate fechaInicio, LocalDate fechaFin) {
        Criteria criteria = new Criteria();

        List<Criteria> filters = new ArrayList<>();

        //Filter by Id by Regex (MongoDB)
        if (id != null && !id.isEmpty()){
            filters.add(Criteria.where("id").regex(id, "i")); // "i" ignore uppercase/lowercase
        }
        //Filter by status
        if (proceso != null){
            filters.add(Criteria.where("proceso").is(proceso));
        }
        //Filter date between
        if (fechaInicio != null && fechaFin != null){
            filters.add(Criteria.where("fechaLlegada").gte(fechaInicio).lte(fechaFin));
        }
        //Filter date
        if (fechaInicio != null){
            filters.add(Criteria.where("fechaLlegada").gte(fechaInicio));
        }

        //filter existing
        if (!filters.isEmpty()){
            criteria.andOperator(filters.toArray(new Criteria[0]));
        }

        Query query = new Query(criteria);
        return mongoTemplate.find(query, Tramite.class);
    }
}
