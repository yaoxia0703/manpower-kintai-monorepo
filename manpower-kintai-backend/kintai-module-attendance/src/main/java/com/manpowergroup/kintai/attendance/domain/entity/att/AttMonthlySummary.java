package com.manpowergroup.kintai.attendance.domain.entity.att;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.manpowergroup.kintai.attendance.domain.enums.MonthlySummaryStatus;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@TableName("att_monthly_summary")
public class AttMonthlySummary {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long employeeId;

    private Long companyId;

    private String summaryMonth;

    private Integer workDays;

    private Integer absentDays;

    private BigDecimal paidLeaveDays;

    private Integer totalWorkMinutes;

    private Integer totalOvertimeMinutes;

    private Integer lateCount;

    private Integer earlyLeaveCount;

    private MonthlySummaryStatus status;

    private LocalDateTime confirmedAt;

    private Long confirmedBy;

    private Long createdBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    private Long updatedBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableLogic
    private Integer isDeleted;
}
