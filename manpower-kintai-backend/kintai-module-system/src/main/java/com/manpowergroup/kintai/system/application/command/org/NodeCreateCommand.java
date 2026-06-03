package com.manpowergroup.kintai.system.application.command.org;

import com.manpowergroup.kintai.common.enums.Status;

public record NodeCreateCommand(Long companyId, Long parentId, Long managerId, String name, String typeCode,
                                String deptFunction, String code, Integer level, Integer sort, Status status) {
}
