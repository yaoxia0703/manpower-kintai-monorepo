package com.manpowergroup.kintai.system.domain.entity.org;

import com.baomidou.mybatisplus.annotation.*;
import com.manpowergroup.kintai.common.enums.Status;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

// 会社マスタ
@Getter
@Setter(AccessLevel.PRIVATE)
@Accessors(chain = true)
@TableName("org_company")
/** 会社階層と会社の有効状態を管理する。 */
public class OrgCompany {

    @TableId(type = IdType.AUTO)
    @Setter
    // 会社ID
    private Long id;

    // 親会社ID（NULLが最上位）
    private Long parentId;

    // 会社名
    private String name;

    // 会社コード
    private String companyCode;

    // 階層レベル（1=総本社）
    private Integer level;

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

    /** 指定階層に会社を作成する。 */
    public static OrgCompany create(Long parentId, String name, String companyCode,
                                    Integer level, Integer sort, Status status) {
        return new OrgCompany()
                .setParentId(parentId)
                .setName(name)
                .setCompanyCode(companyCode)
                .setLevel(level)
                .setSort(sort)
                .setStatus(status == null ? Status.ENABLED : status);
    }

    /** 会社の編集可能な属性を更新する。 */
    public void updateEditableFields(Long parentId, String name, String companyCode,
                                     Integer level, Integer sort) {
        this.parentId = parentId;
        this.name = name;
        this.companyCode = companyCode;
        this.level = level;
        this.sort = sort;
    }

    public void enable() {
        this.status = Status.ENABLED;
    }

    public void disable() {
        this.status = Status.DISABLED;
    }
}

