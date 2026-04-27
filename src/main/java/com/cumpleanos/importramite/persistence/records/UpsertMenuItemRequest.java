package com.cumpleanos.importramite.persistence.records;

import com.cumpleanos.importramite.persistence.model.MenuItem;

public record UpsertMenuItemRequest(
        String menuId,
        String parentId,
        MenuItem items
) {
}
