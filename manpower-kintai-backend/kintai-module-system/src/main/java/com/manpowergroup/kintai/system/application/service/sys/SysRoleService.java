package com.manpowergroup.kintai.system.application.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.system.application.command.sys.RoleCreateCommand;
import com.manpowergroup.kintai.system.application.command.sys.RoleUpdateCommand;
import com.manpowergroup.kintai.system.application.dto.sys.RoleAuthorizationResponse;
import com.manpowergroup.kintai.system.application.dto.sys.request.RoleAuthorizationSaveRequest;
import com.manpowergroup.kintai.system.domain.entity.sys.SysRole;

import java.util.List;

public interface SysRoleService extends IService<SysRole> {

    SysRole getById(Long id);

    PageResult<SysRole> page(Long companyId, PageRequest request);

    List<SysRole> listByCompany(Long companyId);

    SysRole create(RoleCreateCommand command);

    SysRole update(Long id, RoleUpdateCommand command);

    void assignMenus(Long roleId, List<Long> menuIds);

    void assignPermissions(Long roleId, List<Long> permissionIds);

    RoleAuthorizationResponse getAuthorization(Long roleId);

    void saveAuthorization(Long roleId, RoleAuthorizationSaveRequest request);

    void enable(Long id);

    void disable(Long id);

    void remove(Long id);
}
