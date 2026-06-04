package com.manpowergroup.kintai.system.domain.entity.emp;

import com.baomidou.mybatisplus.annotation.*;
import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.common.exception.BizException;
import com.manpowergroup.kintai.common.exception.ErrorCode;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

// 社員アカウント（ログイン用）
@Data
@Accessors(chain = true)
@TableName("emp_account")
public class EmpAccount {

    @TableId(type = IdType.AUTO)
    private Long id;

    // 社員ID
    private Long employeeId;

    // ユーザー名
    private String username;

    // パスワード（BCrypt）
    private String password;

    // 最終ログイン日時
    private LocalDateTime lastLogin;

    // ステータス
    private Status status;

    private Long createdBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    private Long updatedBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableLogic
    private Integer isDeleted;

    public void changeUsername(String username) {
        this.username = username;
    }

    public void authenticate(String rawPassword, PasswordEncoder passwordEncoder) {
        if (this.status != Status.ENABLED) {
            throw BizException.withDetail(ErrorCode.FORBIDDEN, "Account is disabled");
        }
        if (!passwordEncoder.matches(rawPassword, this.password)) {
            throw BizException.withDetail(ErrorCode.UNAUTHORIZED, "Invalid credentials");
        }
    }

    public void recordLogin(LocalDateTime loggedInAt) {
        this.lastLogin = loggedInAt;
    }

    public void enable() {
        this.status = Status.ENABLED;
    }

    public void disable() {
        this.status = Status.DISABLED;
    }
}

