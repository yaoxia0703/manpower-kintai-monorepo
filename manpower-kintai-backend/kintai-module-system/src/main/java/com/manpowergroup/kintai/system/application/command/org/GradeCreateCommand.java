package com.manpowergroup.kintai.system.application.command.org;

import com.manpowergroup.kintai.common.enums.Status;

public record GradeCreateCommand(Long companyId, String name, String code, String gradeLevel, Integer sort, Status status) {
}
