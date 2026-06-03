package com.manpowergroup.kintai.system.application.command.org;

import com.manpowergroup.kintai.common.enums.Status;

public record CompanyUpdateCommand(Long parentId, String name, String companyCode, Integer level, Integer sort, Status status) {
}
