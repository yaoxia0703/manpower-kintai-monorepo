package com.manpowergroup.kintai.system.domain.entity.sys;

import com.baomidou.mybatisplus.annotation.*;
import com.manpowergroup.kintai.common.enums.Status;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

// 列挙型マスタ（ORG_NODE_TYPE / GRADE_LEVEL 等）
@Data
@Accessors(chain = true)
@TableName("sys_enum_type")
public class SysEnumType {

    @TableId(type = IdType.AUTO)
    private Long id;

    // 列挙型コード（例：ORG_NODE_TYPE）
    private String code;

    // 列挙型名称
    private String name;

    // 備考
    private String remark;

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

