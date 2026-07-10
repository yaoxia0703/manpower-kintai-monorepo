package com.manpowergroup.kintai.system.application.dto.org.response;

import io.swagger.v3.oas.annotations.media.Schema;
import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.system.domain.entity.org.OrgCompany;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Companyレスポンス")
public class CompanyResponse {

    @Schema(description = "ID")
    private Long id;
    @Schema(description = "親ID")
    private Long parentId;
    @Schema(description = "名称")
    private String name;
    @Schema(description = "会社コード")
    private String companyCode;
    @Schema(description = "階層レベル")
    private Integer level;
    @Schema(description = "表示順")
    private Integer sort;
    @Schema(description = "ステータス")
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
