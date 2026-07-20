package com.manpowergroup.kintai.system.application.service.impl.sys;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.common.exception.BaseErrorCode;
import com.manpowergroup.kintai.common.exception.BizException;
import com.manpowergroup.kintai.system.application.command.sys.PermissionCreateCommand;
import com.manpowergroup.kintai.system.application.command.sys.PermissionUpdateCommand;
import com.manpowergroup.kintai.system.application.service.sys.SysPermissionService;
import com.manpowergroup.kintai.system.domain.entity.sys.SysEmployeeRole;
import com.manpowergroup.kintai.system.domain.entity.sys.SysMenu;
import com.manpowergroup.kintai.system.domain.entity.sys.SysPermission;
import com.manpowergroup.kintai.system.domain.entity.sys.SysRolePermission;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysEmployeeRoleMapper;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysMenuMapper;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysPermissionMapper;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysRolePermissionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission>
        implements SysPermissionService {

    private final SysEmployeeRoleMapper employeeRoleMapper;
    private final SysRolePermissionMapper rolePermissionMapper;
    private final SysMenuMapper menuMapper;

    @Override
    public SysPermission getById(Long id) {
        return requirePermission(id);
    }

    private SysPermission requirePermission(Long id) {
        SysPermission permission = super.getById(id);
        if (permission == null) throw new BizException(SystemErrorCode.PERMISSION_NOT_FOUND);
        return permission;
    }

    @Override
    public PageResult<SysPermission> page(Long menuId, String keyword, PageRequest request) {
        List<Long> menuIds = menuId == null ? List.of() : collectDescendantMenuIds(menuId);
        if (menuId != null && menuIds.isEmpty()) {
            return emptyPage(request);
        }

        String normalizedKeyword = StringUtils.hasText(keyword) ? keyword.trim() : null;
        LambdaQueryWrapper<SysPermission> wrapper = Wrappers.lambdaQuery(SysPermission.class)
                .in(menuId != null, SysPermission::getMenuId, menuIds);
        if (normalizedKeyword != null) {
            String codePattern = "%" + escapeLikeLiteral(normalizedKeyword.toLowerCase(Locale.ROOT)) + "%";
            String namePattern = "%" + escapeLikeLiteral(normalizedKeyword) + "%";
            wrapper.and(query -> query
                    .apply("LOWER(code) LIKE {0} ESCAPE '!'", codePattern)
                    .or()
                    .apply("name LIKE {0} ESCAPE '!'", namePattern));
        }
        wrapper.orderByAsc(SysPermission::getSort).orderByAsc(SysPermission::getId);

        Page<SysPermission> page = new Page<>(request.page(), request.size());
        baseMapper.selectPage(page, wrapper);
        return PageResult.of(page);
    }

    private String escapeLikeLiteral(String value) {
        return value
                .replace("!", "!!")
                .replace("%", "!%")
                .replace("_", "!_");
    }

    private List<Long> collectDescendantMenuIds(Long rootId) {
        List<SysMenu> menus = menuMapper.selectList(Wrappers.<SysMenu>lambdaQuery()
                .select(SysMenu::getId, SysMenu::getParentId));
        if (menus.stream().noneMatch(menu -> rootId.equals(menu.getId()))) {
            return List.of();
        }

        Set<Long> collected = new LinkedHashSet<>();
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
        return List.copyOf(collected);
    }

    private PageResult<SysPermission> emptyPage(PageRequest request) {
        Page<SysPermission> page = new Page<>(request.page(), request.size());
        page.setRecords(List.of());
        page.setTotal(0L);
        return PageResult.of(page);
    }

    @Override
    public List<SysPermission> listByMenu(Long menuId) {
        return lambdaQuery()
                .eq(SysPermission::getMenuId, menuId)
                .orderByAsc(SysPermission::getSort)
                .list();
    }

    @Override
    public List<SysPermission> listByEmployeeId(Long employeeId) {
        LocalDate today = LocalDate.now();
        List<Long> roleIds = employeeRoleMapper.selectList(Wrappers.<SysEmployeeRole>lambdaQuery()
                        .eq(SysEmployeeRole::getEmployeeId, employeeId))
                .stream()
                .filter(assignment -> assignment.isEffectiveOn(today))
                .map(SysEmployeeRole::getRoleId)
                .collect(Collectors.toList());
        if (roleIds.isEmpty()) return Collections.emptyList();

        List<Long> permissionIds = rolePermissionMapper.selectList(Wrappers.<SysRolePermission>lambdaQuery()
                        .in(SysRolePermission::getRoleId, roleIds))
                .stream().map(SysRolePermission::getPermissionId).distinct().collect(Collectors.toList());
        if (permissionIds.isEmpty()) return Collections.emptyList();

        return lambdaQuery()
                .in(SysPermission::getId, permissionIds)
                .orderByAsc(SysPermission::getSort)
                .list();
    }

    @Override
    @Transactional
    public SysPermission create(PermissionCreateCommand command) {
        ensureMenuExists(command.menuId());
        ensureCodeUnique(command.code(), null);
        SysPermission permission = SysPermission.create(
                command.menuId(), command.code(), command.name(), command.method(),
                command.path(), command.remark(), command.sort());
        save(permission);
        return permission;
    }

    @Override
    @Transactional
    public SysPermission update(Long id, PermissionUpdateCommand command) {
        SysPermission existing = requirePermission(id);
        ensureMenuExists(command.menuId());
        ensureCodeUnique(command.code(), id);
        existing.updateEditableFields(
                command.menuId(), command.code(), command.name(), command.method(),
                command.path(), command.remark(), command.sort());
        updateById(existing);
        return existing;
    }

    @Override
    @Transactional
    public void enable(Long id) {
        SysPermission permission = requirePermission(id);
        permission.enable();
        updateById(permission);
    }

    @Override
    @Transactional
    public void disable(Long id) {
        SysPermission permission = requirePermission(id);
        permission.disable();
        updateById(permission);
    }

    @Override
    @Transactional
    public void remove(Long id) {
        requirePermission(id);
        boolean assignedToRole = rolePermissionMapper.selectCount(Wrappers.<SysRolePermission>lambdaQuery()
                .eq(SysRolePermission::getPermissionId, id)) > 0;
        if (assignedToRole) throw new BizException(SystemErrorCode.PERMISSION_ASSIGNED_TO_ROLE);
        removeById(id);
    }

    private void ensureCodeUnique(String code, Long currentId) {
        boolean exists = lambdaQuery()
                .eq(SysPermission::getCode, code)
                .ne(currentId != null, SysPermission::getId, currentId)
                .count() > 0;
        if (exists) throw new BizException(SystemErrorCode.PERMISSION_CODE_DUPLICATE);
    }

    private void ensureMenuExists(Long menuId) {
        if (menuId == null || menuMapper.selectById(menuId) == null) {
            throw new BizException(SystemErrorCode.PERMISSION_MENU_NOT_FOUND);
        }
    }

    enum SystemErrorCode implements BaseErrorCode {
        PERMISSION_NOT_FOUND(404, "error.permission.not_found"),
        PERMISSION_CODE_DUPLICATE(409, "error.permission.code_duplicate"),
        PERMISSION_ASSIGNED_TO_ROLE(409, "error.permission.assigned_to_role"),
        PERMISSION_MENU_NOT_FOUND(400, "error.permission.menu_not_found");

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
