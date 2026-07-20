package com.manpowergroup.kintai.employee.application.assembler.org;

import com.manpowergroup.kintai.employee.application.command.org.CompanyCreateCommand;
import com.manpowergroup.kintai.employee.application.command.org.CompanyUpdateCommand;
import com.manpowergroup.kintai.employee.application.dto.org.response.CompanyResponse;
import com.manpowergroup.kintai.employee.application.dto.org.request.CompanyCreateRequest;
import com.manpowergroup.kintai.employee.application.dto.org.request.CompanyUpdateRequest;
import com.manpowergroup.kintai.employee.domain.entity.org.OrgCompany;

public final class CompanyAssembler {

    private CompanyAssembler() {
    }

    public static CompanyCreateCommand toCommand(CompanyCreateRequest request) {
        return new CompanyCreateCommand(
                request.getParentId(),
                request.getName(),
                request.getCompanyCode(),
                request.getLevel(),
                request.getSort(),
                request.getStatus()
        );
    }

    public static CompanyUpdateCommand toCommand(CompanyUpdateRequest request) {
        return new CompanyUpdateCommand(
                request.getParentId(),
                request.getName(),
                request.getCompanyCode(),
                request.getLevel(),
                request.getSort(),
                request.getStatus()
        );
    }

    public static CompanyResponse toResponse(OrgCompany company) {
        return CompanyResponse.from(company);
    }
}
