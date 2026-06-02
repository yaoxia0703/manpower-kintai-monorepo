package com.manpowergroup.kintai.system.application.dto.sys;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RoleAuthorizationRequest {

    private List<Long> menuIds = new ArrayList<>();

    private List<Long> permissionIds = new ArrayList<>();
}
