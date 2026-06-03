package com.manpowergroup.kintai.system.domain.entity.emp;

import com.baomidou.mybatisplus.annotation.*;
import com.manpowergroup.kintai.common.enums.Status;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

// 社員職位関連（兼任対応）
@Data
@Accessors(chain = true)
@TableName("emp_employee_position")
public class EmpEmployeePosition {

    @TableId(type = IdType.AUTO)
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

    private Long createdBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    private Long updatedBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableLogic
    private Integer isDeleted;

    public void terminate(LocalDate endDate) {
        this.endDate = endDate;
    }
}

