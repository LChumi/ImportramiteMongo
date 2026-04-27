package com.cumpleanos.importramite.persistence.model;

import lombok.Data;

import java.util.List;

@Data
public class MenuItem {
    private String id;
    private String label;
    private String icon;
    private List<String> routerLink;
    private List<MenuItem> items;
}