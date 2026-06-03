package com.manpowergroup.kintai.system.application.command.org;

import com.manpowergroup.kintai.common.enums.Status;

public record CompanyCreateCommand(Long parentId, String name, String companyCode, Integer level, Integer sort, Status status) {
}
