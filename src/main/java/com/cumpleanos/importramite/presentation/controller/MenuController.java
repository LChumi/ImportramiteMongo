package com.cumpleanos.importramite.presentation.controller;

import com.cumpleanos.importramite.persistence.model.Menu;
import com.cumpleanos.importramite.persistence.model.MenuItem;
import com.cumpleanos.importramite.persistence.records.DeleteMenuItemRequest;
import com.cumpleanos.importramite.persistence.records.MoveMenuItemRequest;
import com.cumpleanos.importramite.persistence.records.UpdateMenuRolesRequest;
import com.cumpleanos.importramite.persistence.records.UpsertMenuItemRequest;
import com.cumpleanos.importramite.service.interfaces.IMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("menu")
@RequiredArgsConstructor
public class MenuController {

    private final IMenuService menuService;

    @GetMapping("/get/{user}/usuario")
    public ResponseEntity<List<MenuItem>> getMenu(@PathVariable String user) {
        List<MenuItem> menus = menuService.getMenu(user);
        return ResponseEntity.ok(menus);
    }

    @PostMapping("/item")
    public void add(@RequestBody UpsertMenuItemRequest req) {
        menuService.addItem(req);
    }

    @DeleteMapping("/item")
    public void delete(@RequestBody DeleteMenuItemRequest req) {
        menuService.deleteItem(req);
    }

    @PostMapping("/item/move")
    public void move(@RequestBody MoveMenuItemRequest req) {
        menuService.moveItem(req);
    }

    @PostMapping("/save")
    public ResponseEntity<Menu> save(@RequestBody Menu menu) {
        Menu m = menuService.save(menu);
        return ResponseEntity.ok(m);
    }

    @PutMapping("/roles")
    public void updateRoles(@RequestBody UpdateMenuRolesRequest req) {
        menuService.updateRoles(req);
    }

    @PostMapping("/{menuId}/role/{role}")
    public void addRole(@PathVariable String menuId, @PathVariable String role) {
        menuService.addRole(menuId, role);
    }

    @DeleteMapping("/{menuId}/role/{role}")
    public void removeRole(@PathVariable String menuId, @PathVariable String role) {
        menuService.removeRole(menuId, role);
    }
}