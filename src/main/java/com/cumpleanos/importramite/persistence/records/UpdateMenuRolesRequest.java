package com.cumpleanos.importramite.persistence.records;

import java.util.List;

public record UpdateMenuRolesRequest(
        String menuId,
        List<String> roles
) {
}
