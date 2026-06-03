package com.manpowergroup.kintai.system.application.dto.org;

import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.system.domain.entity.org.OrgCompany;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CompanyResponse {

    private Long id;
    private Long parentId;
    private String name;
    private String companyCode;
    private Integer level;
    private Integer sort;
    private Status status;

    public static CompanyResponse from(OrgCompany company) {
        return CompanyResponse.builder()
                .id(company.getId())
                .parentId(company.getParentId())
                .name(company.getName())
                .companyCode(company.getCompanyCode())
                .level(company.getLevel())
                .sort(company.getSort())
                .status(company.getStatus())
                .build();
    }
}
