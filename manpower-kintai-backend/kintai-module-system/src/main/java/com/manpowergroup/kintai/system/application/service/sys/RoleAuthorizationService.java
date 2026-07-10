package com.manpowergroup.kintai.system.application.service.sys;

import com.manpowergroup.kintai.system.application.command.sys.RoleAuthorizationSaveCommand;
import com.manpowergroup.kintai.system.application.command.sys.RoleMenuAssignCommand;
import com.manpowergroup.kintai.system.application.command.sys.RolePermissionAssignCommand;
import com.manpowergroup.kintai.system.application.dto.sys.response.RoleAuthorizationResponse;

public interface RoleAuthorizationService {

    RoleAuthorizationResponse getAuthorization(Long roleId);

    void assignMenus(RoleMenuAssignCommand command);

    void assignPermissions(RolePermissionAssignCommand command);

    void saveAuthorization(RoleAuthorizationSaveCommand command);
}
