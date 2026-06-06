package com.manpowergroup.kintai.system.application.dto.manager.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SubordinateEmployeeResponse {

    private Long employeeId;
    private String employeeCode;
    private String displayName;
    private String email;
    private Long companyId;
    private Long nodeId;
    private String nodeName;
    private Long gradeId;
    private String gradeName;
}
