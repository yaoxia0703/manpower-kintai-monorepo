package com.manpowergroup.kintai.system.application.service.impl.sys;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manpowergroup.kintai.common.exception.BaseErrorCode;
import com.manpowergroup.kintai.common.exception.BizException;
import com.manpowergroup.kintai.system.application.command.sys.MenuCreateCommand;
import com.manpowergroup.kintai.system.application.command.sys.MenuUpdateCommand;
import com.manpowergroup.kintai.system.application.service.sys.SysMenuService;
import com.manpowergroup.kintai.system.domain.entity.sys.SysEmployeeRole;
import com.manpowergroup.kintai.system.domain.entity.sys.SysMenu;
import com.manpowergroup.kintai.system.domain.entity.sys.SysPermission;
import com.manpowergroup.kintai.system.domain.entity.sys.SysRoleMenu;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysEmployeeRoleMapper;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysMenuMapper;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysPermissionMapper;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysRoleMenuMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu>
        implements SysMenuService {

    private final SysEmployeeRoleMapper employeeRoleMapper;
    private final SysRoleMenuMapper roleMenuMapper;
    private final SysPermissionMapper permissionMapper;

    @Override
    public SysMenu getById(Long id) {
        return requireMenu(id);
    }

    private SysMenu requireMenu(Long id) {
        SysMenu menu = super.getById(id);
        if (menu == null) throw new BizException(SystemErrorCode.MENU_NOT_FOUND);
        return menu;
    }

    @Override
    public List<SysMenu> listAll() {
        return lambdaQuery().orderByAsc(SysMenu::getSort).list();
    }

    @Override
    public List<SysMenu> listByEmployeeId(Long employeeId) {
        LocalDate today = LocalDate.now();
        List<Long> roleIds = employeeRoleMapper.selectList(Wrappers.<SysEmployeeRole>lambdaQuery()
                        .eq(SysEmployeeRole::getEmployeeId, employeeId))
                .stream()
                .filter(assignment -> assignment.isEffectiveOn(today))
                .map(SysEmployeeRole::getRoleId)
                .collect(Collectors.toList());
        if (roleIds.isEmpty()) return Collections.emptyList();

        List<Long> menuIds = roleMenuMapper.selectList(Wrappers.<SysRoleMenu>lambdaQuery()
                        .in(SysRoleMenu::getRoleId, roleIds))
                .stream().map(SysRoleMenu::getMenuId).distinct().collect(Collectors.toList());
        if (menuIds.isEmpty()) return Collections.emptyList();

        return lambdaQuery()
                .in(SysMenu::getId, menuIds)
                .orderByAsc(SysMenu::getSort)
                .list();
    }

    @Override
    @Transactional
    public SysMenu create(MenuCreateCommand command) {
        boolean exists = lambdaQuery().eq(SysMenu::getCode, command.code()).count() > 0;
        if (exists) throw new BizException(SystemErrorCode.MENU_CODE_DUPLICATE);
        SysMenu menu = SysMenu.create(
                command.parentId(), command.name(), command.code(), command.path(),
                command.component(), command.icon(), command.type(), command.sort(), command.visible());
        save(menu);
        return menu;
    }

    @Override
    @Transactional
    public SysMenu update(Long id, MenuUpdateCommand command) {
        SysMenu existing = requireMenu(id);
        boolean exists = lambdaQuery()
                .eq(SysMenu::getCode, command.code())
                .ne(SysMenu::getId, id)
                .count() > 0;
        if (exists) throw new BizException(SystemErrorCode.MENU_CODE_DUPLICATE);
        existing.updateEditableFields(
                command.parentId(), command.name(), command.code(), command.path(),
                command.component(), command.icon(), command.type(), command.sort(), command.visible());
        updateById(existing);
        return existing;
    }

    @Override
    @Transactional
    public void show(Long id) {
        SysMenu menu = requireMenu(id);
        menu.show();
        updateById(menu);
    }

    @Override
    @Transactional
    public void hide(Long id) {
        SysMenu menu = requireMenu(id);
        menu.hide();
        updateById(menu);
    }

    @Override
    @Transactional
    public void enable(Long id) {
        SysMenu menu = requireMenu(id);
        menu.enable();
        updateById(menu);
    }

    @Override
    @Transactional
    public void disable(Long id) {
        SysMenu menu = requireMenu(id);
        menu.disable();
        updateById(menu);
    }

    @Override
    @Transactional
    public void remove(Long id) {
        requireMenu(id);
        List<Long> menuIds = collectDescendantIds(id);
        boolean hasPermissions = permissionMapper.selectCount(Wrappers.<SysPermission>lambdaQuery()
                .in(SysPermission::getMenuId, menuIds)) > 0;
        if (hasPermissions) throw new BizException(SystemErrorCode.MENU_HAS_PERMISSIONS);

        roleMenuMapper.delete(Wrappers.<SysRoleMenu>lambdaQuery().in(SysRoleMenu::getMenuId, menuIds));
        menuIds.forEach(this::removeById);
    }

    private List<Long> collectDescendantIds(Long rootId) {
        List<SysMenu> menus = listAll();
        Set<Long> collected = new HashSet<>();
        collected.add(rootId);

        boolean changed;
        do {
            changed = false;
            for (SysMenu menu : menus) {
                if (menu.getId() != null
                        && menu.getParentId() != null
                        && collected.contains(menu.getParentId())
                        && collected.add(menu.getId())) {
                    changed = true;
                }
            }
        } while (changed);

        return new ArrayList<>(collected);
    }

    enum SystemErrorCode implements BaseErrorCode {
        MENU_NOT_FOUND(404, "error.menu.not_found"),
        MENU_CODE_DUPLICATE(409, "error.menu.code_duplicate"),
        MENU_HAS_PERMISSIONS(409, "error.menu.has_permissions");

        private final int code;
        private final String messageKey;

        SystemErrorCode(int code, String messageKey) {
            this.code = code;
            this.messageKey = messageKey;
        }

        @Override public int code() { return code; }
        @Override public String messageKey() { return messageKey; }
    }
}
