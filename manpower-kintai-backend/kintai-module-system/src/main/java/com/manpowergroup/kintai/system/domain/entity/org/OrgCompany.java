package com.manpowergroup.kintai.system.domain.entity.org;

import com.baomidou.mybatisplus.annotation.*;
import com.manpowergroup.kintai.common.enums.Status;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

// 会社マスタ
@Data
@Accessors(chain = true)
@TableName("org_company")
public class OrgCompany {

    @TableId(type = IdType.AUTO)
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

    public void enable() {
        this.status = Status.ENABLED;
    }

    public void disable() {
        this.status = Status.DISABLED;
    }
}

