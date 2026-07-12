package com.manpowergroup.kintai.attendance.domain.entity.att;

import com.baomidou.mybatisplus.annotation.*;
import com.manpowergroup.kintai.common.exception.BizException;
import com.manpowergroup.kintai.common.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 年度ごとの有給休暇残数を管理し、取得日数と失効日数の整合性を保証する。
 */
@Getter
@Setter(AccessLevel.PRIVATE)
@Accessors(chain = true)
@TableName("att_paid_leave_balance")
public class AttPaidLeaveBalance {

    @TableId(type = IdType.AUTO)
    @Setter
    // 有給残数ID
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

    // 作成者ID
    private Long createdBy;

    // 作成日時
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    // 更新者ID
    private Long updatedBy;

    // 更新日時
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    // 論理削除（0=有効 1=削除）
    @TableLogic
    private Integer isDeleted;

    /** 付与日数を全残数として新しい有給休暇残高を作成する。 */
    public static AttPaidLeaveBalance grant(Long employeeId, Long companyId, String fiscalYear,
                                            BigDecimal grantedDays, LocalDate expireDate) {
        return new AttPaidLeaveBalance()
                .setEmployeeId(employeeId)
                .setCompanyId(companyId)
                .setFiscalYear(fiscalYear)
                .setGrantedDays(grantedDays)
                .setUsedDays(BigDecimal.ZERO)
                .setExpiredDays(BigDecimal.ZERO)
                .setBalanceDays(grantedDays)
                .setExpireDate(expireDate);
    }

    /** 有給取得日数を消化し、利用済み日数と残数を更新する。 */
    public void useDays(BigDecimal days) {
        requirePositiveDays(days);
        requireEnoughBalance(days);
        this.usedDays = safeDays(this.usedDays).add(days);
        this.balanceDays = safeDays(this.balanceDays).subtract(days);
    }

    /** 有給日数を失効させ、失効日数と残数を更新する。 */
    public void expireDays(BigDecimal days) {
        requirePositiveDays(days);
        requireEnoughBalance(days);
        this.expiredDays = safeDays(this.expiredDays).add(days);
        this.balanceDays = safeDays(this.balanceDays).subtract(days);
    }

    private void requirePositiveDays(BigDecimal days) {
        if (days == null || days.compareTo(BigDecimal.ZERO) <= 0) {
            throw BizException.withDetail(ErrorCode.VALIDATION_ERROR, "paid leave days must be positive");
        }
    }

    private void requireEnoughBalance(BigDecimal days) {
        if (safeDays(this.balanceDays).compareTo(days) < 0) {
            throw BizException.withDetail(ErrorCode.CONFLICT, "paid leave balance is insufficient");
        }
    }

    private BigDecimal safeDays(BigDecimal days) {
        return days == null ? BigDecimal.ZERO : days;
    }
}
