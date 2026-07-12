package com.manpowergroup.kintai.system.domain.model.sys;

import com.manpowergroup.kintai.system.domain.entity.sys.SysRoleMenu;
import com.manpowergroup.kintai.system.domain.entity.sys.SysRolePermission;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RoleAuthorizationTest {

    @Test
    void replaceNormalizesNullListsToEmptyLists() {
        RoleAuthorization authorization = RoleAuthorization.replace(7L, null, null);

        assertEquals(7L, authorization.roleId());
        assertEquals(List.of(), authorization.menuIds());
        assertEquals(List.of(), authorization.permissionIds());
    }

    @Test
    void replaceFiltersNullIdsAndRemovesDuplicatesInInputOrder() {
        RoleAuthorization authorization = RoleAuthorization.replace(
                7L,
                Arrays.asList(1L, null, 2L, 1L, 3L),
                Arrays.asList(10L, null, 11L, 10L, 12L));

        assertEquals(List.of(1L, 2L, 3L), authorization.menuIds());
        assertEquals(List.of(10L, 11L, 12L), authorization.permissionIds());
    }

    @Test
    void toRoleMenusCreatesRelationEntitiesForNormalizedMenuIds() {
        RoleAuthorization authorization = RoleAuthorization.replace(
                7L,
                Arrays.asList(1L, null, 2L, 1L),
                null);

        List<SysRoleMenu> roleMenus = authorization.toRoleMenus();

        assertEquals(2, roleMenus.size());
        assertEquals(List.of(7L, 7L), roleMenus.stream().map(SysRoleMenu::getRoleId).toList());
        assertEquals(List.of(1L, 2L), roleMenus.stream().map(SysRoleMenu::getMenuId).toList());
    }

    @Test
    void toRolePermissionsCreatesRelationEntitiesForNormalizedPermissionIds() {
        RoleAuthorization authorization = RoleAuthorization.replace(
                7L,
                null,
                Arrays.asList(10L, null, 11L, 10L));

        List<SysRolePermission> rolePermissions = authorization.toRolePermissions();

        assertEquals(2, rolePermissions.size());
        assertEquals(List.of(7L, 7L), rolePermissions.stream().map(SysRolePermission::getRoleId).toList());
        assertEquals(List.of(10L, 11L), rolePermissions.stream().map(SysRolePermission::getPermissionId).toList());
    }

    @Test
    void normalizedListsAreReadOnly() {
        RoleAuthorization authorization = RoleAuthorization.replace(7L, List.of(1L), List.of(10L));

        assertThrows(UnsupportedOperationException.class, () -> authorization.menuIds().add(2L));
        assertThrows(UnsupportedOperationException.class, () -> authorization.permissionIds().add(11L));
    }
}
