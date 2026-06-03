package com.manpowergroup.kintai.system.domain.entity.org;

import com.baomidou.mybatisplus.annotation.*;
import com.manpowergroup.kintai.common.enums.Status;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

// 職級マスタ
@Data
@Accessors(chain = true)
@TableName("org_grade")
public class OrgGrade {

    @TableId(type = IdType.AUTO)
    private Long id;

    // 所属会社ID
    private Long companyId;

    // 職級名
    private String name;

    // 職級コード
    private String code;

    // 集団統一職級レベル（GRADE_LEVEL参照：L1〜L5）
    private String gradeLevel;

    // 表示順
    private Integer sort;

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

    public void enable() {
        this.status = Status.ENABLED;
    }

    public void disable() {
        this.status = Status.DISABLED;
    }
}

