package com.manpowergroup.kintai.system.application.dto.emp;

import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.system.domain.entity.emp.EmpAccount;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AccountResponse {

    private Long id;
    private Long employeeId;
    private String username;
    private LocalDateTime lastLogin;
    private Status status;

    public static AccountResponse from(EmpAccount account) {
        return AccountResponse.builder()
                .id(account.getId())
                .employeeId(account.getEmployeeId())
                .username(account.getUsername())
                .lastLogin(account.getLastLogin())
                .status(account.getStatus())
                .build();
    }
}
