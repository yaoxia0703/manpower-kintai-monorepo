package com.manpowergroup.kintai.system.domain.entity.sys;

import com.baomidou.mybatisplus.annotation.*;
import com.manpowergroup.kintai.common.enums.Status;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

// 列挙値定義（DIVISION / DEPT / L1〜L5 等）
@Data
@Accessors(chain = true)
@TableName("sys_enum_value")
public class SysEnumValue {

    @TableId(type = IdType.AUTO)
    private Long id;

    // 列挙型コード（sys_enum_type.codeを参照）
    private String enumTypeCode;

    // 列挙値コード（例：DIVISION）
    private String code;

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
}

