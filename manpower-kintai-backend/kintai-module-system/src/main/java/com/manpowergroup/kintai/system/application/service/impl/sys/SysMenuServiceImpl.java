package com.manpowergroup.kintai.system.application.service.impl.sys;

import com.manpowergroup.kintai.common.exception.BaseErrorCode;
import com.manpowergroup.kintai.common.exception.BizException;
import com.manpowergroup.kintai.system.application.command.sys.MenuCreateCommand;
import com.manpowergroup.kintai.system.application.command.sys.MenuUpdateCommand;
import com.manpowergroup.kintai.system.application.service.sys.SysMenuService;
import com.manpowergroup.kintai.system.domain.entity.sys.SysEmployeeRole;
import com.manpowergroup.kintai.system.domain.entity.sys.SysMenu;
import com.manpowergroup.kintai.system.domain.entity.sys.SysRoleMenu;
import com.manpowergroup.kintai.system.domain.repository.sys.SysEmployeeRoleRepository;
import com.manpowergroup.kintai.system.domain.repository.sys.SysMenuRepository;
import com.manpowergroup.kintai.system.domain.repository.sys.SysPermissionRepository;
import com.manpowergroup.kintai.system.domain.repository.sys.SysRoleMenuRepository;
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
public class SysMenuServiceImpl implements SysMenuService {

    private final SysMenuRepository menuRepository;
    private final SysEmployeeRoleRepository employeeRoleRepository;
    private final SysRoleMenuRepository roleMenuRepository;
    private final SysPermissionRepository permissionRepository;

    @Override
    public SysMenu getById(Long id) {
        return requireMenu(id);
    }

    private SysMenu requireMenu(Long id) {
        SysMenu menu = menuRepository.getById(id);
        if (menu == null) throw new BizException(SystemErrorCode.MENU_NOT_FOUND);
        return menu;
    }

    @Override
    public List<SysMenu> listAll() {
        return menuRepository.listAllOrderBySort();
    }

    @Override
    public List<SysMenu> listByEmployeeId(Long employeeId) {
        LocalDate today = LocalDate.now();
        List<Long> roleIds = employeeRoleRepository.listByEmployee(employeeId)
            .stream()
            .filter(assignment -> assignment.isEffectiveOn(today))
            .map(SysEmployeeRole::getRoleId)
            .collect(Collectors.toList());
        if (roleIds.isEmpty()) return Collections.emptyList();

        List<Long> menuIds = roleMenuRepository.listByRoleIds(roleIds)
            .stream().map(SysRoleMenu::getMenuId).distinct().collect(Collectors.toList());
        if (menuIds.isEmpty()) return Collections.emptyList();

        return menuRepository.listByIdsOrderBySort(menuIds);
    }

    @Override
    @Transactional
    public SysMenu create(MenuCreateCommand command) {
        if (menuRepository.existsByCode(command.code())) throw new BizException(SystemErrorCode.MENU_CODE_DUPLICATE);
        SysMenu menu = SysMenu.create(
            command.parentId(), command.name(), command.code(), command.path(),
            command.component(), command.icon(), command.type(), command.sort(), command.visible());
        menuRepository.save(menu);
        return menu;
    }

    @Override
    @Transactional
    public SysMenu update(Long id, MenuUpdateCommand command) {
        SysMenu existing = requireMenu(id);

        if (menuRepository.existsByCodeExcludingId(id, command.code()))
            throw new BizException(SystemErrorCode.MENU_CODE_DUPLICATE);
        existing.updateEditableFields(
            command.parentId(), command.name(), command.code(), command.path(),
            command.component(), command.icon(), command.type(), command.sort(), command.visible());
        menuRepository.updateById(existing);
        return existing;
    }

    @Override
    @Transactional
    public void show(Long id) {
        SysMenu menu = requireMenu(id);
        menu.show();
        menuRepository.updateById(menu);
    }

    @Override
    @Transactional
    public void hide(Long id) {
        SysMenu menu = requireMenu(id);
        menu.hide();
        menuRepository.updateById(menu);
    }

    @Override
    @Transactional
    public void enable(Long id) {
        SysMenu menu = requireMenu(id);
        menu.enable();
        menuRepository.updateById(menu);
    }

    @Override
    @Transactional
    public void disable(Long id) {
        SysMenu menu = requireMenu(id);
        menu.disable();
        menuRepository.updateById(menu);
    }

    @Override
    @Transactional
    public void remove(Long id) {
        requireMenu(id);
        List<Long> menuIds = collectDescendantIds(id);
        if (permissionRepository.existsByMenuIds(menuIds)) throw new BizException(SystemErrorCode.MENU_HAS_PERMISSIONS);

        roleMenuRepository.deleteByMenuIds(menuIds);
        menuIds.forEach(menuRepository::deleteById);
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

        @Override
        public int code() {
            return code;
        }

        @Override
        public String messageKey() {
            return messageKey;
        }
    }
}
