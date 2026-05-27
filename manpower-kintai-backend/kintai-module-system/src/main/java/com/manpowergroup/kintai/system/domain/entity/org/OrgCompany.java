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

    private Long createdBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    private Long updatedBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    // 論理削除（0=有効 1=削除）
    @TableLogic
    private Integer isDeleted;
}

