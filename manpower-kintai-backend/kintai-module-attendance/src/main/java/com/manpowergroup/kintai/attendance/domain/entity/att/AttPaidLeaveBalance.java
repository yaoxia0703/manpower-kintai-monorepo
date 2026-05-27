package com.manpowergroup.kintai.attendance.domain.entity.att;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

// 有給休暇残数管理
@Data
@Accessors(chain = true)
@TableName("att_paid_leave_balance")
public class AttPaidLeaveBalance {

    @TableId(type = IdType.AUTO)
    private Long id;

    // 社員ID
    private Long employeeId;

    // 会社ID
    private Long companyId;

    // 対象年度（YYYY）
    private String fiscalYear;

    // 付与日数
    private BigDecimal grantedDays;

    // 取得済日数
    private BigDecimal usedDays;

    // 失効日数
    private BigDecimal expiredDays;

    // 残日数
    private BigDecimal balanceDays;

    // 有効期限
    private LocalDate expireDate;

    private Long createdBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    private Long updatedBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableLogic
    private Integer isDeleted;
}

