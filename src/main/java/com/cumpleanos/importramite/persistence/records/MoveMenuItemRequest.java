package com.cumpleanos.importramite.persistence.records;

public record MoveMenuItemRequest(
        String menuId,
        String itemId,
        String newParentId //null = root
) {
}
