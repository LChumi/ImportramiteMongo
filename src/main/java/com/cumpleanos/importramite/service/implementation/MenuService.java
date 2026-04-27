package com.cumpleanos.importramite.service.implementation;

import com.cumpleanos.importramite.persistence.model.Menu;
import com.cumpleanos.importramite.persistence.model.MenuItem;
import com.cumpleanos.importramite.persistence.model.Usuario;
import com.cumpleanos.importramite.persistence.records.DeleteMenuItemRequest;
import com.cumpleanos.importramite.persistence.records.MoveMenuItemRequest;
import com.cumpleanos.importramite.persistence.records.UpsertMenuItemRequest;
import com.cumpleanos.importramite.persistence.repository.MenuRepository;
import com.cumpleanos.importramite.persistence.repository.UsuarioRepository;
import com.cumpleanos.importramite.service.interfaces.IMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MenuService extends GenericServiceImpl<Menu,String> implements IMenuService {

    private final UsuarioRepository usuarioRepo;
    private final MenuRepository menuRepo;

    public List<MenuItem> getMenu(String idUsuario) {

        Usuario user = usuarioRepo.findByIdUsuario(idUsuario).orElseThrow();

        List<Menu> menus = menuRepo.findByRolesPermitidos(user.getRoles());

        return menus.stream()
                .map(this::mapToMenuItem)
                .toList();
    }

    private MenuItem mapToMenuItem(Menu menu) {
        MenuItem item = new MenuItem();
        item.setLabel(menu.getLabel());
        item.setIcon(menu.getIcon());
        item.setItems(menu.getItems());
        return item;
    }

    //Buscar Nodo (recursivo)
    private MenuItem findById(List<MenuItem> items, String id) {
        for (MenuItem item : items) {
            if (item.getId().equals(id)) return item;

            if (item.getItems() != null){
                MenuItem found = findById(item.getItems(), id);
                if (found != null) return found;
            }
        }
        return null;
    }

    //Eliminar nodo
    private boolean removeById(List<MenuItem> items, String id) {
        Iterator<MenuItem> it = items.iterator();

        while (it.hasNext()) {
            MenuItem item = it.next();

            if (item.getId().equals(id)) {
                it.remove();
                return true;
            }

            if (item.getItems() != null && removeById(item.getItems(), id)) {
                return true;
            }
        }
        return false;
    }

    //Agregar nodo
    public void addItem(UpsertMenuItemRequest req){
        Menu menu = menuRepo.findById(req.menuId()).orElseThrow();

        MenuItem newItem = req.items();
        newItem.setId(UUID.randomUUID().toString());

        if (req.parentId() == null){
            menu.getItems().add(newItem);
        } else {
            MenuItem parent = findById(menu.getItems(), req.parentId());
            assert parent != null;
            parent.getItems().add(newItem);
        }

        menuRepo.save(menu);
    }

    //Eliminar nodo
    public void deleteItem(DeleteMenuItemRequest req){
        Menu menu = menuRepo.findById(req.menuId()).orElseThrow();

        removeById(menu.getItems(), req.itemId());
        menuRepo.save(menu);
    }

    //Mover nodo
    public void moveItem(MoveMenuItemRequest req) {

        Menu menu = menuRepo.findById(req.menuId()).orElseThrow();

        MenuItem item = findById(menu.getItems(), req.itemId());

        removeById(menu.getItems(), req.itemId());

        if (req.newParentId() == null) {
            menu.getItems().add(item);
        } else {
            MenuItem newParent = findById(menu.getItems(), req.newParentId());
            assert newParent != null;
            newParent.getItems().add(item);
        }

        menuRepo.save(menu);
    }

    @Override
    public CrudRepository<Menu, String> getRepository() {
        return menuRepo;
    }
}
