package com.manpowergroup.kintai.system.application.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.system.application.dto.sys.RoleAuthorizationRequest;
import com.manpowergroup.kintai.system.application.dto.sys.RoleAuthorizationResponse;
import com.manpowergroup.kintai.system.domain.entity.sys.SysRole;

import java.util.List;

public interface SysRoleService extends IService<SysRole> {

    SysRole getById(Long id);

    PageResult<SysRole> page(Long companyId, PageRequest request);

    List<SysRole> listByCompany(Long companyId);

    SysRole create(SysRole role);

    SysRole update(Long id, SysRole role);

    void assignMenus(Long roleId, List<Long> menuIds);

    void assignPermissions(Long roleId, List<Long> permissionIds);

    RoleAuthorizationResponse getAuthorization(Long roleId);

    void saveAuthorization(Long roleId, RoleAuthorizationRequest request);

    void enable(Long id);

    void disable(Long id);

    void remove(Long id);
}
