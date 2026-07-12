package com.manpowergroup.kintai.system.domain.entity.sys;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.manpowergroup.kintai.common.exception.BizException;
import com.manpowergroup.kintai.common.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter(AccessLevel.PRIVATE)
@Accessors(chain = true)
@TableName("sys_employee_role")
/** 社員へのロール付与期間を管理する。 */
public class SysEmployeeRole {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long employeeId;
    private Long roleId;
    private Long companyId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long createdBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    private Long updatedBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableLogic
    private Integer isDeleted;

    /** 期間の前後関係を検証して社員へロールを付与する。 */
    public static SysEmployeeRole assign(Long employeeId, Long roleId, Long companyId,
                                         LocalDate startDate, LocalDate endDate) {
        validateDateRange(startDate, endDate);
        return new SysEmployeeRole()
                .setEmployeeId(employeeId)
                .setRoleId(roleId)
                .setCompanyId(companyId)
                .setStartDate(startDate)
                .setEndDate(endDate);
    }

    /** ロール付与の有効期間を変更する。 */
    public void changeValidity(LocalDate startDate, LocalDate endDate) {
        validateDateRange(startDate, endDate);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /** 指定日にロール付与が有効か判定する。 */
    public boolean isEffectiveOn(LocalDate date) {
        return (startDate == null || !date.isBefore(startDate))
                && (endDate == null || !date.isAfter(endDate));
    }

    private static void validateDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
            throw BizException.withDetail(ErrorCode.VALIDATION_ERROR,
                    "employee role endDate must not be before startDate");
        }
    }
}
