package com.manpowergroup.kintai.system.domain.entity.sys;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

// 社員個人追加ロール関連（期間付き）
@Data
@Accessors(chain = true)
@TableName("sys_employee_role")
public class SysEmployeeRole {

    @TableId(type = IdType.AUTO)
    // 社員ロール関連ID
    private Long id;

    // 社員ID
    private Long employeeId;

    // ロールID
    private Long roleId;

    // 付与会社ID
    private Long companyId;

    // 有効開始日
    private LocalDate startDate;

    // 有効終了日（NULLは無期限）
    private LocalDate endDate;

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
}

