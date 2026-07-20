package com.manpowergroup.kintai.employee.domain.entity.org;

import com.baomidou.mybatisplus.annotation.*;
import com.manpowergroup.kintai.common.enums.Status;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

// 職級マスタ
@Getter
@Setter(AccessLevel.PRIVATE)
@Accessors(chain = true)
@TableName("org_grade")
/** 会社内の職級と全社共通職級レベルを管理する。 */
public class OrgGrade {

    @TableId(type = IdType.AUTO)
    @Setter
    // 職級ID
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

    /** 会社に属する職級を作成する。 */
    public static OrgGrade create(Long companyId, String name, String code,
                                  String gradeLevel, Integer sort, Status status) {
        return new OrgGrade()
                .setCompanyId(companyId)
                .setName(name)
                .setCode(code)
                .setGradeLevel(gradeLevel)
                .setSort(sort)
                .setStatus(status == null ? Status.ENABLED : status);
    }

    /** 所属会社を変更せずに職級情報を更新する。 */
    public void updateEditableFields(String name, String code, String gradeLevel, Integer sort) {
        this.name = name;
        this.code = code;
        this.gradeLevel = gradeLevel;
        this.sort = sort;
    }

    public void enable() {
        this.status = Status.ENABLED;
    }

    public void disable() {
        this.status = Status.DISABLED;
    }
}

