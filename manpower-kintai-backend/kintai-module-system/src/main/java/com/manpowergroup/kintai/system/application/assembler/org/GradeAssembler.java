package com.manpowergroup.kintai.system.application.assembler.org;

import com.manpowergroup.kintai.system.application.command.org.GradeCreateCommand;
import com.manpowergroup.kintai.system.application.command.org.GradeUpdateCommand;
import com.manpowergroup.kintai.system.application.dto.org.response.GradeResponse;
import com.manpowergroup.kintai.system.application.dto.org.request.GradeCreateRequest;
import com.manpowergroup.kintai.system.application.dto.org.request.GradeUpdateRequest;
import com.manpowergroup.kintai.system.domain.entity.org.OrgGrade;

public final class GradeAssembler {

    private GradeAssembler() {
    }

    public static GradeCreateCommand toCommand(GradeCreateRequest request) {
        return new GradeCreateCommand(
                request.getCompanyId(), request.getName(), request.getCode(), request.getGradeLevel(),
                request.getSort(), request.getStatus()
        );
    }

    public static GradeUpdateCommand toCommand(GradeUpdateRequest request) {
        return new GradeUpdateCommand(
                request.getCompanyId(), request.getName(), request.getCode(), request.getGradeLevel(),
                request.getSort(), request.getStatus()
        );
    }

    public static GradeResponse toResponse(OrgGrade grade) {
        return GradeResponse.from(grade);
    }
}
