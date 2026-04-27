package com.cumpleanos.importramite.service.interfaces;

import com.cumpleanos.importramite.persistence.model.Menu;
import com.cumpleanos.importramite.persistence.model.MenuItem;
import com.cumpleanos.importramite.persistence.records.DeleteMenuItemRequest;
import com.cumpleanos.importramite.persistence.records.MoveMenuItemRequest;
import com.cumpleanos.importramite.persistence.records.UpsertMenuItemRequest;

import java.util.List;

public interface IMenuService extends IGenericService<Menu,String> {

    List<MenuItem> getMenu(String idUsuario);

    void addItem(UpsertMenuItemRequest req);

    void deleteItem(DeleteMenuItemRequest req);

    void moveItem(MoveMenuItemRequest req);
}
