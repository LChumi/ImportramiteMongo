package com.cumpleanos.importramite.persistence.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document("menu")
public class Menu {

    @Id
    private String id;

    private String label;
    private String icon;
    private List<MenuItem> items;

    private List<String> rolesPermitidos;
}