package com.manpowergroup.kintai.system.application.service.impl.sys;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.common.exception.BaseErrorCode;
import com.manpowergroup.kintai.common.exception.BizException;
import com.manpowergroup.kintai.system.application.dto.sys.MenuResponse;
import com.manpowergroup.kintai.system.application.dto.sys.RoleAuthorizationRequest;
import com.manpowergroup.kintai.system.application.dto.sys.RoleAuthorizationResponse;
import com.manpowergroup.kintai.system.application.service.sys.SysRoleService;
import com.manpowergroup.kintai.system.domain.entity.sys.SysEmployeeRole;
import com.manpowergroup.kintai.system.domain.entity.sys.SysMenu;
import com.manpowergroup.kintai.system.domain.entity.sys.SysPermission;
import com.manpowergroup.kintai.system.domain.entity.sys.SysRole;
import com.manpowergroup.kintai.system.domain.entity.sys.SysRoleMenu;
import com.manpowergroup.kintai.system.domain.entity.sys.SysRolePermission;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysEmployeeRoleMapper;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysMenuMapper;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysPermissionMapper;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysRoleMapper;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysRoleMenuMapper;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysRolePermissionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole>
        implements SysRoleService {

    private final SysRoleMenuMapper roleMenuMapper;
    private final SysRolePermissionMapper rolePermissionMapper;
    private final SysEmployeeRoleMapper employeeRoleMapper;
    private final SysMenuMapper menuMapper;
    private final SysPermissionMapper permissionMapper;

    @Override
    public SysRole getById(Long id) {
        SysRole role = super.getById(id);
        if (role == null) throw new BizException(SystemErrorCode.ROLE_NOT_FOUND);
        return role;
    }

    @Override
    public PageResult<SysRole> page(Long companyId, PageRequest request) {
        Page<SysRole> p = new Page<>(request.page(), request.size());
        page(p, lambdaQuery()
                .eq(companyId != null, SysRole::getCompanyId, companyId)
                .orderByAsc(SysRole::getSort)
                .getWrapper());
        return PageResult.of(p);
    }

    @Override
    public List<SysRole> listByCompany(Long companyId) {
        return lambdaQuery()
                .eq(companyId != null, SysRole::getCompanyId, companyId)
                .orderByAsc(SysRole::getSort)
                .list();
    }

    @Override
    @Transactional
    public SysRole create(SysRole role) {
        boolean exists = lambdaQuery()
                .eq(role.getCompanyId() != null, SysRole::getCompanyId, role.getCompanyId())
                .isNull(role.getCompanyId() == null, SysRole::getCompanyId)
                .eq(SysRole::getCode, role.getCode())
                .count() > 0;
        if (exists) throw new BizException(SystemErrorCode.ROLE_CODE_DUPLICATE);
        save(role);
        return role;
    }

    @Override
    @Transactional
    public SysRole update(Long id, SysRole role) {
        SysRole existing = getById(id);
        boolean exists = lambdaQuery()
                .eq(role.getCompanyId() != null, SysRole::getCompanyId, role.getCompanyId())
                .isNull(role.getCompanyId() == null, SysRole::getCompanyId)
                .eq(SysRole::getCode, role.getCode())
                .ne(SysRole::getId, id)
                .count() > 0;
        if (exists) throw new BizException(SystemErrorCode.ROLE_CODE_DUPLICATE);
        existing.setCompanyId(role.getCompanyId())
                .setName(role.getName())
                .setCode(role.getCode())
                .setRemark(role.getRemark())
                .setSort(role.getSort());
        updateById(existing);
        return existing;
    }

    @Override
    @Transactional
    public void assignMenus(Long roleId, List<Long> menuIds) {
        getById(roleId);
        replaceMenus(roleId, normalizeIds(menuIds));
    }

    @Override
    @Transactional
    public void assignPermissions(Long roleId, List<Long> permissionIds) {
        getById(roleId);
        replacePermissions(roleId, normalizeIds(permissionIds));
    }

    @Override
    public RoleAuthorizationResponse getAuthorization(Long roleId) {
        getById(roleId);
        List<Long> selectedMenuIds = roleMenuMapper.selectList(Wrappers.<SysRoleMenu>lambdaQuery()
                        .eq(SysRoleMenu::getRoleId, roleId))
                .stream()
                .map(SysRoleMenu::getMenuId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
        List<Long> selectedPermissionIds = rolePermissionMapper.selectList(Wrappers.<SysRolePermission>lambdaQuery()
                        .eq(SysRolePermission::getRoleId, roleId))
                .stream()
                .map(SysRolePermission::getPermissionId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
        List<MenuResponse> menus = menuMapper.selectList(Wrappers.<SysMenu>lambdaQuery()
                        .orderByAsc(SysMenu::getSort))
                .stream()
                .map(MenuResponse::from)
                .toList();
        List<SysPermission> permissions = permissionMapper.selectList(Wrappers.<SysPermission>lambdaQuery()
                .orderByAsc(SysPermission::getSort));

        return RoleAuthorizationResponse.builder()
                .menus(menus)
                .permissions(permissions)
                .selectedMenuIds(selectedMenuIds)
                .selectedPermissionIds(selectedPermissionIds)
                .build();
    }

    @Override
    @Transactional
    public void saveAuthorization(Long roleId, RoleAuthorizationRequest request) {
        getById(roleId);
        replaceMenus(roleId, normalizeIds(request == null ? null : request.getMenuIds()));
        replacePermissions(roleId, normalizeIds(request == null ? null : request.getPermissionIds()));
    }

    @Override
    @Transactional
    public void enable(Long id) {
        SysRole role = getById(id);
        role.setStatus(Status.ENABLED);
        updateById(role);
    }

    @Override
    @Transactional
    public void disable(Long id) {
        SysRole role = getById(id);
        role.setStatus(Status.DISABLED);
        updateById(role);
    }

    @Override
    @Transactional
    public void remove(Long id) {
        getById(id);
        boolean assignedToEmployee = employeeRoleMapper.selectCount(Wrappers.<SysEmployeeRole>lambdaQuery()
                .eq(SysEmployeeRole::getRoleId, id)) > 0;
        if (assignedToEmployee) throw new BizException(SystemErrorCode.ROLE_ASSIGNED_TO_EMPLOYEE);
        roleMenuMapper.delete(Wrappers.<SysRoleMenu>lambdaQuery().eq(SysRoleMenu::getRoleId, id));
        rolePermissionMapper.delete(Wrappers.<SysRolePermission>lambdaQuery().eq(SysRolePermission::getRoleId, id));
        removeById(id);
    }

    private void replaceMenus(Long roleId, List<Long> menuIds) {
        roleMenuMapper.delete(Wrappers.<SysRoleMenu>lambdaQuery().eq(SysRoleMenu::getRoleId, roleId));
        menuIds.stream()
                .map(menuId -> new SysRoleMenu().setRoleId(roleId).setMenuId(menuId))
                .forEach(roleMenuMapper::insert);
    }

    private void replacePermissions(Long roleId, List<Long> permissionIds) {
        rolePermissionMapper.delete(Wrappers.<SysRolePermission>lambdaQuery().eq(SysRolePermission::getRoleId, roleId));
        permissionIds.stream()
                .map(permissionId -> new SysRolePermission().setRoleId(roleId).setPermissionId(permissionId))
                .forEach(rolePermissionMapper::insert);
    }

    private List<Long> normalizeIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return Collections.emptyList();
        return ids.stream()
                .filter(Objects::nonNull)
                .distinct()
                .toList();
    }

    enum SystemErrorCode implements BaseErrorCode {
        ROLE_NOT_FOUND(404, "error.role.not_found"),
        ROLE_CODE_DUPLICATE(409, "error.role.code_duplicate"),
        ROLE_ASSIGNED_TO_EMPLOYEE(409, "error.role.assigned_to_employee");

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
