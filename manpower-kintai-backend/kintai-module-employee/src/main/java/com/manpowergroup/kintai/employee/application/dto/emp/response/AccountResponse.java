package com.manpowergroup.kintai.employee.application.dto.emp.response;

import io.swagger.v3.oas.annotations.media.Schema;
import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.employee.domain.entity.emp.EmpAccount;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@Schema(description = "Accountレスポンス")
public class AccountResponse {

    @Schema(description = "ID")
    private Long id;
    @Schema(description = "社員ID")
    private Long employeeId;
    @Schema(description = "ユーザー名")
    private String username;
    @Schema(description = "最終ログイン日時")
    private LocalDateTime lastLogin;
    @Schema(description = "ステータス")
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
