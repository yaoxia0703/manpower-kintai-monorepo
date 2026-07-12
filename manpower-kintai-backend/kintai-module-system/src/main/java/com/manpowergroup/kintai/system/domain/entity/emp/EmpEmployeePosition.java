package com.manpowergroup.kintai.system.domain.entity.emp;

import com.baomidou.mybatisplus.annotation.*;
import com.manpowergroup.kintai.common.enums.Status;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

// 社員職位関連（兼任対応）
@Getter
@Setter(AccessLevel.PRIVATE)
@Accessors(chain = true)
@TableName("emp_employee_position")
/** 社員の組織・職級への任命期間と主務区分を管理する。 */
public class EmpEmployeePosition {

    @TableId(type = IdType.AUTO)
    @Setter
    // 社員職位関連ID
    private Long id;

    // 社員ID
    private Long employeeId;

    // 所属会社ID
    private Long companyId;

    // 組織ノードID
    private Long nodeId;

    // 職級ID
    private Long gradeId;

    // 主務フラグ（1=主務 0=兼務）
    private Integer isPrimary;

    // 着任日
    private LocalDate startDate;

    // 離任日（NULLは現在も在任）
    private LocalDate endDate;

    // ステータス
    private Status status;

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

    /** 社員を組織ノードと職級へ任命する。 */
    public static EmpEmployeePosition assign(Long employeeId, Long companyId,
                                             Long nodeId, Long gradeId,
                                             Integer isPrimary, LocalDate startDate,
                                             LocalDate endDate, Status status) {
        return new EmpEmployeePosition()
                .setEmployeeId(employeeId)
                .setCompanyId(companyId)
                .setNodeId(nodeId)
                .setGradeId(gradeId)
                .setIsPrimary(isPrimary)
                .setStartDate(startDate)
                .setEndDate(endDate)
                .setStatus(status == null ? Status.ENABLED : status);
    }

    /** 社員と会社を変更せずに任命内容を更新する。 */
    public void updateAssignment(Long nodeId, Long gradeId, Integer isPrimary,
                                 LocalDate startDate, LocalDate endDate) {
        this.nodeId = nodeId;
        this.gradeId = gradeId;
        this.isPrimary = isPrimary;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /** 離任日を設定して任命を終了する。 */
    public void terminate(LocalDate endDate) {
        this.endDate = endDate;
    }
}

