package com.manpowergroup.kintai.system.application.service.impl.sys;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.common.exception.BaseErrorCode;
import com.manpowergroup.kintai.common.exception.BizException;
import com.manpowergroup.kintai.system.domain.entity.sys.SysRole;
import com.manpowergroup.kintai.system.domain.entity.sys.SysRoleMenu;
import com.manpowergroup.kintai.system.domain.entity.sys.SysRolePermission;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysRoleMapper;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysRoleMenuMapper;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysRolePermissionMapper;
import com.manpowergroup.kintai.system.application.service.sys.SysRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// ロールマスタサービス実装（アプリケーション層）
@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole>
        implements SysRoleService {

    private final SysRoleMenuMapper roleMenuMapper;
    private final SysRolePermissionMapper rolePermissionMapper;

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
                .eq(SysRole::getCompanyId, companyId)
                .orderByAsc(SysRole::getSort)
                .getWrapper());
        return PageResult.of(p);
    }

    @Override
    public List<SysRole> listByCompany(Long companyId) {
        return lambdaQuery()
                .eq(SysRole::getCompanyId, companyId)
                .orderByAsc(SysRole::getSort)
                .list();
    }

    @Override
    @Transactional
    public SysRole create(SysRole role) {
        boolean exists = lambdaQuery()
                .eq(SysRole::getCompanyId, role.getCompanyId())
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
                .eq(SysRole::getCompanyId, role.getCompanyId())
                .eq(SysRole::getCode, role.getCode())
                .ne(SysRole::getId, id)
                .count() > 0;
        if (exists) throw new BizException(SystemErrorCode.ROLE_CODE_DUPLICATE);
        existing.setName(role.getName())
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
        roleMenuMapper.delete(Wrappers.<SysRoleMenu>lambdaQuery().eq(SysRoleMenu::getRoleId, roleId));
        menuIds.stream()
                .map(menuId -> new SysRoleMenu().setRoleId(roleId).setMenuId(menuId))
                .forEach(roleMenuMapper::insert);
    }

    @Override
    @Transactional
    public void assignPermissions(Long roleId, List<Long> permissionIds) {
        getById(roleId);
        rolePermissionMapper.delete(Wrappers.<SysRolePermission>lambdaQuery().eq(SysRolePermission::getRoleId, roleId));
        permissionIds.stream()
                .map(pid -> new SysRolePermission().setRoleId(roleId).setPermissionId(pid))
                .forEach(rolePermissionMapper::insert);
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
        removeById(id);
    }

    enum SystemErrorCode implements BaseErrorCode {
        ROLE_NOT_FOUND(404, "error.role.not_found"),
        ROLE_CODE_DUPLICATE(409, "error.role.code_duplicate");

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

