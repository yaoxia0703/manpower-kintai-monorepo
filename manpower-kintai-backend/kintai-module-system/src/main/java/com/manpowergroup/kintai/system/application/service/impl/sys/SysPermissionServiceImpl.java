package com.manpowergroup.kintai.system.application.service.impl.sys;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.common.exception.BaseErrorCode;
import com.manpowergroup.kintai.common.exception.BizException;
import com.manpowergroup.kintai.system.application.command.sys.PermissionCreateCommand;
import com.manpowergroup.kintai.system.application.command.sys.PermissionUpdateCommand;
import com.manpowergroup.kintai.system.application.service.sys.SysPermissionService;
import com.manpowergroup.kintai.system.domain.entity.sys.SysEmployeeRole;
import com.manpowergroup.kintai.system.domain.entity.sys.SysPermission;
import com.manpowergroup.kintai.system.domain.entity.sys.SysRolePermission;
import com.manpowergroup.kintai.system.domain.repository.sys.SysEmployeeRoleRepository;
import com.manpowergroup.kintai.system.domain.repository.sys.SysMenuRepository;
import com.manpowergroup.kintai.system.domain.repository.sys.SysPermissionRepository;
import com.manpowergroup.kintai.system.domain.repository.sys.SysRolePermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SysPermissionServiceImpl implements SysPermissionService {

    private final SysPermissionRepository permissionRepository;
    private final SysRolePermissionRepository rolePermissionRepository;
    private final SysEmployeeRoleRepository employeeRoleRepository;
    private final SysMenuRepository menuRepository;

    @Override
    public SysPermission getById(Long id) {
        return requirePermission(id);
    }

    private SysPermission requirePermission(Long id) {
        SysPermission permission = permissionRepository.findById(id);
        if (permission == null) throw new BizException(SystemErrorCode.PERMISSION_NOT_FOUND);
        return permission;
    }

    @Override
    public PageResult<SysPermission> page(Long menuId, String keyword, PageRequest request) {
        List<Long> menuIds = List.of();
        if (menuId != null) {
            menuIds = menuRepository.listSelfAndDescendantIds(menuId);
            if (menuIds.isEmpty()) {
                return PageResult.empty(request.page(), request.size());
            }
        }
        return permissionRepository.findPageByMenuIdsAndKeyword(
            menuIds, keyword, request.page(), request.size());
    }


    private PageResult<SysPermission> emptyPage(PageRequest request) {
        Page<SysPermission> page = new Page<>(request.page(), request.size());
        page.setRecords(List.of());
        page.setTotal(0L);
        return PageResult.of(page);
    }

    @Override
    public List<SysPermission> listByMenu(Long menuId) {
        return permissionRepository.listByMenuOrderBySort(menuId);
    }

    @Override
    public List<SysPermission> listByEmployeeId(Long employeeId) {
        LocalDate today = LocalDate.now();
        List<Long> roleIds = employeeRoleRepository.listByEmployee(employeeId)
            .stream()
            .filter(assignment -> assignment.isEffectiveOn(today))
            .map(SysEmployeeRole::getRoleId)
            .collect(Collectors.toList());
        if (roleIds.isEmpty()) return Collections.emptyList();

        List<Long> permissionIds = rolePermissionRepository.listByRoleIds(roleIds)
            .stream().map(SysRolePermission::getPermissionId).distinct().collect(Collectors.toList());
        if (permissionIds.isEmpty()) return Collections.emptyList();

        return permissionRepository.listByIdsOrderBySort(permissionIds);
    }

    @Override
    @Transactional
    public SysPermission create(PermissionCreateCommand command) {
        ensureMenuExists(command.menuId());
        ensureCodeUnique(command.code(), null);
        SysPermission permission = SysPermission.create(
            command.menuId(), command.code(), command.name(), command.method(),
            command.path(), command.remark(), command.sort());
        permissionRepository.save(permission);
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
        permissionRepository.updateById(existing);
        return existing;
    }

    @Override
    @Transactional
    public void enable(Long id) {
        SysPermission permission = requirePermission(id);
        permission.enable();
        permissionRepository.updateById(permission);
    }

    @Override
    @Transactional
    public void disable(Long id) {
        SysPermission permission = requirePermission(id);
        permission.disable();
        permissionRepository.updateById(permission);
    }

    @Override
    @Transactional
    public void remove(Long id) {
        requirePermission(id);
        if (rolePermissionRepository.existsByPermissionId(id))
            throw new BizException(SystemErrorCode.PERMISSION_ASSIGNED_TO_ROLE);
        permissionRepository.deleteById(id);
    }

    private void ensureCodeUnique(String code, Long currentId) {

        if (permissionRepository.existsByCodeExcludingId(code, currentId))
            throw new BizException(SystemErrorCode.PERMISSION_CODE_DUPLICATE);
    }

    private void ensureMenuExists(Long menuId) {
        if (!menuRepository.existsById(menuId)) {
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
