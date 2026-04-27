package com.cumpleanos.importramite.persistence.records;

public record DeleteMenuItemRequest (
        String menuId,
        String itemId
) {
}
